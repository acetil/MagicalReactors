package acetil.magicalreactors.common.reactor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IReactorFuel {
    int getCurrentDurability ();
    int getMaxDurability ();
    int getEnergyProduced (int heat, int maxHeat);
    int getHeatProduced ();
    void damage ();
    void readNBT (NBTTagCompound nbt);
    ItemStack getByproduct ();
    String getName ();
    NBTTagCompound writeNBT ();
}
