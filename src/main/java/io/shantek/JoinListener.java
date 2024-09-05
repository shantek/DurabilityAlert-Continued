package io.shantek;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class JoinListener implements Listener {

    private final DurabilityAlertContinued plugin = DurabilityAlertContinued.getInstance();

    private FileConfiguration playerDataConfig;
    private File playerData;

    private final DurabilityAlertContinued main;

    public JoinListener(DurabilityAlertContinued plugin) {
        main = plugin;
        setup(); // Ensure setup is called in the constructor, so the file is created at startup
    }

    public void setup() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdir()) {
                plugin.getLogger().severe("Could not create plugin data folder!");
                throw new RuntimeException("Failed to create data folder for DurabilityAlertContinued plugin.");
            }
        }

        playerData = new File(plugin.getDataFolder(), "PlayerData.yml");

        if (!playerData.exists()) {
            try {
                if (playerData.createNewFile()) {
                    plugin.getLogger().info(DurabilityAlertContinued.prefix + ChatColor.GREEN + "PlayerData.yml has been created");
                } else {
                    plugin.getLogger().warning(DurabilityAlertContinued.prefix + ChatColor.YELLOW + "PlayerData.yml already exists");
                }
            } catch (IOException e) {
                plugin.getLogger().severe(DurabilityAlertContinued.prefix + ChatColor.RED + "Could not create PlayerData.yml");
                plugin.getLogger().log(Level.SEVERE, "Exception occurred while creating PlayerData.yml", e);
            }
        }

        playerDataConfig = YamlConfiguration.loadConfiguration(playerData);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Load player data from file (if present)
        playerLoad(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Save player settings to file when they quit
        playerSave(player);
        // Remove player settings from memory
        plugin.removePlayerSettings(player);
    }

    public void onServerStop() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            playerSave(p);
        }
    }

    public void onServerStart() {
        // Ensure player data file is ready when the server starts
        setup();  // Make sure setup is called again on server start to ensure the file is ready
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            playerLoad(p);
        }
    }

    private void playerLoad(Player player) {
        if (playerDataConfig.contains("player." + player.getUniqueId())) {
            String path = "player." + player.getUniqueId();
            boolean warningsEnabled = playerDataConfig.getBoolean(path + ".warningsEnabled", DurabilityAlertContinued.isEnableByDefault());
            int armorThreshold = playerDataConfig.getInt(path + ".armorThreshold", DurabilityAlertContinued.getDefaultValue());
            int toolsThreshold = playerDataConfig.getInt(path + ".toolsThreshold", DurabilityAlertContinued.getDefaultValue());
            PlayerSettings.AlertType alertType = PlayerSettings.AlertType.valueOf(playerDataConfig.getString(path + ".alertType", DurabilityAlertContinued.getDefaultType().name()));
            boolean enchantedItemsOnly = playerDataConfig.getBoolean(path + ".enchantedItemsOnly", DurabilityAlertContinued.isDefaultEnchanted());
            boolean soundEnabled = playerDataConfig.getBoolean(path + ".soundEnabled", true);

            // Create a new PlayerSettings object based on the loaded values
            PlayerSettings settings = new PlayerSettings(warningsEnabled, armorThreshold, toolsThreshold, alertType, enchantedItemsOnly, soundEnabled);
            main.setPlayerData(player, settings);
        }
    }

    void playerSave(Player player) {
        if (playerDataConfig == null) {
            plugin.getLogger().severe("Player data configuration is null! Unable to save player data.");
            return;
        }

        PlayerSettings settings = main.getPlayerSettings(player); // Get the player settings object

        String path = "player." + player.getUniqueId();
        playerDataConfig.set(path + ".warningsEnabled", settings.isWarningsEnabled());
        playerDataConfig.set(path + ".armorThreshold", settings.getArmorThreshold());
        playerDataConfig.set(path + ".toolsThreshold", settings.getToolsThreshold());
        playerDataConfig.set(path + ".alertType", settings.getAlertType().name());
        playerDataConfig.set(path + ".enchantedItemsOnly", settings.isEnchantedItemsOnly());
        playerDataConfig.set(path + ".soundEnabled", settings.isSoundEnabled());

        try {
            playerDataConfig.save(playerData);
        } catch (IOException e) {
            plugin.getLogger().severe(DurabilityAlertContinued.prefix + ChatColor.RED + "Could not save player data.");
            plugin.getLogger().log(Level.SEVERE, "Exception occurred while saving PlayerData.yml", e);
        }
    }
}
