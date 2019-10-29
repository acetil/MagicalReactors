package acetil.magicalreactors.common.capabilities.machines.machinehandlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineFluidHandler;

import java.util.List;
    // TODO: consider refactor
public interface IMachineCapability {
    int addEnergy (int energy);
    int energyToCompletion ();
    void setEnergyToCompletion (int energy);
    int getEnergyUseRate ();
    int energyRequired ();
    int getEnergyPerTick ();
    boolean workFinished ();
    void updateItems (ItemStackHandler itemStackHandler, MachineFluidHandler machineFluidHandler);
    void updateRedstone (boolean powered);
    boolean holdsFluid ();
    boolean isOn ();
    MachineRedstone getRedstoneRequirement ();
    void setRedstoneRequirement (MachineRedstone m);
    boolean shouldUpdateWork ();
    void updateWork (ItemStackHandler itemStackHandler, MachineFluidHandler machineFluidHandler);
    boolean isInRecipe ();
    void setIsInRecipe (boolean isInRecipe);
    List<ItemStack> getRecipeOutputs ();
    void setRecipeOutputs (List<ItemStack> recipeOutputs);
    @OnlyIn(Dist.CLIENT)
    void handlePacket (boolean isOn, int energyPerTick, int energyToCompletion, int totalEnergyRequired);
    @OnlyIn(Dist.CLIENT)
    void updateClient ();
}
