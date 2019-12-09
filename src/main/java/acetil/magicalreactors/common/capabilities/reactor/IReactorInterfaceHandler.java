package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.reactor.IReactorFuel;

import java.util.ArrayList;
import java.util.List;

public interface IReactorInterfaceHandler {
    void updateInterface (IReactorHandlerNew reactorHandler, IReactorControlCapability reactorControl);
    default int getPowerAcceptance () {
        return 0;
    }

    default List<IReactorFuel> getReactorFuels () {
        return new ArrayList<>();
    }

    default int receivePower (int power) {
        return 0;
    }
}
