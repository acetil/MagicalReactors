package acetil.magicalreactors.common.capabilities.machines.machinehandlers;

import acetil.magicalreactors.common.recipes.MachineRecipe;
import acetil.magicalreactors.common.recipes.MachineRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MachineHandlerFluid extends MachineHandlerBase {
    private int fluidInputSlots;
    private int fluidOutputSlots;
    private List<FluidStack> fluidInputs;
    private List<FluidStack> fluidOutputs;
    private List<FluidStack> recipeFluidOutputs;
    public MachineHandlerFluid (String machine, int inputSlots, int outputSlots, int fluidInputSlots, int fluidOutputSlots, Supplier<Integer> energyUseRate) {
        super(machine, energyUseRate,inputSlots, outputSlots);
        this.fluidInputSlots = fluidInputSlots;
        this.fluidOutputSlots = fluidOutputSlots;
        fluidInputs = new ArrayList<>();
        fluidOutputs = new ArrayList<>();
        recipeFluidOutputs = new ArrayList<>();
    }
    public void updateFluids (MachineFluidHandler fluidHandler) {
        fluidInputs.clear();
        fluidOutputs.clear();
        for (int i = 0; i < fluidInputSlots; i++) {
            fluidInputs.add(fluidHandler.getFluid(i));
        }
        for (int i = fluidInputSlots; i < fluidInputSlots + fluidOutputSlots; i++) {
            fluidOutputs.add(fluidHandler.getFluid(i));
        }
        shouldUpdate = true;
    }
    @Override
    public void updateItems(ItemStackHandler itemStackHandler, MachineFluidHandler machineFluidHandler) {
        super.updateItems(itemStackHandler, machineFluidHandler);
        updateFluids(machineFluidHandler);
    }
    private MachineRecipeInput getFluidStackAsMRI (FluidStack f) {
        if (f == null) {
            return null;
        } else {
            return new MachineRecipeInput(f.getFluid(), f.getAmount());
        }
    }
    @Override
    public boolean holdsFluid () {
        return true;
    }
    @Override
    List<MachineRecipeInput> getInputsAsMRI () {
        List<MachineRecipeInput> recipeInputs =  super.getInputsAsMRI();
        recipeInputs.addAll(fluidInputs.stream()
                .map(this::getFluidStackAsMRI)
                .collect(Collectors.toList()));
        return recipeInputs;
    }

    @Override
    List<MachineRecipeInput> getOutputsAsMRI () {
        List<MachineRecipeInput> recipeOutputs = super.getOutputsAsMRI();
        recipeOutputs.addAll(fluidOutputs.stream()
                .map((FluidStack f) -> new MachineRecipeInput(f.getFluid(), f.getAmount()))
                .collect(Collectors.toList()));
        return recipeOutputs;
    }

    @Override
    void reduceInputQuantity (int slot, int amount, List<MachineRecipeInput> recipeInputs,
                              ItemStackHandler itemStackHandler, MachineFluidHandler machineFluidHandler) {
        if (slot < inputSlots) {
            super.reduceInputQuantity(slot, amount, recipeInputs, itemStackHandler, machineFluidHandler);
        } else {
            machineFluidHandler.removeFluid(amount, slot - inputSlots, false);
        }

    }
    @Override
    void setRecipeOutputs (MachineRecipe recipe) {
        for (MachineRecipeInput m : recipe.getOutputs()) {
            if (!m.isFluid()) {
                recipeOutputs.add(new ItemStack(m.getItemInput(), m.getQuantity()));
            } else {
                recipeFluidOutputs.add(new FluidStack(m.getFluidInput(), m.getQuantity()));
            }
        }
    }
    @Override
    void completeRecipe (ItemStackHandler itemStackHandler, MachineFluidHandler machineFluidHandler) {
        if (itemStackHandler != null) {
            for (int i = 0; i < recipeOutputs.size(); i++) {
                itemStackHandler.insertItem(i + inputSlots, recipeOutputs.get(i), false);
            }
        }
        for (int i = 0; i < recipeFluidOutputs.size(); i++) {
            machineFluidHandler.addFluid(recipeFluidOutputs.get(i),i + fluidInputSlots, false);
        }
        recipeOutputs.clear();
        recipeFluidOutputs.clear();
        updateItems(itemStackHandler, machineFluidHandler);
        isInRecipe = false;
    }
    public List<FluidStack> getRecipeFluidOutputs () {
        return recipeFluidOutputs;
    }
    public void setRecipeFluidOutputs (List<FluidStack> recipeFluidOutputs) {
        this.recipeFluidOutputs = recipeFluidOutputs;
    }

}
