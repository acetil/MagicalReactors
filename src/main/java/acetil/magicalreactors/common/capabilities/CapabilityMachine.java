package acetil.magicalreactors.common.capabilities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.MachineHandlerBase;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.MachineHandlerFluid;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.MachineRedstone;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CapabilityMachine {
    @CapabilityInject(IMachineCapability.class)
    public static Capability<IMachineCapability> MACHINE_CAPABILITY = null;
    public static void register () {
        CapabilityManager.INSTANCE.register(IMachineCapability.class, new Capability.IStorage<IMachineCapability>() {
            @Nonnull
            @Override
            public NBTBase writeNBT(Capability<IMachineCapability> capability, IMachineCapability instance, EnumFacing side) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("energyToCompletion", instance.energyToCompletion());
                int redstone;
                switch (instance.getRedstoneRequirement()) {
                    case REQUIRED_POWERED: {
                        redstone = 1;
                    }
                        break;
                    case REQUIRED_UNPOWERED: {
                        redstone = 2;
                    }
                        break;
                    default: {
                        redstone = 0;
                    }
                }
                nbt.setInteger("redstoneRequirement", redstone);
                nbt.setBoolean("inRecipe", instance.isInRecipe());
                NBTTagCompound outputNBT = new NBTTagCompound();
                outputNBT.setInteger("num_item_outputs", instance.getRecipeOutputs().size());
                NBTTagCompound itemsNBT = new NBTTagCompound();
                for (int i = 0; i < instance.getRecipeOutputs().size(); i++) {
                    ItemStack stack = instance.getRecipeOutputs().get(i);
                    NBTTagCompound itemStackCompound = new NBTTagCompound();
                    itemStackCompound.setString("item", stack.getItem().getRegistryName().toString());
                    itemStackCompound.setInteger("count", stack.getCount());
                    if (stack.getTagCompound() != null) {
                        itemStackCompound.setTag("nbt", stack.getTagCompound());
                    }
                    itemsNBT.setTag("item" + i, itemStackCompound);
                }
                outputNBT.setTag("items", itemsNBT);
                if (instance instanceof MachineHandlerFluid) {
                    outputNBT.setString("hasFluid", "");
                    outputNBT.setInteger("num_fluid_outputs", ((MachineHandlerFluid)instance).getRecipeFluidOutputs().size());
                    NBTTagCompound fluidNBT = new NBTTagCompound();
                    for (int i = 0; i < ((MachineHandlerFluid)instance).getRecipeFluidOutputs().size(); i++) {
                        FluidStack stack = ((MachineHandlerFluid)instance).getRecipeFluidOutputs().get(i);
                        NBTTagCompound fluidStackCompound = new NBTTagCompound();
                        fluidStackCompound.setString("fluid", stack.getFluid().getName());
                        fluidStackCompound.setInteger("amount", stack.amount);
                        if (stack.tag != null) {
                            fluidStackCompound.setTag("nbt", stack.tag);
                        }
                        fluidNBT.setTag("fluid" + i, fluidStackCompound);
                    }
                    outputNBT.setTag("fluids", fluidNBT);
                }
                nbt.setTag("output", outputNBT);
                return nbt;
            }
            @Override
            public void readNBT(Capability<IMachineCapability> capability, IMachineCapability instance, EnumFacing side, NBTBase nbt) {
                NBTTagCompound tags = (NBTTagCompound) nbt;
                instance.setEnergyToCompletion(tags.getInteger("energyToCompletion"));
                int redstone = tags.getInteger("redstoneRequirement");
                switch (redstone) {
                    case 1:
                        instance.setRedstoneRequirement(MachineRedstone.REQUIRED_POWERED);
                        break;
                    case 2:
                        instance.setRedstoneRequirement(MachineRedstone.REQUIRED_UNPOWERED);
                        break;
                    default:
                        instance.setRedstoneRequirement(MachineRedstone.NONE);
                        break;
                }
                instance.setIsInRecipe(tags.getBoolean("inRecipe"));

                List<ItemStack> recipeOutputs = new ArrayList<>();
                List<FluidStack> fluidRecipeOutputs = new ArrayList<>();
                NBTTagCompound outputNBT = ((NBTTagCompound) nbt).getCompoundTag("output");
                int itemsCount = outputNBT.getInteger("num_item_outputs");
                NBTTagCompound itemsNBT = outputNBT.getCompoundTag("items");
                for (int i = 0; i < itemsCount; i++) {
                    NBTTagCompound itemStackNBT = itemsNBT.getCompoundTag("item" + i);
                    ItemStack stack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(itemStackNBT.getString("item"))),
                            itemStackNBT.getInteger("count"));
                    if (itemStackNBT.hasKey("nbt")) {
                        stack.setTagCompound(itemStackNBT.getCompoundTag("nbt"));
                    }
                    recipeOutputs.add(stack);
                }
                instance.setRecipeOutputs(recipeOutputs);

                if (outputNBT.hasKey("hasFluid") && instance instanceof MachineHandlerFluid) {
                    int fluidsCount = outputNBT.getInteger("num_fluid_outputs");
                    NBTTagCompound fluidsNBT = outputNBT.getCompoundTag("fluids");
                    for (int i = 0; i < fluidsCount; i++) {
                        NBTTagCompound fluidStackNBT = fluidsNBT.getCompoundTag("fluid" + i);
                        FluidStack stack = new FluidStack(FluidRegistry.getFluid(fluidStackNBT.getString("fluid")),
                                fluidStackNBT.getInteger("amount"));
                        if (fluidStackNBT.hasKey("nbt")) {
                            stack.tag = fluidStackNBT.getCompoundTag("nbt");
                        }
                    }
                    ((MachineHandlerFluid)instance).setRecipeFluidOutputs(fluidRecipeOutputs);
                }
            }
        }, () -> new MachineHandlerBase("centrifuge", 40, 1, 2));
    }
}
