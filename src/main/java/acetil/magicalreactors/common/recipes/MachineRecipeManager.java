package acetil.magicalreactors.common.recipes;

import com.google.gson.Gson;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.utils.FileUtils;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/* TODO: look back at */
public class MachineRecipeManager {
    protected static List<MachineRecipe> machineRecipes = new LinkedList<>();
    public static List<MachineRecipe> getRecipes () {
        return machineRecipes;
    }
    public static List<MachineRecipe> getRecipes (String machine) {
        return machineRecipes.stream().filter(p -> p.getMachine().equals(machine)).collect(Collectors.toList());
    }
    public static MachineRecipe getRecipe (List<MachineRecipeInput> inputs, String machine) {
        List<MachineRecipe> relevantRecipes = getRecipes(machine);
        MachineRecipe recipe = null;
        for (MachineRecipe m : relevantRecipes) {
            if (m.matches(inputs)) {
                recipe = m;
                break;
            }
        }
        return recipe;
    }
    public static void readRecipes (String location) {
        MagicalReactors.LOGGER.info("Started loading of recipes at " + location);
        List<MachineRecipeJson> jsonRecipes = new LinkedList<>();
        URI uri = FileUtils.getURI(location);
        Gson gson = new Gson();
        List<Path> paths;
        paths = FileUtils.getPaths(uri);
        for (Path p : paths) {
            try {
                jsonRecipes.add(gson.fromJson(Files.newBufferedReader(p), MachineRecipeJson.class));
            } catch (IOException e) {
                MagicalReactors.LOGGER.error("Error reading recipe file " + p.getFileName());
            }
        }
        FileUtils.closeFileSystem(uri);
        machineRecipes.addAll(jsonRecipes.stream()
                .map(MachineRecipe::new)
                .collect(Collectors.toList()));
        MagicalReactors.LOGGER.info("Completed loading of " + machineRecipes.size() + " recipes.");
    }
}
