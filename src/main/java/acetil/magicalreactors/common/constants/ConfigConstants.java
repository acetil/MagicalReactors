package acetil.magicalreactors.common.constants;

import acetil.magicalreactors.common.machines.MachineRegistry;
import acetil.magicalreactors.common.machines.MachineRegistryItem;
import acetil.magicalreactors.common.utils.ModifiableSupplier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;

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
        public Builder addMachineConfig (MachineConfig config, String comment) {
            this.addCategory(config.NAME, comment);
            this.add("energyStorage", config.energyStorageModifiable, config.defaultEnergyStorage,
                    "Change this to modify the energy capacity of the machine", false, true);
            this.add("maxReceive", config.maxReceiveModifiable, config.defaultMaxReceive,
                    "Change this to modify the maximum amount of energy a machine can receive", false, true);
            this.add("energyUseRate", config.energyUseRateModifiable, config.defaultEnergyUseRate,
                    "Change this to modify the amount of energy used per tick (more means the machine works faster)",
                    false, true);
            this.exitCategory();
            return this;
        }
        public Builder addMachineConfig (MachineConfig config) {
            return addMachineConfig(config, null);
        }
        public Builder addCategory (String categoryName, String comment) {
            if (comment != null) {
                builder.comment(comment).push(categoryName);
            } else {
                builder.push(categoryName);
            }
            return this;
        }
        public Builder addCategory (String categoryName) {
            return addCategory(categoryName, null);
        }
        public Builder exitCategory () {
            builder.pop();
            return this;
        }
        public <T> Builder add (String categoryName, ModifiableSupplier<T> modSupplier, T defaultVal, String comment, boolean requiresRestart, boolean hasTranslation) {
            if (comment != null) {
                builder.comment(comment);
            }
            if (requiresRestart) {
                builder.worldRestart();
            }
            if (hasTranslation) {
                builder.translation(Constants.MODID + ".configgui." + categoryName);
            }
            modSupplier.setSupplier(builder.define(categoryName, defaultVal)::get);
            return this;
        }
    }
}
