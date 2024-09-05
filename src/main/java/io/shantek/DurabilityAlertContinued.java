package io.shantek;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class DurabilityAlertContinued extends JavaPlugin {

    private static DurabilityAlertContinued plugin;

    // Default settings
    private static boolean enableByDefault;
    private static int defaultValue;
    private static PlayerSettings.AlertType defaultType;
    private static boolean defaultEnchanted;
    private static int displayTime;

    // Prefix used in messages
    public static String prefix = "&f&l[&9&lDurabilityAlert&f&l] ";

    // Map to store player settings
    private final Map<UUID, PlayerSettings> playerData = new HashMap<>();

    // Listeners
    JoinListener joinListener;
    ConfigHandler configHandler;

    @Override
    public void onEnable() {
        plugin = this;

        // Load default config if not present
        saveDefaultConfig();

        enableByDefault = getConfig().getBoolean("enabled-by-default", false);
        defaultValue = getConfig().getInt("defaultvalue", 10);
        defaultType = PlayerSettings.AlertType.valueOf(getConfig().getString("defaulttype", "PERCENT").toUpperCase());
        defaultEnchanted = getConfig().getBoolean("defaultenchanted", false);
        displayTime = getConfig().getInt("displaytime", 10); // Set default if not in config

        // Initialize listeners and config handler
        joinListener = new JoinListener(this);
        configHandler = new ConfigHandler(this);

        // Register events
        getServer().getPluginManager().registerEvents(joinListener, this);
        // Register the DurabilityListener
        getServer().getPluginManager().registerEvents(new DurabilityListener(this), this);

        // Register the tab completer
        Objects.requireNonNull(this.getCommand("durabilityalert")).setTabCompleter(new DurabilityTabCompleter());

        // Register commands
        Objects.requireNonNull(this.getCommand("durabilityalert")).setExecutor(new CommandHandler());

        // Perform any additional setup when the server starts
        joinListener.onServerStart();
    }

    @Override
    public void onDisable() {
        // Perform any cleanup tasks when the server stops
        joinListener.onServerStop();
    }

    public static DurabilityAlertContinued getInstance() {
        return plugin;
    }

    // Method to retrieve player settings or create default settings if not present
    public PlayerSettings getPlayerSettings(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerSettings());
    }

    // Method to store player settings
    public void setPlayerData(Player player, PlayerSettings settings) {
        playerData.put(player.getUniqueId(), settings);
    }

    // Method to remove player settings from the playerData map when they quit
    public void removePlayerSettings(Player player) {
        playerData.remove(player.getUniqueId());
    }

    // Method to set the alert type for a player (e.g., percent or durability)
    public void setPlayerAlertType(Player player, PlayerSettings.AlertType alertType) {
        PlayerSettings settings = getPlayerSettings(player);
        settings.setAlertType(alertType);
        setPlayerData(player, settings);
    }

    // Method to set the armor threshold for a player
    public void setPlayerArmorThreshold(Player player, int threshold) {
        PlayerSettings settings = getPlayerSettings(player);
        settings.setArmorThreshold(threshold);
        setPlayerData(player, settings);
    }

    // Method to set the tools threshold for a player
    public void setPlayerToolsThreshold(Player player, int threshold) {
        PlayerSettings settings = getPlayerSettings(player);
        settings.setToolsThreshold(threshold);
        setPlayerData(player, settings);
    }

    // Toggle specific player settings (warnings enabled, enchanted items only, sound enabled)
    public void togglePlayerSetting(Player player, Setting setting) {
        PlayerSettings settings = getPlayerSettings(player);

        switch (setting) {
            case WARNINGS_ENABLED:
                settings.setWarningsEnabled(!settings.isWarningsEnabled());
                break;
            case ENCHANTED_ITEMS_ONLY:
                settings.setEnchantedItemsOnly(!settings.isEnchantedItemsOnly());
                break;
            case SOUND_ENABLED:
                settings.setSoundEnabled(!settings.isSoundEnabled());
                break;
        }

        setPlayerData(player, settings);
    }

    // Enum to represent different toggleable settings
    public enum Setting {
        WARNINGS_ENABLED,
        ENCHANTED_ITEMS_ONLY,
        SOUND_ENABLED
    }

    // Getter for default settings
    public static boolean isEnableByDefault() {
        return enableByDefault;
    }

    public static int getDefaultValue() {
        return defaultValue;
    }

    public static PlayerSettings.AlertType getDefaultType() {
        return defaultType;
    }

    public static boolean isDefaultEnchanted() {
        return defaultEnchanted;
    }

    public static int getDisplayTime() {
        return displayTime;
    }
}
