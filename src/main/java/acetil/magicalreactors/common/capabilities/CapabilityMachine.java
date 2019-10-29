package acetil.magicalreactors.common.capabilities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.MachineHandlerBase;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.MachineHandlerFluid;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.MachineRedstone;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CapabilityMachine {
    @CapabilityInject(IMachineCapability.class)
    public static Capability<IMachineCapability> MACHINE_CAPABILITY = null;
    public static void register () {
        CapabilityManager.INSTANCE.register(IMachineCapability.class, new Capability.IStorage<IMachineCapability>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IMachineCapability> capability, IMachineCapability instance, Direction side) {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putInt("energyToCompletion", instance.energyToCompletion());
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
                nbt.putInt("redstoneRequirement", redstone);
                nbt.putBoolean("inRecipe", instance.isInRecipe());
                CompoundNBT outputNBT = new CompoundNBT();
                outputNBT.putInt("num_item_outputs", instance.getRecipeOutputs().size());
                CompoundNBT itemsNBT = new CompoundNBT();
                for (int i = 0; i < instance.getRecipeOutputs().size(); i++) {
                    ItemStack stack = instance.getRecipeOutputs().get(i);
                    CompoundNBT itemStackCompound = new CompoundNBT();
                    itemStackCompound.putString("item", stack.getItem().getRegistryName().toString());
                    itemStackCompound.putInt("count", stack.getCount());
                    if (stack.getTag() != null) {
                        itemStackCompound.put("nbt", stack.getTag());
                    }
                    itemsNBT.put("item" + i, itemStackCompound);
                }
                outputNBT.put("items", itemsNBT);
                if (instance instanceof MachineHandlerFluid) {
                    outputNBT.putString("hasFluid", "");
                    outputNBT.putInt("num_fluid_outputs", ((MachineHandlerFluid)instance).getRecipeFluidOutputs().size());
                    CompoundNBT fluidNBT = new CompoundNBT();
                    for (int i = 0; i < ((MachineHandlerFluid)instance).getRecipeFluidOutputs().size(); i++) {
                        FluidStack stack = ((MachineHandlerFluid)instance).getRecipeFluidOutputs().get(i);
                        CompoundNBT fluidStackCompound = new CompoundNBT();
                        fluidStackCompound.putString("fluid", stack.getFluid().getRegistryName().toString());
                        fluidStackCompound.putInt("amount", stack.getAmount());
                        if (stack.getTag() != null) {
                            fluidStackCompound.put("nbt", stack.getTag());
                        }
                        fluidNBT.put("fluid" + i, fluidStackCompound);
                    }
                    outputNBT.put("fluids", fluidNBT);
                }
                nbt.put("output", outputNBT);
                return nbt;
            }

            @Override
            public void readNBT(Capability<IMachineCapability> capability, IMachineCapability instance, Direction side, INBT nbt) {
                CompoundNBT tags = (CompoundNBT) nbt;
                instance.setEnergyToCompletion(tags.getInt("energyToCompletion"));
                int redstone = tags.getInt("redstoneRequirement");
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
                CompoundNBT outputNBT = ((CompoundNBT) nbt).getCompound("output");
                int itemsCount = outputNBT.getInt("num_item_outputs");
                CompoundNBT itemsNBT = outputNBT.getCompound("items");
                for (int i = 0; i < itemsCount; i++) {
                    CompoundNBT itemStackNBT = itemsNBT.getCompound("item" + i);
                    ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemStackNBT.getString("item"))),
                            itemStackNBT.getInt("count"));
                    if (itemStackNBT.contains("nbt")) {
                        stack.setTag(itemStackNBT.getCompound("nbt"));
                    }
                    recipeOutputs.add(stack);
                }
                instance.setRecipeOutputs(recipeOutputs);

                if (outputNBT.contains("hasFluid") && instance instanceof MachineHandlerFluid) {
                    int fluidsCount = outputNBT.getInt("num_fluid_outputs");
                    CompoundNBT fluidsNBT = outputNBT.getCompound("fluids");
                    for (int i = 0; i < fluidsCount; i++) {
                        CompoundNBT fluidStackNBT = fluidsNBT.getCompound("fluid" + i);
                        FluidStack stack = new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidStackNBT.getString("fluid"))),
                                fluidStackNBT.getInt("amount"));
                        if (fluidStackNBT.contains("nbt")) {
                            stack.setTag(fluidStackNBT.getCompound("nbt"));
                        }
                    }
                    ((MachineHandlerFluid)instance).setRecipeFluidOutputs(fluidRecipeOutputs);
                }
            }

            /*@Nonnull
            @Override
            public NBTBase writeNBT(Capability<IMachineCapability> capability, IMachineCapability instance, EnumFacing side) {
                CompoundNBT nbt = new CompoundNBT();
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
                CompoundNBT outputNBT = new CompoundNBT();
                outputNBT.setInteger("num_item_outputs", instance.getRecipeOutputs().size());
                CompoundNBT itemsNBT = new CompoundNBT();
                for (int i = 0; i < instance.getRecipeOutputs().size(); i++) {
                    ItemStack stack = instance.getRecipeOutputs().get(i);
                    CompoundNBT itemStackCompound = new CompoundNBT();
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
                    CompoundNBT fluidNBT = new CompoundNBT();
                    for (int i = 0; i < ((MachineHandlerFluid)instance).getRecipeFluidOutputs().size(); i++) {
                        FluidStack stack = ((MachineHandlerFluid)instance).getRecipeFluidOutputs().get(i);
                        CompoundNBT fluidStackCompound = new CompoundNBT();
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
                CompoundNBT tags = (CompoundNBT) nbt;
                instance.setEnergyToCompletion(tags.getInt("energyToCompletion"));
                int redstone = tags.getInt("redstoneRequirement");
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
                CompoundNBT outputNBT = ((CompoundNBT) nbt).getCompound("output");
                int itemsCount = outputNBT.getInt("num_item_outputs");
                CompoundNBT itemsNBT = outputNBT.getCompound("items");
                for (int i = 0; i < itemsCount; i++) {
                    CompoundNBT itemStackNBT = itemsNBT.getCompound("item" + i);
                    ItemStack stack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(itemStackNBT.getString("item"))),
                            itemStackNBT.getInt("count"));
                    if (itemStackNBT.hasKey("nbt")) {
                        stack.setTagCompound(itemStackNBT.getCompound("nbt"));
                    }
                    recipeOutputs.add(stack);
                }
                instance.setRecipeOutputs(recipeOutputs);

                if (outputNBT.hasKey("hasFluid") && instance instanceof MachineHandlerFluid) {
                    int fluidsCount = outputNBT.getInt("num_fluid_outputs");
                    CompoundNBT fluidsNBT = outputNBT.getCompound("fluids");
                    for (int i = 0; i < fluidsCount; i++) {
                        CompoundNBT fluidStackNBT = fluidsNBT.getCompound("fluid" + i);
                        FluidStack stack = new FluidStack(FluidRegistry.getFluid(fluidStackNBT.getString("fluid")),
                                fluidStackNBT.getInt("amount"));
                        if (fluidStackNBT.hasKey("nbt")) {
                            stack.tag = fluidStackNBT.get("nbt");
                        }
                    }
                    ((MachineHandlerFluid)instance).setRecipeFluidOutputs(fluidRecipeOutputs);
                }
            } */
        }, () -> new MachineHandlerBase("centrifuge", 40, 1, 2));
    }
}
