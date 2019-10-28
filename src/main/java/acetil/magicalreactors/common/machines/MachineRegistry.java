package acetil.magicalreactors.common.machines;

import java.util.HashMap;

public class MachineRegistry {
    private static HashMap<String, MachineRegistryItem> machineRegistry = new HashMap<>();
    public static void registerMachine (MachineRegistryItem registryItem) {
        machineRegistry.put(registryItem.machine, registryItem);
    }
    public static MachineRegistryItem getMachine (String machine) {
        return machineRegistry.get(machine);
    }
}
