package acetil.magicalreactors.common.capabilities.reactoritems;

import acetil.magicalreactors.common.lib.LibReactor;

public class ReactorItem implements IReactorItem {
    private int heat = 0;
    private int maxHeat = LibReactor.DEFAULT_MAX_HEAT;
    private boolean isSmart = false;
    private int hullTransferRate = 0;
    private int heatTransferRate = 0;
    @Override
    public void receiveHeat(int heat) {
        this.heat += heat;
    }

    @Override
    public int getHeat() {
        return heat;
    }

    @Override
    public int getMaxHeat() {
        return maxHeat;
    }

    @Override
    public int getEnergyProduced() {
        return 0;
    }

    @Override
    public int receivePulse() {
        return LibReactor.NO_RESPONSE;
    }

    @Override
    public void endUpdate() {

    }

    @Override
    public boolean isSmart() {
        return isSmart;
    }

    @Override
    public int getHeatTransferRate() {
        return heatTransferRate;
    }

    @Override
    public int getHullTransferRate() {
        return hullTransferRate;
    }

    @Override
    public IReactorItem setMaxHeat(int maxHeat) {
        this.maxHeat = maxHeat;
        return this;
    }

    @Override
    public IReactorItem setHeatTransferRate(int heatTransferRate) {
        this.heatTransferRate = heatTransferRate;
        return this;
    }

    @Override
    public IReactorItem setHullTransferRate(int hullTransferRate) {
        this.hullTransferRate = hullTransferRate;
        return this;
    }

    @Override
    public IReactorItem setIsSmart(boolean isSmart) {
        this.isSmart = isSmart;
        return this;
    }

    @Override
    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getDamage () {
        return heat;
    }

    @Override
    public int getMaxDamage() {
        return maxHeat;
    }

    public boolean willAcceptHeat () {
        return true;
    }
}
