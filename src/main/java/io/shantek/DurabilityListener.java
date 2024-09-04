package io.shantek;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Objects;

public class DurabilityListener implements Listener {

    private final DurabilityAlert main;

    DurabilityListener(DurabilityAlert plugin) {
        main = plugin;
    }

    @EventHandler
    private void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (player.hasPermission("durabilityalert.alert")) {
            String type = item.getType().toString().toLowerCase();

            boolean isDamaged = false;
            int threshold = 0;

            // Check if the item is armor
            if (isArmor(type)) {
                threshold = main.getPlayerSettings(player).getArmorThreshold();
                isDamaged = true;
            }
            // Check if the item is a tool
            else if (isTool(type)) {
                threshold = main.getPlayerSettings(player).getToolsThreshold();
                isDamaged = true;
            }

            // Check if the player only wants alerts for enchanted items
            if (item.getEnchantments().isEmpty() && main.getPlayerSettings(player).isEnchantedItemsOnly()) {
                return;
            }

            // Check if durability alerts are enabled for this player
            PlayerSettings settings = main.getPlayerSettings(player);
            if (isDamaged && settings.isWarningsEnabled()) {
                float toolPercent = ((float) (item.getType().getMaxDurability() - ((Damageable) Objects.requireNonNull(item.getItemMeta())).getDamage())
                        / item.getType().getMaxDurability()) * 100;
                int toolLeft = item.getType().getMaxDurability() - ((Damageable) Objects.requireNonNull(item.getItemMeta())).getDamage();

                boolean alert = false;
                if (settings.getAlertType() == PlayerSettings.AlertType.PERCENT && toolPercent <= threshold) {
                    alert = true;
                } else if (settings.getAlertType() == PlayerSettings.AlertType.DURABILITY && toolLeft <= threshold) {
                    alert = true;
                }

                if (alert) {
                    String itemName = getFormattedItemName(type, item);
                    sendWarning(player, itemName, toolLeft, settings.isSoundEnabled());
                }
            }
        }
    }

    private boolean isArmor(String type) {
        return type.contains("helmet") || type.contains("chestplate") || type.contains("leggings") || type.contains("boots") || type.contains("elytra");
    }

    private boolean isTool(String type) {
        return type.contains("pickaxe") || type.contains("axe") || type.contains("shovel") || type.contains("sword") ||
                type.contains("hoe") || type.contains("fishing") || type.contains("shears") || type.contains("shield");
    }

    private String getFormattedItemName(String type, ItemStack item) {
        if (!type.contains("shears") && !type.contains("shield") && !type.contains("elytra")) {
            String[] parts = item.getType().toString().split("_");
            if (parts.length > 1) {
                return WordUtils.capitalize(parts[1].toLowerCase());
            }
        }
        return WordUtils.capitalize(item.getType().toString().toLowerCase());
    }

    private void sendWarning(Player player, String item, int durability, boolean soundEnabled) {
        String subtitle = "";

        // If the item durability is less than or equal to 10, warn the player with the remaining durability
        if (durability <= 10) {
            subtitle = ChatColor.GRAY.toString() + ChatColor.BOLD +
                    main.configHandler.durabilityLeft.replaceAll("%durability%", ChatColor.RED.toString() + ChatColor.BOLD + durability);
            if (soundEnabled) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
            }
        } else {
            if (soundEnabled) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
            }
        }

        String title = ChatColor.RED + main.configHandler.lowDurability.replaceAll("%item%", item);
        player.sendTitle(title, subtitle, 2, DurabilityAlert.getDisplayTime(), 2);

    }
}
