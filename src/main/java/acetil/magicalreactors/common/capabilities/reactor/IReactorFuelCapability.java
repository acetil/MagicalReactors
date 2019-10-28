package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.reactor.IReactorFuel;

public interface IReactorFuelCapability {
    String getName ();
    void setName (String name);
    IReactorFuel getFuel ();
}
