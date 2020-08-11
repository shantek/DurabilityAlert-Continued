package me.darkolythe.durabilityalert;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DurabilityAlert extends JavaPlugin {

    private static DurabilityAlert plugin;

    final String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "DurabilityAlert" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";
    public static int displaytime;
    public static int defaultvalue;

    private static Map<Player, List<Integer>> playerData = new HashMap<>();

    DurabilityListener durabilitylistener;
    JoinListener joinlistener;
    ConfigHandler confighandler;

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();

        joinlistener = new JoinListener(plugin);
        durabilitylistener = new DurabilityListener(plugin);
        confighandler = new ConfigHandler(plugin);
        joinlistener.setup();

        getServer().getPluginManager().registerEvents(durabilitylistener, plugin);
        getServer().getPluginManager().registerEvents(joinlistener, plugin);
        getCommand("durabilityalert").setExecutor(new CommandHandler());

        Metrics metrics = new Metrics(plugin);

        joinlistener.onServerStart();

        displaytime = getConfig().getInt("displaytime");
        defaultvalue = getConfig().getInt("defaultvalue");

        System.out.println(prefix + ChatColor.GREEN + "DurabilityAlert enabled!");
    }

    @Override
    public void onDisable() {
        joinlistener.onServerStop();
    }

    public static DurabilityAlert getInstance() {
        return plugin;
    }

    List<Integer> getPlayerData(Player player) {
        if (playerData.containsKey(player)) {
            return playerData.get(player);
        } else {
            List<Integer> data = new ArrayList<>();
            data.add(1);            //toggle
            data.add(defaultvalue); //armour
            data.add(defaultvalue); //tools
            data.add(0);            //type
            playerData.put(player, data);
            return playerData.get(player);
        }
    }

    void setPlayerToggle(Player player) {
        List<Integer> data = getPlayerData(player);
        if (data.get(0) == 0) {
            data.set(0, 1);
        } else {
            data.set(0, 0);
        }
        playerData.put(player, data);
    }

    void setPlayerType(Player player, int type) {
        List<Integer> data = getPlayerData(player);
        data.set(3, type);
        playerData.put(player, data);
    }

    void setPlayerArmour(Player player, int percent) {
        List<Integer> data = getPlayerData(player);
        data.set(1, percent);
        playerData.put(player, data);
    }

    void setPlayerTools(Player player, int percent) {
        List<Integer> data = getPlayerData(player);
        data.set(2, percent);
        playerData.put(player, data);
    }

    void setPlayerData(Player player, List<Integer> data) {
        playerData.put(player, data);
    }
}
