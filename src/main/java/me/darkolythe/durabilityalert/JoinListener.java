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

public class JoinListener implements Listener {

    private DurabilityAlert plugin = DurabilityAlert.getInstance();

    private FileConfiguration playerDataConfig;
    private File playerData;

    private DurabilityAlert main;
    JoinListener(DurabilityAlert plugin) {
        main = plugin;
    }

    void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        playerData = new File(plugin.getDataFolder(), "PlayerData.yml");

        if (!playerData.exists()) {
            try {
                playerData.createNewFile();
                System.out.println(main.prefix + ChatColor.GREEN + "PlayerData.yml has been created");
            } catch (IOException e) {
                System.out.println(main.prefix + ChatColor.RED + "Could not create PlayerData.yml");
            }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerData);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        playerLoad(event.getPlayer());
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(main, new Runnable() {
            @Override
            public void run() {
                playerSave(event.getPlayer());
            }
        });
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
            if (data.size() < 4) {
                data.add(0);
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
            System.out.println(main.prefix + ChatColor.RED + "Could not save recipes");
        }
    }
}
