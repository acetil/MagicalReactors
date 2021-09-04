package acetil.magicalreactors.common.reactor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IReactorFuel {
    int getCurrentDurability ();
    int getMaxDurability ();
    int getEnergyProduced (int heat, int maxHeat);
    int getHeatProduced ();
    void damage ();
    void readNBT (CompoundTag nbt);
    ItemStack getByproduct ();
    String getName ();
    CompoundTag writeNBT ();
}
