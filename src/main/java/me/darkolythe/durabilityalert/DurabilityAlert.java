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

    public static String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "DurabilityAlert" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";
    public static int displaytime;
    public static int defaultvalue;
    public static String defaulttype;
    public static boolean defaultenchanted;
    public static boolean enableByDefault = false;

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

        // register events
        getServer().getPluginManager().registerEvents(durabilitylistener, plugin);
        getServer().getPluginManager().registerEvents(joinlistener, plugin);
        
        
        // register commands
        this.getCommand("durabilityalert").setExecutor(new CommandHandler());
        
        // register auto-complete
        this.getCommand("durabilityalert").setTabCompleter(new PluginTabCompleter("durabilityalert"));
        
        //set tab completion list
		((PluginTabCompleter)this.getCommand("durabilityalert").getTabCompleter()).setTabCompletions(
			new TablistGroup[] {
                new TablistGroup(new ArrayList<String>(Arrays.asList("toggle")), "durabilityalert.command"),
                new TablistGroup(new ArrayList<String>(Arrays.asList("armour", "<number>")), "durabilityalert.command"),
                new TablistGroup(new ArrayList<String>(Arrays.asList("tools", "<number>")), "durabilityalert.command"),
                new TablistGroup(new ArrayList<String>(Arrays.asList("type", "percent")), "durabilityalert.command"),
                new TablistGroup(new ArrayList<String>(Arrays.asList("type", "durability")), "durabilityalert.command"),
                new TablistGroup(new ArrayList<String>(Arrays.asList("enchant")), "durabilityalert.command"),
                new TablistGroup(new ArrayList<String>(Arrays.asList("status")), "durabilityalert.command")
			}
		);

        Metrics metrics = new Metrics(plugin);

        joinlistener.onServerStart();

        displaytime = getConfig().getInt("displaytime");
        defaultvalue = getConfig().getInt("defaultvalue");
        defaulttype = getConfig().getString("defaulttype");
        defaultenchanted = getConfig().getBoolean("defaultenchanted");
        enableByDefault = getConfig().getBoolean("enabled-by-default");

        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));

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
        defaults.add(enableByDefault ? 1 : 0); //toggle
        defaults.add(defaultvalue); //armour
        defaults.add(defaultvalue); //tools
        defaults.add(defaulttype.equals("percent") ? 0 : 1); //type
        defaults.add(defaultenchanted ? 1 : 0);              //alert on enchanted only

        if (playerData.containsKey(player)) {
            if (playerData.get(player).size() < 5) { // if the player data does not contain all required data points, add the missing ones
                int index = 0;
                List<Integer> data = new ArrayList<>();
                for (int i = index; i < playerData.get(player).size(); i++) {
                    data.add(playerData.get(player).get(i));
                    index++;
                }

                for (int i = index; i < 5; i++) {
                    data.add(defaults.get(i));
                }
                playerData.put(player, defaults);
            }
            return playerData.get(player);
        } else {
            playerData.put(player, defaults); // if the player data does not exist, initialize it
            return playerData.get(player);
        }
    }

    /*
    Data index 0 is warning toggle
    Data index 4 is enchant toggle
     */
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

    void setPlayerData(Player player, List<Integer> data) {
        playerData.put(player, data);
    }
}
