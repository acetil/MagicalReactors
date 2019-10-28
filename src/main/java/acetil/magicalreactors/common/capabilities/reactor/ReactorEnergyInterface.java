package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.capabilities.EnergyHandler;

public class ReactorEnergyInterface implements IReactorInterfaceHandler {
    private EnergyHandler energyHandler;
    public ReactorEnergyInterface (EnergyHandler energyHandler) {
        this.energyHandler = energyHandler;
    }
    @Override
    public void updateInterface(IReactorHandlerNew reactorHandler, IReactorControlCapability reactorControl) {

    }

    @Override
    public int getPowerAcceptance () {
        return energyHandler.getMaxEnergyStored() - energyHandler.getEnergyStored();
    }
    @Override
    public int receivePower (int energy) {
        return energyHandler.receiveEnergy(energy, false);
    }
}
