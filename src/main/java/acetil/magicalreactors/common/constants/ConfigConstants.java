package acetil.magicalreactors.common.constants;

import acetil.magicalreactors.common.utils.ModifiableSupplier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.HashMap;

public class ConfigConstants {
    public static class Server {
        private HashMap<String, MachineConfig> MACHINE_CONFIGS;
        public Server (ForgeConfigSpec.Builder builder) {
            MACHINE_CONFIGS = new HashMap<>();
        }

        public MachineConfig getMachineConfig (String name) {
            return MACHINE_CONFIGS.get(name);
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
