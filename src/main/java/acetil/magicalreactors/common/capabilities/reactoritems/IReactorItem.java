package acetil.magicalreactors.common.capabilities.reactoritems;

public interface IReactorItem {
    void receiveHeat(int heat);
    int getHeat();
    int getMaxHeat();
    int getEnergyProduced();
    int receivePulse();
    void endUpdate();
    boolean isSmart();
    int getHeatTransferRate();
    int getHullTransferRate();
    IReactorItem setMaxHeat(int maxHeat);
    IReactorItem setHeatTransferRate(int heatTransferRate);
    IReactorItem setHullTransferRate(int hullTransferRate);
    IReactorItem setIsSmart(boolean isSmart);
    void setHeat(int heat);
    int getDamage();
    int getMaxDamage();
    boolean willAcceptHeat();
}
