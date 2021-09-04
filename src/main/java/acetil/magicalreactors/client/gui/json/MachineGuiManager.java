package acetil.magicalreactors.client.gui.json;

import acetil.magicalreactors.client.gui.ContainerGui;
import acetil.magicalreactors.common.constants.Constants;
import acetil.magicalreactors.common.containers.GuiContainer;
import com.google.gson.Gson;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.utils.FileUtils;
import net.minecraftforge.registries.ForgeRegistries;
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
    private static Map<String, MachineGuiJson> guiMap = new HashMap<>();
    public static int addGuiId (String machine) {
        guiIdMap.put(machine, currentId);
        currentId++;
        return currentId - 1;
    }
    public static int getGuiId (String machine) {
        return guiIdMap.get(machine);
    }
    @OnlyIn(Dist.CLIENT)
    public static void addGui (String machine, MachineGuiJson gui) {
        guiMap.put(machine, gui);
    }
    @OnlyIn(Dist.CLIENT)
    public static void readGuiJson (String location) {
        MagicalReactors.LOGGER.log(Level.INFO, "Started loading of guis at " + location);
        List<MachineGuiJson> guiJsonList = new ArrayList<>();
        Gson gson = new Gson();
        URI uri = FileUtils.getURI(location);
        for (Path p : FileUtils.getPaths(uri)) {
            try {
                guiJsonList.add(gson.fromJson(Files.newBufferedReader(p), MachineGuiJson.class));
            } catch (IOException e) {
                MagicalReactors.LOGGER.log(Level.ERROR, "Failed to load gui file " + p.getFileName());
            }
        }
        FileUtils.closeFileSystem(uri);
        for (MachineGuiJson json : guiJsonList) {
            guiMap.put(json.machine, json);
        }
        MagicalReactors.LOGGER.log(Level.INFO, "Loaded " + guiJsonList.size() + " guis");
    }
    @SuppressWarnings("unchecked")
    public static void registerGuis () {
        readGuiJson("assets/magicalreactors/gui");
        for (String key : guiMap.keySet()) {
            MenuScreens.register((MenuType<GuiContainer>) ForgeRegistries.CONTAINERS.getValue(new ResourceLocation(Constants.MODID, key)),
                    (GuiContainer gui, Inventory inv, Component name) -> new ContainerGui(gui, inv, name, guiMap.get(key)));
        }
    }
}
