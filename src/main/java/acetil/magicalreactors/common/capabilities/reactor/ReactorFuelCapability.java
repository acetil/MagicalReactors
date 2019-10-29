package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.reactor.IReactorFuel;
import acetil.magicalreactors.common.reactor.ReactorFuelRegistry;

public class ReactorFuelCapability implements IReactorFuelCapability {
    private String name;
    public ReactorFuelCapability (String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public IReactorFuel getFuel() {
        return ReactorFuelRegistry.getFuel(name);
    }
}
