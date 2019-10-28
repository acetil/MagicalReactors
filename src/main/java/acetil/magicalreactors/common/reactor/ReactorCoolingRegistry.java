package acetil.magicalreactors.common.reactor;

import java.util.HashMap;
import java.util.Map;

public class ReactorCoolingRegistry {
    private static Map<String, Integer> coolingMap = new HashMap<>();
    public static void registerCooling (String registryName, int cooling) {
        coolingMap.put(registryName, cooling);
    }
    public static int getCooling (String registryName) {
        return coolingMap.get(registryName);
    }
}
