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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class JoinListener implements Listener {

    private final DurabilityAlert plugin = DurabilityAlert.getInstance();

    private FileConfiguration playerDataConfig;
    private File playerData;

    private final DurabilityAlert main;

    public JoinListener(DurabilityAlert plugin) {
        main = plugin;
    }

    public void setup() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdir()) {
            plugin.getLogger().severe("Could not create plugin data folder!");
            throw new RuntimeException("Failed to create data folder for DurabilityAlert plugin.");
        }

        playerData = new File(plugin.getDataFolder(), "PlayerData.yml");

        if (!playerData.exists()) {
            try {
                if (playerData.createNewFile()) {
                    plugin.getLogger().info(DurabilityAlert.prefix + ChatColor.GREEN + "PlayerData.yml has been created");
                } else {
                    plugin.getLogger().warning(DurabilityAlert.prefix + ChatColor.YELLOW + "PlayerData.yml already exists");
                }
            } catch (IOException e) {
                plugin.getLogger().severe(DurabilityAlert.prefix + ChatColor.RED + "Could not create PlayerData.yml");
                plugin.getLogger().log(Level.SEVERE, "Exception occurred while creating PlayerData.yml", e);
            }
        } else {
            plugin.getLogger().warning(DurabilityAlert.prefix + ChatColor.YELLOW + "PlayerData.yml already exists");
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
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            playerLoad(p);
        }
    }

    private void playerLoad(Player player) {
        if (playerDataConfig.contains("player." + player.getUniqueId())) {
            String path = "player." + player.getUniqueId();
            boolean warningsEnabled = playerDataConfig.getBoolean(path + ".warningsEnabled", DurabilityAlert.isEnableByDefault());
            int armorThreshold = playerDataConfig.getInt(path + ".armorThreshold", DurabilityAlert.getDefaultValue());
            int toolsThreshold = playerDataConfig.getInt(path + ".toolsThreshold", DurabilityAlert.getDefaultValue());
            PlayerSettings.AlertType alertType = PlayerSettings.AlertType.valueOf(playerDataConfig.getString(path + ".alertType", DurabilityAlert.getDefaultType().name()));
            boolean enchantedItemsOnly = playerDataConfig.getBoolean(path + ".enchantedItemsOnly", DurabilityAlert.isDefaultEnchanted());
            boolean soundEnabled = playerDataConfig.getBoolean(path + ".soundEnabled", true);

            // Create a new PlayerSettings object based on the loaded values
            PlayerSettings settings = new PlayerSettings(warningsEnabled, armorThreshold, toolsThreshold, alertType, enchantedItemsOnly, soundEnabled);
            main.setPlayerData(player, settings);
        }
    }

    void playerSave(Player player) {
        PlayerSettings settings = main.getPlayerSettings(player); // Get the player settings object

        String path = "player." + player.getUniqueId();
        playerDataConfig.set(path + ".warningsEnabled", settings.isWarningsEnabled());
        playerDataConfig.set(path + ".armorThreshold", settings.getArmorThreshold());
        playerDataConfig.set(path + ".toolsThreshold", settings.getToolsThreshold());
        playerDataConfig.set(path + ".alertType", settings.getAlertType().name());
        playerDataConfig.set(path + ".enchantedItemsOnly", settings.isEnchantedItemsOnly());
        playerDataConfig.set(path + ".soundEnabled", settings.isSoundEnabled());

        try {
            // Use try-with-resources to handle file saving more securely
            File tempFile = new File(plugin.getDataFolder(), "PlayerData_temp.yml");
            playerDataConfig.save(tempFile);
            Files.move(tempFile.toPath(), playerData.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            plugin.getLogger().severe(DurabilityAlert.prefix + ChatColor.RED + "Could not save player data.");
            plugin.getLogger().log(Level.SEVERE, "Exception occurred while saving PlayerData.yml", e);
        }
    }
}
