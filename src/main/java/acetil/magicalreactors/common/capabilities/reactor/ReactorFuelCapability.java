package acetil.magicalreactors.common.capabilities.reactor;

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
}
