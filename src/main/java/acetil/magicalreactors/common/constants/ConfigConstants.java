package acetil.magicalreactors.common.constants;

import acetil.magicalreactors.common.machines.MachineRegistry;
import acetil.magicalreactors.common.machines.MachineRegistryItem;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

public class ConfigConstants {
    public static ForgeConfigSpec SERVER_SPEC;
    public static Server SERVER;
    public static class Server {
        public Server (ForgeConfigSpec.Builder forgeBuilder) {
            Builder builder = new Builder(forgeBuilder, "Magical Reactors serverside config.", "magicalreactors");
            builder.addCategory("general", "General configuration values");
            builder.exitCategory();
            builder.addCategory("machines", "Configs for all the machines");
            for (MachineRegistryItem m : MachineRegistry.getMachines()) {
                builder.addMachineConfig(m.config);
            }
            builder.exitCategory();
        }
        public static void bakeConfigs () {
            Pair<Server, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder()
                    .configure(Server::new);
            SERVER = pair.getLeft();
            SERVER_SPEC = pair.getRight();
        }
    }
    public static class Builder {
        ForgeConfigSpec.Builder builder;
        public Builder (ForgeConfigSpec.Builder builder, String comment, String path) {
            this.builder = builder;
            builder.comment(comment).push(path);
        }
        public void addMachineConfig (MachineConfig config, String comment) {
            this.addCategory(config.NAME, comment);
            config.ENERGY_STORAGE =  this.add("energyStorage", config.defaultEnergyStorage,
                    "Change this to modify the energy capacity of the machine", false, true);
            config.MAX_RECEIVE=  this.add("maxReceive", config.defaultMaxReceive,
                    "Change this to modify the maximum amount of energy a machine can receive", false, true);
            config.ENERGY_USE_RATE =  this.add("energyUseRate", config.defaultEnergyUseRate,
                    "Change this to modify the amount of energy used per tick (more means the machine works faster)",
                    false, true);
            this.exitCategory();
        }
        public void addMachineConfig (MachineConfig config) {
            addMachineConfig(config, null);
        }
        public void addCategory (String categoryName, String comment) {
            if (comment != null) {
                builder.comment(comment).push(categoryName);
            } else {
                builder.push(categoryName);
            }
        }
        public void addCategory (String categoryName) {
            addCategory(categoryName, null);
        }
        public void exitCategory () {
            builder.pop();
        }
        public <T> Supplier<T> add (String categoryName, T defaultVal, String comment, boolean requiresRestart, boolean hasTranslation) {
            if (comment != null) {
                builder.comment(comment);
            }
            if (requiresRestart) {
                builder.worldRestart();
            }
            if (hasTranslation) {
                builder.translation(Constants.MODID + ".configgui." + categoryName);
            }
            return builder.define(categoryName, defaultVal)::get;
        }
    }
}
