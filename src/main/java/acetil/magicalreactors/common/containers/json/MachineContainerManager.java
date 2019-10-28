package acetil.magicalreactors.common.containers.json;

import com.google.gson.Gson;
import acetil.magicalreactors.common.NuclearMod;
import acetil.magicalreactors.common.utils.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineContainerManager {
    private static Map<String, MachineContainerJson> registry = new HashMap<>();

    public static void registerContainerJson (String key, MachineContainerJson containerJson) {
        registry.put(key, containerJson);
    }
    public static MachineContainerJson getContainerJson (String key) {
        return registry.get(key);
    }
    public static void readContainers (String location) {
        URI uri = FileUtils.getURI(location);
        Gson gson = new Gson();
        List<Path> paths = FileUtils.getPaths(uri);
        for (Path p : paths) {
            try {
                MachineContainerJson containerJson = gson.fromJson(Files.newBufferedReader(p), MachineContainerJson.class);
                registry.put(containerJson.name, containerJson);
            } catch (IOException e) {
                NuclearMod.logger.log(Level.ERROR,"Error reading file " + p.getFileName());
            }
        }
        FileUtils.closeFileSystem(uri);
        NuclearMod.logger.log(Level.INFO, "Finished loading " + registry.size() + " containers");
    }
}
