package acetil.magicalreactors.common.multiblock;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.utils.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiblockLoader {
    public static void loadMultiblock (Path p) {
        // TODO: refactor all gson code into one function
        Gson gson = new Gson();
        try (BufferedReader b = Files.newBufferedReader(p)) {
            MultiblockJson json = gson.fromJson(b, MultiblockJson.class);
            MultiblockImpl impl = new MultiblockImpl(json);
            if (impl.complete) {
                MultiblockRegistry.registerMultiblock(json.name, impl);
            }
        } catch (IOException e) {
            MagicalReactors.LOGGER.log(Level.WARN,  "Failed to load \"" + p.getFileName().toString() + "\".");
        }
    }
    public static void loadMultiblocks (String location) {
        MagicalReactors.LOGGER.log(Level.INFO, "Loading multiblocks.");
        List<Path> paths = FileUtils.getPaths(FileUtils.getURI(location));
        for (Path p : paths) {
            if (p.getFileName().toString().endsWith(".json")) {
                loadMultiblock(p);
            }
        }
        MagicalReactors.LOGGER.log(Level.INFO, "Finished loading multiblocks");
    }
    public static class MultiblockJson {
        // Idea stolen from Vaskii's Patchouli mod
        // https://github.com/Vazkii/Patchouli
        // TODO: update to not steal from Vaskii
        public String name = "";
        public String type = "default";
        @SerializedName("key")
        public Map<String, String> keyMap = new HashMap<>();
        public String[][] multiblock;
        @SerializedName("filled_air")
        public boolean allowsFilledAir = false;
    }

}
