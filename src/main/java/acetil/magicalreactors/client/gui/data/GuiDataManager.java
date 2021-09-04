package acetil.magicalreactors.client.gui.data;

import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GuiDataManager {
    private static final GuiDataNamespaceTopLevel GUI_DATA =
            new GuiDataNamespaceTopLevel();
    private static final Map<String, Function<BlockEntity, ?>> LAZY_COMPILED_FUNCS = new HashMap<>();

    public static Function<BlockEntity, ?> getVariable (String varPath) {
        String s = varPath.trim();
        if (LAZY_COMPILED_FUNCS.containsKey(s)) {
            return LAZY_COMPILED_FUNCS.get(s);
        } else if (isValidVarPath(s)) {
            MagicalReactors.LOGGER.debug("Compiling gui data path \"{}\"", s);
            Function<BlockEntity, ?> f =  GUI_DATA.getSubValue(s);
            LAZY_COMPILED_FUNCS.put(s, f);
            return f;
        }
        MagicalReactors.LOGGER.error("Gui data path \"{}\" is invalid!", s);
        LAZY_COMPILED_FUNCS.put(s, (BlockEntity te) -> te);
        return (BlockEntity te) -> te;
    }
    public static Function<BlockEntity, String> getVariableString (String varStr) {
        Function<Object, String> f = Object::toString;
        return f.compose(getVariable(varStr));
    }
    public static void addVariable (String varPath, GuiDataVariable<?, ?> var) {
        String s = varPath.trim();
        MagicalReactors.LOGGER.debug("Attempting to add gui variable at path {}!", varPath);
        if (isValidVarPath(s)) {
            GUI_DATA.addSubValue(s, var);
        } else {
            MagicalReactors.LOGGER.error("Gui data path \"{}\" is invalid!", s);
        }
    }
    private static void addVariable (Pair<String, GuiDataVariable<?, ?>> varPair) {
        addVariable(varPair.t, varPair.u);
    }
    public static void addNamespace (NamespaceBuilder<BlockEntity, ?> nameBuild) {
        nameBuild.build("")
                 .map(GuiDataManager::cleanPath)
                 .forEach(GuiDataManager::addVariable);
    }
    public static boolean isValidVarPath (String varPath) {
        return true;
    }
    public static void addDefaultVariables () {
        addNamespace(new NamespaceCapBuilder<IEnergyStorage>("energy", CapabilityEnergy.ENERGY,
                    () -> null)
                        .addVariable("energyStored", IEnergyStorage::getEnergyStored)
                        .addVariable("maxEnergyStored", IEnergyStorage::getMaxEnergyStored)
                        .addVariable("fractionStored", (IEnergyStorage e) ->
                                (float) e.getEnergyStored() / e.getMaxEnergyStored()));
        addNamespace(new NamespaceCapBuilder<IMachineCapability>("machine", CapabilityMachine.MACHINE_CAPABILITY,
                () -> null)
                        .addVariable("energyToCompletion", IMachineCapability::energyToCompletion)
                        .addVariable("energyRequired", IMachineCapability::energyRequired)
                        .addVariable("fractionComplete", (IMachineCapability m) ->
                                m.energyRequired() == 0 ? 0 : 1.0f - (float) m.energyToCompletion() / m.energyRequired()));
    }

    private static <A> Pair<String, A> cleanPath (Pair<String, A> p) {
        String s = p.t.trim();
        if (s.startsWith(".")) {
            s = s.substring(1);
        }
        p.t = s;
        return p;
    }
    static class Pair <T, U> {
        public T t;
        public U u;
        public Pair (T t, U u) {
            this.t = t;
            this.u = u;
        }
    }
    static class VariableBuilder <T, U> {
        String name;
        Function<T, U> func;
        public VariableBuilder (String name, Function<T, U> func) {
            this.name = name;
            this.func = func;
        }
        Stream<Pair<String, GuiDataVariable<?, ?>>> build (String path) {
            List<Pair<String, GuiDataVariable<?, ?>>> l = new ArrayList<>();
            l.add(new Pair<>(path + "." + name, new GuiDataVariable<>(func)));
            return l.stream();
        }
    }
    public static class NamespaceBuilder <T, U> extends VariableBuilder<T, U> {
        List<VariableBuilder<U, ?>> variables;
        public NamespaceBuilder (String name, Function<T, U> func) {
            super(name, func);
            variables = new ArrayList<>();
        }
        public <V> NamespaceBuilder<T, U> addVariable (String name, Function<U, V> func) {
            variables.add(new VariableBuilder<>(name, func));
            return this;
        }
        public NamespaceBuilder<T, U> addNamespace (NamespaceBuilder<U, ?> namespace) {
            variables.add(namespace);
            return this;
        }

        @Override
        Stream<Pair<String, GuiDataVariable<?, ?>>> build (String path) {
            List<Pair<String, GuiDataVariable<?, ?>>> l = new ArrayList<>();
            l.add(new Pair<>(path + "." + name, new GuiDataNamespace<>(func)));
            return variables.stream()
                            .map((VariableBuilder<U, ?> v) -> v.build(path + "." + name))
                            .reduce(l.stream(), Stream::concat);
        }
    }
    public static class NamespaceCapBuilder <T> extends NamespaceBuilder<BlockEntity, T> {
        public NamespaceCapBuilder (String name, Capability<T> cap, T defaultVal) {
            super(name, (BlockEntity te) -> te.getCapability(cap).orElse(defaultVal));
        }
        public NamespaceCapBuilder (String name, Capability<T> cap, NonNullSupplier<T> defaultSupplier) {
            super(name, (BlockEntity te) -> te.getCapability(cap).orElseGet(defaultSupplier));
        }
    }
}
