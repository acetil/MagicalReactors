package acetil.magicalreactors.common.capabilities.machines.machinehandlers;

import acetil.magicalreactors.common.network.MessageMachineUpdate;
import acetil.magicalreactors.common.network.PacketHandler;
import acetil.magicalreactors.common.recipes.MachineRecipe;
import acetil.magicalreactors.common.recipes.MachineRecipeInput;
import acetil.magicalreactors.common.recipes.MachineRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineFluidHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
// TODO: consider refactor
public class MachineHandlerBase implements IMachineCapability {
    int inputSlots;
    int outputSlots;
    boolean isOn = true;
    MachineRedstone redstoneRequired = MachineRedstone.REQUIRED_POWERED;
    String machine;
    Supplier<Integer> energyUseRate;
    int energyToCompletion;
    int energyPerTick;
    int totalEnergyRequired;
    int pastEnergyPerTick;
    boolean shouldUpdate;
    List<ItemStack> inputs;
    List<ItemStack> outputs;
    List<ItemStack> recipeOutputs;
    boolean isInRecipe = false;
    boolean shouldSendPacket = false;
    public MachineHandlerBase (String machine, Supplier<Integer> energyUseRate, int inputSlots, int outputSlots) {
        this.machine = machine;
        this.energyUseRate = energyUseRate;
        this.energyToCompletion = 0;
        this.totalEnergyRequired = 0;
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.recipeOutputs = new ArrayList<>();
        shouldUpdate = false;
    }
    @Override
    public int addEnergy(int power) {
        int originalEnergy = energyToCompletion;
        energyToCompletion -= Math.min(Math.min(power, energyUseRate.get()), energyToCompletion);
        pastEnergyPerTick = energyPerTick;
        energyPerTick = originalEnergy - energyToCompletion;
        return energyPerTick;
    }

    @Override
    public int energyToCompletion() {
        return energyToCompletion;
    }

    public void setEnergyToCompletion (int energy) {
        energyToCompletion = energy;
    }

    @Override
    public int getEnergyUseRate() {
        return energyUseRate.get();
    }

    @Override
    public int energyRequired() {
        return totalEnergyRequired;
    }

    @Override
    public int getEnergyPerTick() {
        return energyPerTick;
    }

    @Override
    public boolean workFinished() {
        return energyToCompletion == 0 && isInRecipe;
    }

