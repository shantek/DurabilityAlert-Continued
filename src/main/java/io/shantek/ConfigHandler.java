package io.shantek;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {

    private final DurabilityAlertContinued plugin;
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
    public String prefix;
    public float soundVolume;

    public ConfigHandler(DurabilityAlertContinued plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    // Helper to parse color codes
    private String parse(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public void loadConfig() {
        FileConfiguration config = plugin.getConfig();

        // Load and translate the prefix to Minecraft color codes
        prefix = parse(config.getString("prefix", "&f&l[&3Durability&bAlert&f&l] ")) + ChatColor.RESET;

        // Load messages with defaults and parse colors
        invalidArguments = parse(config.getString("invalidarguments", "Invalid arguments"));
        warningsEnabled = parse(config.getString("warningsenabled", "Durability warnings are now &2enabled"));
        warningsDisabled = parse(config.getString("warningsdisabled", "Durability warnings are now &cdisabled"));
        enchantedTrue = parse(config.getString("enchantedtrue", "Alerts will only show for enchanted items"));
        enchantedFalse = parse(config.getString("enchantedfalse", "Alerts will show for all items"));
        setType = parse(config.getString("settype", "Warning type has been set to %type%"));
        armourSet = parse(config.getString("armourset", "Armor warning value set to %armour%"));
        toolSet = parse(config.getString("toolset", "Tools warning value set to %tool%"));
        mustBeNumber = parse(config.getString("mustbenumber", "Percent must be a number! ex: 10"));
        lowDurability = parse(config.getString("lowdurability", "Durability Warning"));
        durabilityLeft = parse(config.getString("durabilityleft", "%durability% Remaining"));
        soundEnabled = parse(config.getString("soundenabled", "Alert sounds are now &2enabled"));
        soundDisabled = parse(config.getString("sounddisabled", "Alert sounds are now &cdisabled"));


        // Load the volume setting with default 1.0
        soundVolume = (float) config.getDouble("soundvolume", 0.7);
    }
}
