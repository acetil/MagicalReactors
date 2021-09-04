package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.reactor.IReactorFuel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IReactorHandlerNew {
    int getHeat ();
    void setHeat (int heat);
    int getEnergyProduced ();
    void update ();
    void cool (int cooling);
    boolean finished ();
    CompoundTag writeNBT ();
    void readNBT (CompoundTag nbt);
    void setFuels (List<IReactorFuel> fuels);
    void setNumSlots (int numSlots);
    // TODO: update
    ItemStack getNextOutput (boolean simulate);
    void debugMessage (Player player);
}
