package io.shantek;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {

    private final DurabilityAlert plugin;
    public String invalidArguments;
    public String warningsEnabled;
    public String warningsDisabled;
    public String enchantedTrue;
    public String enchantedFalse;
    public String setType;
    public String armourSet;
    public String toolSet;
    public String mustBeNumber;
    public String lowDurability;
    public String durabilityLeft;
    public String soundEnabled;
    public String soundDisabled;

    public ConfigHandler(DurabilityAlert plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();

        // Load messages with defaults
        invalidArguments = config.getString("invalidarguments", "Invalid Arguments");
        warningsEnabled = config.getString("warningsenabled", "Warnings enabled.");
        warningsDisabled = config.getString("warningsdisabled", "Warnings disabled.");
        enchantedTrue = config.getString("enchantedtrue", "Alerts will only show for enchanted items.");
        enchantedFalse = config.getString("enchantedfalse", "Alerts will show for all items.");
        setType = config.getString("settype", "Warning type has been set to %type%.");
        armourSet = config.getString("armourset", "Armor warning value set to %armour%.");
        toolSet = config.getString("toolset", "Tools warning value set to %tool%.");
        mustBeNumber = config.getString("mustbenumber", "Percent must be a number! ex: 10");
        lowDurability = config.getString("lowdurability", "Durability Warning");
        durabilityLeft = config.getString("durabilityleft", "%durability% Remaining");
        soundEnabled = config.getString("soundenabled", "Durability alert sound enabled.");
        soundDisabled = config.getString("sounddisabled", "Durability alert sound disabled.");
    }
}
