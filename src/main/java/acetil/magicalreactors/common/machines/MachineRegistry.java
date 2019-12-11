package acetil.magicalreactors.common.machines;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MachineRegistry {
    private static HashMap<String, MachineRegistryItem> machineRegistry = new HashMap<>();
    public static void registerMachine (MachineRegistryItem registryItem) {
        machineRegistry.put(registryItem.machine, registryItem);
    }
    public static MachineRegistryItem getMachine (String machine) {
        return machineRegistry.get(machine);
    }
    public static List<MachineRegistryItem> getMachines () {
        return machineRegistry.keySet()
                              .stream()
                              .map(machineRegistry::get)
                              .collect(Collectors.toList());
    }
}
