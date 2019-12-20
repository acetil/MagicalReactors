package acetil.magicalreactors.common.containers.json;

import acetil.magicalreactors.common.containers.GuiContainer;
import acetil.magicalreactors.common.containers.GuiContainerFactory;
import acetil.magicalreactors.common.constants.Constants;
import com.google.gson.Gson;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.utils.FileUtils;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
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
import java.util.stream.Collectors;

public class MachineContainerManager {
    private static Map<String, MachineContainerJson> registry = new HashMap<>();
    public static DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Constants.MODID);
    public static List<RegistryObject<ContainerType<GuiContainer>>> CONTAINER_LIST = new ArrayList<>();
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
                MagicalReactors.LOGGER.log(Level.ERROR,"Error reading file " + p.getFileName());
            }
        }
        FileUtils.closeFileSystem(uri);
        MagicalReactors.LOGGER.log(Level.INFO, "Finished loading " + registry.size() + " containers");
    }
    public static void registerContainers () {
        MagicalReactors.LOGGER.info("Starting registering containers!");
        readContainers("assets/magicalreactors/containers");
        /*for (String key : registry.keySet()) {
            event.getRegistry().register(IForgeContainerType.create(new GuiContainerFactory(registry.get(key))).setRegistryName(key));
        }*/
        CONTAINER_LIST = registry.keySet().stream().map((String s) -> CONTAINERS.register(s,
                () -> IForgeContainerType.create(new GuiContainerFactory(registry.get(s))))).collect(Collectors.toList());
    }
}
