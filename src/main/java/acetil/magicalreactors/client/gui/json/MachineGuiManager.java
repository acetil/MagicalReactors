package acetil.magicalreactors.client.gui.json;

import com.google.gson.Gson;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import acetil.magicalreactors.common.NuclearMod;
import acetil.magicalreactors.common.utils.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineGuiManager {
    private static Map<String, Integer> guiIdMap = new HashMap<>();
    private static int currentId = 3;
    private static Map<Integer, MachineGuiJson> guiMap = new HashMap<>();
    public static int addGuiId (String machine) {
        guiIdMap.put(machine, currentId);
        currentId++;
        return currentId - 1;
    }
    public static int getGuiId (String machine) {
        return guiIdMap.get(machine);
    }
    @SideOnly(Side.CLIENT)
    public static MachineGuiJson getGui (int id) {
        return guiMap.get(id);
    }
    @SideOnly(Side.CLIENT)
    public static void addGui (String machine, MachineGuiJson gui) {
        guiMap.put(guiIdMap.get(machine), gui);
    }
    @SideOnly(Side.CLIENT)
    public static void readGuiJson (String location) {
        NuclearMod.logger.log(Level.INFO, "Started loading of guis at " + location);
        List<MachineGuiJson> guiJsonList = new ArrayList<>();
        Gson gson = new Gson();
        URI uri = FileUtils.getURI(location);
        for (Path p : FileUtils.getPaths(uri)) {
            try {
                guiJsonList.add(gson.fromJson(Files.newBufferedReader(p), MachineGuiJson.class));
            } catch (IOException e) {
                NuclearMod.logger.log(Level.ERROR, "Failed to load gui file " + p.getFileName());
            }
        }
        FileUtils.closeFileSystem(uri);
        for (MachineGuiJson json : guiJsonList) {
            if (guiIdMap.containsKey(json.machine)) {
                guiMap.put(guiIdMap.get(json.machine), json);
            }
        }
        NuclearMod.logger.log(Level.INFO, "Loaded " + guiJsonList.size() + " guis");
    }
}
