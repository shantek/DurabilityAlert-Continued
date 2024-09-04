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

    public ConfigHandler(DurabilityAlert plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();

        // Load messages with defaults
        invalidArguments = config.getString("messages.invalid-arguments", "Invalid arguments");
        warningsEnabled = config.getString("messages.warnings-enabled", "Warnings enabled.");
        warningsDisabled = config.getString("messages.warnings-disabled", "Warnings disabled.");
        enchantedTrue = config.getString("messages.enchanted-true", "Alerts for enchanted items only enabled.");
        enchantedFalse = config.getString("messages.enchanted-false", "Alerts for enchanted items only disabled.");
        setType = config.getString("messages.set-type", "Alert type set to %type%.");
        armourSet = config.getString("messages.armour-set", "Armor threshold set to %armour%.");
        toolSet = config.getString("messages.tool-set", "Tools threshold set to %tool%.");
        mustBeNumber = config.getString("messages.must-be-number", "The value must be a number.");
        lowDurability = config.getString("messages.low-durability", "Low durability on %item%");
        durabilityLeft = config.getString("messages.durability-left", "Durability left: %durability%");
    }
}
