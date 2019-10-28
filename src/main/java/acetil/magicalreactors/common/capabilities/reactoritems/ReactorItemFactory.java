package acetil.magicalreactors.common.capabilities.reactoritems;

public class ReactorItemFactory {
    private int maxHeat;
    private boolean isSmart;
    private int heatTransferRate;
    private int hullTransferRate;
    public ReactorItemFactory (int maxHeat, boolean isSmart, int heatTransferRate, int hullTransferRate) {
        this.maxHeat = maxHeat;
        this.isSmart = isSmart;
        this.heatTransferRate = heatTransferRate;
        this.hullTransferRate = hullTransferRate;
    }
    public IReactorItem createReactorItem () {
        ReactorItem item = new ReactorItem();
        item.setMaxHeat(maxHeat);
        item.setHeatTransferRate(heatTransferRate);
        item.setHullTransferRate(hullTransferRate);
        item.setIsSmart(isSmart);
        return item;
    }
}
