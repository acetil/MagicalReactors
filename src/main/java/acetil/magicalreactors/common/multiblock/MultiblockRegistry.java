package acetil.magicalreactors.common.multiblock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiblockRegistry {
    private static Map<String, IMultiblock> multiblockMap = new HashMap<>();
    public static void registerMultiblock (String name, IMultiblock multiblock) {
        multiblockMap.put(name, multiblock);
    }
    public static IMultiblock getMultiblock (String name) {
        return multiblockMap.get(name);
    }
    public static List<IMultiblock> getMultiblocks (String type) {
        return multiblockMap.values()
                            .stream()
                            .filter((IMultiblock m) -> m.getType().equals(type))
                            .collect(Collectors.toList());
    }
}
