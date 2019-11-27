package acetil.magicalreactors.common.capabilities.reactor;

public class ReactorRedstoneInterface implements IReactorInterfaceHandler {
    boolean isPowered;
    @Override
    public void updateInterface(IReactorHandlerNew reactorHandler, IReactorControlCapability reactorControl) {
        reactorControl.setIsPowered(isPowered);
    }
    public void setPowered (boolean isPowered) {
        this.isPowered = isPowered;
    }
}
