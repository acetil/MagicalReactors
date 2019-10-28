package acetil.magicalreactors.common.capabilities;

import net.minecraftforge.items.ItemStackHandler;

public interface IReactorHandler {
    void update();
    int getEnergyProduction();
    boolean isActive();
    void setActive(boolean isActive);
    int getHullHeat();
    void updateItems(ItemStackHandler itemStackHandler);
    int getTicksSinceUpdate();
    void setTicksSinceUpdate(int ticks);
    int getMaxHullHeat();
    void setHullHeat(int heat);
}
