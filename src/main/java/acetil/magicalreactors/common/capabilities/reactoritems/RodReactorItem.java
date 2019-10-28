package acetil.magicalreactors.common.capabilities.reactoritems;

import acetil.magicalreactors.common.lib.LibReactor;

public class RodReactorItem extends DamageableReactorItem {
    int pulses = 0;
    int efficiency = 0;
    int energyPerPulse = 0;
    int heatCoefficient = 0;
    int defaultEfficiency = 0;
    @Override
    public int getEnergyProduced () {
        //System.out.println("Efficiency: " + efficiency);
        return efficiency * energyPerPulse;
    }
    @Override
    public int receivePulse () {
        efficiency++;
        int gainedHeat = efficiency * (efficiency + 1) * heatCoefficient - (efficiency - 1) * (efficiency) * heatCoefficient;
        receiveHeat(gainedHeat);
        damage++;
        return LibReactor.PULSE_ALL;
    }
    @Override
    public void endUpdate () {
        efficiency = 0;
    }
    @Override
    public IReactorItem setMaxHeat(int maxHeat) {
        super.setMaxHeat(maxHeat);
        setHeatTransferRate(maxHeat);
        setHullTransferRate(-1 * maxHeat);
        return this;
    }
    public RodReactorItem setEnergyPerPulse (int energyPerPulse) {
        this.energyPerPulse = energyPerPulse;
        return this;
    }
    public RodReactorItem setHeatCoefficient (int heatCoefficient) {
        this.heatCoefficient = heatCoefficient;
        return this;
    }
    public RodReactorItem setDefaultEfficiency (int defaultEfficiency) {
        this.defaultEfficiency = defaultEfficiency;
        return this;
    }
    @Override
    public boolean willAcceptHeat () {
        return false;
    }
}
