package io.shantek;

import org.bukkit.entity.Player;

public class Utility {

    public static void printStatus(Player player, DurabilityAlert main) {
        PlayerSettings settings = main.getPlayerSettings(player);
        String status = DurabilityAlert.prefix + "Status:\n" +
                "Warnings: " + (settings.isWarningsEnabled() ? "Enabled" : "Disabled") + "\n" +
                "Armor Threshold: " + settings.getArmorThreshold() + "\n" +
                "Tools Threshold: " + settings.getToolsThreshold() + "\n" +
                "Alert Type: " + settings.getAlertType().name() + "\n" +
                "Enchanted Items Only: " + (settings.isEnchantedItemsOnly() ? "Yes" : "No") + "\n" +
                "Sound: " + (settings.isSoundEnabled() ? "Enabled" : "Disabled");
        player.sendMessage(status);
    }
}
