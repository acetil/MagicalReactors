package acetil.magicalreactors.client.gui.data;

import acetil.magicalreactors.common.MagicalReactors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GuiDataNamespace <T, U> extends GuiDataVariable<T, U> {
    protected final Map<String, GuiDataVariable<U, ?>> subvalueMap;
    public GuiDataNamespace (Function<T, U> func) {
        super(func);
        subvalueMap = new HashMap<>();
    }

    @Override
    public void addSubValue (String varPath, GuiDataVariable<?, ?> var) {
        String[] sList = varPath.split("\\.");
        MagicalReactors.LOGGER.debug(String.join(", ", sList));
        if (subvalueMap.containsKey(sList[0])) {
            if (sList.length == 1) {
                MagicalReactors.LOGGER.error("Duplicate variable name \"{}\" in namespace \"{}\"!",
                        varPath, fullNamespaceName);
            } else {
                subvalueMap.get(sList[0]).addSubValue(String.join(".",
                        Arrays.copyOfRange(sList, 1, sList.length)), var);
                return;
            }
        }
        if (sList.length == 1) {
            var.setFullVariablePath(fullNamespaceName + "." + varPath);

            // be very careful, could cause very hard to debug bugs!
            //noinspection unchecked
            subvalueMap.put(varPath, (GuiDataVariable<U, ?>) var);
        } else {
            MagicalReactors.LOGGER.error("Variable \"{}\" does not exist in namespace \"{}\"!", sList[0],
                    fullNamespaceName);
        }
    }

    @Override
    public <A> Function<A, ?> getSubValue (String val, Function<A, T> current) {
        return getSubValueInternal(val, func.compose(current));
    }

    protected  <A> Function<A, ?> getSubValueInternal (String varPath, Function<A, U> fun) {
        if (varPath.equals("")) {
            return fun;
        }
        String[] sList = varPath.split("\\.");
        if (subvalueMap.containsKey(sList[0])) {
            GuiDataVariable<U, ?> var = subvalueMap.get(sList[0]);
            String newNameStr = "";
            if (sList.length > 1) {
                newNameStr = String.join(".", Arrays.copyOfRange(sList, 1, sList.length));
            }
            return var.getSubValue(newNameStr, fun);
        }
        throw new IllegalStateException(
                String.format("Cannot get subvalue \"%s\" from current gui namespace \"%s\"!", varPath,
                        getFullVariablePath()));
    }
}
