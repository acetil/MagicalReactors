package acetil.magicalreactors.client.gui.data;

import java.util.function.Function;

public class GuiDataVariable<T, U>   {
    protected final Function<T, U> func;
    protected String fullNamespaceName = "";
    public GuiDataVariable (Function<T, U> func) {
        this.func = func;
    }
    public Function<T, U> getValueFunc () {
        return func;
    }

    public String getFullVariablePath () {
        return fullNamespaceName;
    }
    public void setFullVariablePath (String name) {
        fullNamespaceName = name;
    }
    public <A> Function<A, ?> getSubValue (String val, Function<A, T> current) {
        if (val.equals("")) {
            Function<T, U> f = getValueFunc();
            return (A a) -> f.apply(current.apply(a));
        }
        throw new IllegalStateException(
                String.format("Cannot get subvalue \"%s\" from current gui value \"%s\"!", val, getFullVariablePath()));
    }
    public void addSubValue (String varName, GuiDataVariable<?, ?> var) {
        throw new IllegalStateException(String.format("Cannot add subvalue \"%s\" to current gui variable \"%s\"!",
                varName, getFullVariablePath()));
    }
}
