package me.darkolythe.durabilityalert;

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
import java.util.List;
import java.util.logging.Level;

public class JoinListener implements Listener {

    private final DurabilityAlert plugin = DurabilityAlert.getInstance();

    private FileConfiguration playerDataConfig;
    private File playerData;

    private final DurabilityAlert main;
    JoinListener(DurabilityAlert plugin) {
        main = plugin;
    }

    void setup() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdir()) {
                // Handle the error: log it, throw an exception, etc.
                plugin.getLogger().severe("Could not create plugin data folder!");
                // Optionally, throw an exception if this is a critical error
                throw new RuntimeException("Failed to create data folder for DurabilityAlert plugin.");
            }
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
    private void onPlayerJoin(PlayerJoinEvent event) {
        playerLoad(event.getPlayer());
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        playerSave(event.getPlayer());
        main.removePlayerData(event.getPlayer());
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
            List<Integer> data = playerDataConfig.getIntegerList("player." + player.getUniqueId());
            while (data.size() < 6) { // Ensure we load the sound setting
                data.add(1); // Default to sound enabled if missing
            }
            main.setPlayerData(player, data);
        }
    }

    void playerSave(Player player) {
        playerDataConfig = YamlConfiguration.loadConfiguration(playerData);

        String path = "player." + player.getUniqueId();
        playerDataConfig.set(path, main.getPlayerData(player));

        try {
            playerDataConfig.save(playerData);
        } catch (IOException e) {
            System.out.println(DurabilityAlert.prefix + ChatColor.RED + "Could not save player data.");
        }
    }
}
