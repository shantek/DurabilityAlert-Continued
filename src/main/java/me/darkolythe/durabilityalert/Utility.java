package me.darkolythe.durabilityalert;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utility {

    public static void printStatus(Player player, DurabilityAlert main) {
        String type;
        if (main.getPlayerData(player).get(3) == 0) {
            type = "percent left: ";
        } else {
            type = "durability left: ";
        }
        player.sendMessage(DurabilityAlert.prefix + ChatColor.WHITE + "Status for " + player.getDisplayName());
        player.sendMessage(ChatColor.GRAY + " - Warnings: " + (main.getPlayerData(player).get(0) == 0 ? "False" : "True"));
        player.sendMessage(ChatColor.GRAY + " - Tool " + type + main.getPlayerData(player).get(2));
        player.sendMessage(ChatColor.GRAY + " - Armour " + type + main.getPlayerData(player).get(1));
        player.sendMessage(ChatColor.GRAY + " - Alert for enchanted items only: " + (main.getPlayerData(player).get(4) == 0 ? "False" : "True"));
    }
}
