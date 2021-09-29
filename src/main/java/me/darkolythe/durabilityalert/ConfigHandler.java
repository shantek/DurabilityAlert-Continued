package me.darkolythe.durabilityalert;

import org.bukkit.ChatColor;

public class ConfigHandler {

    String lowdurability;
    String durabilityleft;
    String warningsdisabled;
    String warningsenabled;
    String invalidarguments;
    String armourset;
    String toolset;
    String mustbenumber;
    String settype;
    String enchantedtrue;
    String enchantedfalse;

    DurabilityAlert main;
    ConfigHandler(DurabilityAlert plugin) {
        main = plugin;

        lowdurability = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("lowdurability"));
        durabilityleft = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("durabilityleft"));
        warningsdisabled = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("warningsdisabled"));
        warningsenabled = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("warningsenabled"));
        invalidarguments = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("invalidarguments"));
        armourset = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("armourset"));
        toolset = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("toolset"));
        mustbenumber = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("mustbenumber"));
        settype = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("settype"));
        enchantedtrue = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("enchantedtrue"));
        enchantedfalse = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("enchantedfalse"));
    }
}