    @Override
    public void updateItems(ItemStackHandler itemStackHandler, MachineFluidHandler machineFluidHandler) {
        inputs.clear();
        outputs.clear();
        if (itemStackHandler != null) {
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                if (i < inputSlots) {
                    inputs.add(itemStackHandler.getStackInSlot(i));
                } else {
                    outputs.add(itemStackHandler.getStackInSlot(i));
                }
            }
        }
        shouldUpdate = true;
    }

    @Override
    public void updateRedstone(boolean powered) {
        if (redstoneRequired != MachineRedstone.NONE) {
            isOn = (powered && redstoneRequired == MachineRedstone.REQUIRED_POWERED) ||
                    (!powered && redstoneRequired == MachineRedstone.REQUIRED_UNPOWERED);
            shouldUpdate = true;
            if (!isOn) {
                energyPerTick = 0;
                pastEnergyPerTick = 0;
            }
        }
    }

    @Override
    public boolean holdsFluid() {
        return false;
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    @Override
    public MachineRedstone getRedstoneRequirement() {
        return redstoneRequired;
    }

    @Override
    public void setRedstoneRequirement(MachineRedstone m) {
        redstoneRequired = m;
    }

    @Override
    public boolean shouldUpdateWork() {
        return workFinished() || shouldUpdate;
    }

    List<MachineRecipeInput> getInputsAsMRI () {
        return inputs.stream()
                .map((ItemStack i) -> new MachineRecipeInput(i.getItem(), i.getCount()))
                .collect(Collectors.toList());
    }
    List<MachineRecipeInput> getOutputsAsMRI () {
        return outputs.stream()
                .map((ItemStack i) -> new MachineRecipeInput(i.getItem(), i.getCount()))
                .collect(Collectors.toList());
    }
    void reduceInputQuantity (int slot, int amount, List<MachineRecipeInput> recipeInputs, ItemStackHandler itemStackHandler,
                              MachineFluidHandler machineFluidHandler) {
        itemStackHandler.extractItem(slot, amount, false);
    }
    void setRecipeOutputs (MachineRecipe recipe) {
        recipeOutputs = recipe.getOutputs().stream()
                .map((MachineRecipeInput m) -> new ItemStack(m.getItemInput(), m.getQuantity())) // doesn't work for fluid recipes!
                .collect(Collectors.toList());
    }
    void completeRecipe (ItemStackHandler itemStackHandler, MachineFluidHandler machineFluidHandler) {
        for (int i = 0; i < recipeOutputs.size(); i++) {
            itemStackHandler.insertItem(i + inputSlots, recipeOutputs.get(i), false);
        }
        recipeOutputs.clear();
        updateItems(itemStackHandler, machineFluidHandler);
        isInRecipe = false;
    }
    @Override
    public void updateWork(ItemStackHandler itemStackHandler, MachineFluidHandler machineFluidHandler) {
        System.out.println("Updating work");
        shouldSendPacket = true;
        // TODO clean up
        if (workFinished()) {
            completeRecipe(itemStackHandler, machineFluidHandler);
        }
        if (isInRecipe) {
            return;
        }
        List<MachineRecipeInput> inputsAsMRI = getInputsAsMRI();
        List<MachineRecipeInput> outputsAsMRI = getOutputsAsMRI();
        MachineRecipe recipe = MachineRecipeManager.getRecipe(inputsAsMRI, machine);
        if (recipe == null) {
            shouldUpdate = false;
            totalEnergyRequired = 0;
            return;
        }
        boolean canDoWork = true;
        for (MachineRecipeInput m : outputsAsMRI) {
            if (!recipe.getOutputs().contains(m) && !m.isEmpty()) {
                canDoWork = false; //output is blocked
            } else {
                int index = recipe.getOutputs().indexOf(m);
                int slotIndex = outputsAsMRI.indexOf(m);
                if (index < 0) {
                    continue; // no match of item
                }
                if (m.getQuantity() + recipe.getOutputs().get(index).getQuantity() >
                        outputs.get(slotIndex).getMaxStackSize()) {
                    canDoWork = false; // too many in the output slot
                    totalEnergyRequired = 0;
                }
            }
        }
        boolean hasRequiredQuantity = true;
        for (MachineRecipeInput m : inputsAsMRI) {
            if (!m.isEmpty() && m.getQuantity() < recipe.getInputs()
                    .get(recipe.getInputs().indexOf(m))
                    .getQuantity()) {
                hasRequiredQuantity = false; // input matching recipe input doesn't have required quantity
            }
        }
        if (canDoWork && hasRequiredQuantity) {
            Map<Integer, Integer> quantityMap = new HashMap<>();
            for (int i = 0; i < inputsAsMRI.size(); i++) {
                quantityMap.put(i, recipe.getInputs()
                        .get(recipe.getInputs()
                                .indexOf(inputsAsMRI.get(i)))
                        .getQuantity());
            }
            for (int i : quantityMap.keySet()) {
                reduceInputQuantity(i, quantityMap.get(i), inputsAsMRI, itemStackHandler, machineFluidHandler);
            }
            updateItems(itemStackHandler, machineFluidHandler);
            shouldUpdate = false;
            setRecipeOutputs(recipe);
            energyToCompletion = recipe.getPowerRequired();
            totalEnergyRequired = recipe.getPowerRequired();
            isInRecipe = true;
        }
    }

    @Override
    public boolean isInRecipe() {
        return isInRecipe;
    }

    @Override
    public void setIsInRecipe(boolean isInRecipe) {
        this.isInRecipe = isInRecipe;
    }

    @Override
    public List<ItemStack> getRecipeOutputs() {
        return recipeOutputs;
    }
    @Override
    public void setRecipeOutputs (List<ItemStack> recipeOutputs) {
        this.recipeOutputs = recipeOutputs;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handlePacket(boolean isOn, int energyPerTick, int energyToCompletion, int totalEnergyRequired) {
        this.isOn = isOn;
        this.energyPerTick = energyPerTick;
        this.energyToCompletion = energyToCompletion;
        this.totalEnergyRequired = totalEnergyRequired;
        System.out.println("Handling machine packet!");
    }

    @Override
    public void updateClient() {
        if (isOn) {
            energyToCompletion = Math.max(0, energyToCompletion - energyPerTick);
        }
    }

    @Override
    public void sync(World world, BlockPos pos) {
        if (shouldSendPacket || energyPerTick != pastEnergyPerTick) {
            PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)),
                    new MessageMachineUpdate(pos, isOn,
                            energyPerTick, energyToCompletion, totalEnergyRequired));
            shouldSendPacket = false;
        }
    }

}
