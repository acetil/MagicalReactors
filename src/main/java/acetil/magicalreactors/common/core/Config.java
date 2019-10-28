package acetil.magicalreactors.common.core;

import acetil.magicalreactors.common.items.reactor.ReactorItems;
import net.minecraftforge.common.config.Configuration;
import acetil.magicalreactors.common.MagicalReactors;

public class Config {
    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_REACTOR_ITEMS = "reactoritems";
    public static void readGeneralConfig (Configuration config) {
        try  {
            config.load();
            initGeneralConfigs(config);
        } catch (Exception e) {
            MagicalReactors.LOGGER.error("Error loading general config file: " + e);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
    public static void initGeneralConfigs (Configuration config) {
        config.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
    }
    public static void readReactorItemConfigs (Configuration config) {
        try  {
            config.load();
            ReactorItems.readConfigs(config);
        } catch (Exception e) {
            MagicalReactors.LOGGER.error("Error loading reactor items config file: " + e);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}
