package me.darkolythe.durabilityalert;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class DurabilityAlert extends JavaPlugin {

    private static DurabilityAlert plugin;

    public static String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD + "[" + ChatColor.BLUE + "DurabilityAlert" + ChatColor.WHITE + ChatColor.BOLD + "] ";
    public static int displaytime;
    public static int defaultvalue;
    public static String defaulttype;
    public static boolean defaultenchanted;
    public static boolean enableByDefault = false;

    private static final Map<Player, List<Integer>> playerData = new HashMap<>();

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

        // register events
        getServer().getPluginManager().registerEvents(durabilitylistener, plugin);
        getServer().getPluginManager().registerEvents(joinlistener, plugin);


        // register commands
        Objects.requireNonNull(this.getCommand("durabilityalert")).setExecutor(new CommandHandler());

        // register auto-complete
        Objects.requireNonNull(this.getCommand("durabilityalert")).setTabCompleter(new PluginTabCompleter("durabilityalert"));

        //set tab completion list
        ((PluginTabCompleter) Objects.requireNonNull(Objects.requireNonNull(this.getCommand("durabilityalert")).getTabCompleter())).setTabCompletions(
            new PluginTabCompleter.TablistGroup[] {
                new PluginTabCompleter.TablistGroup(new ArrayList<>(List.of("toggle")), "durabilityalert.command"),
                new PluginTabCompleter.TablistGroup(new ArrayList<>(Arrays.asList("armour", "<number>")), "durabilityalert.command"),
                new PluginTabCompleter.TablistGroup(new ArrayList<>(Arrays.asList("tools", "<number>")), "durabilityalert.command"),
                new PluginTabCompleter.TablistGroup(new ArrayList<>(Arrays.asList("type", "percent")), "durabilityalert.command"),
                new PluginTabCompleter.TablistGroup(new ArrayList<>(Arrays.asList("type", "durability")), "durabilityalert.command"),
                new PluginTabCompleter.TablistGroup(new ArrayList<>(List.of("enchant")), "durabilityalert.command"),
                new PluginTabCompleter.TablistGroup(new ArrayList<>(List.of("status")), "durabilityalert.command")
            }
        );

        joinlistener.onServerStart();

        displaytime = getConfig().getInt("displaytime");
        defaultvalue = getConfig().getInt("defaultvalue");
        defaulttype = getConfig().getString("defaulttype");
        defaultenchanted = getConfig().getBoolean("defaultenchanted");
        enableByDefault = getConfig().getBoolean("enabled-by-default");

        prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("prefix")));

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
        List<Integer> defaults = new ArrayList<>();
        defaults.add(enableByDefault ? 1 : 0); // toggle
        defaults.add(defaultvalue); // armour
        defaults.add(defaultvalue); // tools
        defaults.add(defaulttype.equals("percent") ? 0 : 1); // type
        defaults.add(defaultenchanted ? 1 : 0); // alert on enchanted only
        defaults.add(1); // sound enabled by default

        if (playerData.containsKey(player)) {
            if (playerData.get(player).size() < 6) { // Update to include sound setting
                List<Integer> data = new ArrayList<>(playerData.get(player));
                while (data.size() < 6) {
                    data.add(1); // Add default sound setting if missing
                }
                playerData.put(player, data);
            }
            return playerData.get(player);
        } else {
            playerData.put(player, defaults); // Initialize player data
            return playerData.get(player);
        }
    }

    /*
    Data index 0 is warning toggle
    Data index 4 is enchant toggle
     */

    // Add a method to toggle sound
    void setPlayerSound(Player player) {
        List<Integer> data = getPlayerData(player);
        data.set(5, data.get(5) == 0 ? 1 : 0); // Toggle sound setting
        playerData.put(player, data);
    }

    void setPlayerToggle(Player player, int dataIndex) {
        List<Integer> data = getPlayerData(player);
        if (data.get(dataIndex) == 0) {
            data.set(dataIndex, 1);
        } else {
            data.set(dataIndex, 0);
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

    void removePlayerData(Player player) {
        playerData.remove(player);
    }

    void setPlayerData(Player player, List<Integer> data) {
        playerData.put(player, data);
    }
}
