package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.reactor.IReactorFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IReactorHandlerNew {
    int getHeat ();
    void setHeat (int heat);
    int getEnergyProduced ();
    void update ();
    void cool (int cooling);
    boolean finished ();
    NBTTagCompound writeNBT ();
    void readNBT (NBTTagCompound nbt);
    void setFuels (IReactorFuel[] fuels);
    void setNumSlots (int numSlots);
    // TODO: update
    ItemStack getNextOutput (boolean simulate);
}
