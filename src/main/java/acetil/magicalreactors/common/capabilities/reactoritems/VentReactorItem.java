package acetil.magicalreactors.common.capabilities.reactoritems;

public class VentReactorItem extends ReactorItem {
    int coolingRate;
    @Override
    public void endUpdate () {
        this.receiveHeat(-1 * coolingRate);
    }
    public VentReactorItem setCoolingRate (int coolingRate) {
        this.coolingRate = coolingRate;
        return this;
    }
}
