package me.darkolythe.durabilityalert;

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
            int percent = 0;

            // Check if the item is armor
            if (type.contains("helmet") || type.contains("chestplate") || type.contains("leggings") || type.contains("boots") || type.contains("elytra")) {
                percent = main.getArmorThreshold(player); // Use helper method for armor threshold
                isDamaged = true;
            }
            // Check if the item is a tool
            else if (type.contains("pickaxe") || type.contains("axe") || type.contains("shovel") || type.contains("sword") || type.contains("hoe") || type.contains("fishing") || type.contains("shears") || type.contains("shield")) {
                percent = main.getToolsThreshold(player); // Use helper method for tools threshold
                isDamaged = true;
            }

            // Check if the player only wants alerts for enchanted items
            if (item.getEnchantments().isEmpty() && main.isAlertForEnchantedItemsOnly(player)) {
                return;
            }

            // Check if durability alerts are enabled for this player
            if (isDamaged && main.areWarningsEnabled(player)) {
                float toolPercent = (((float) (item.getType().getMaxDurability() - ((Damageable) Objects.requireNonNull(item.getItemMeta())).getDamage())) / ((float) (item.getType().getMaxDurability())) * 100);
                int toolLeft = (item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage());

                // Determine whether to use percent or durability-based alerts
                if ((main.isAlertTypePercent(player) && toolPercent <= percent) || (!main.isAlertTypePercent(player) && toolLeft <= percent)) {
                    if (!type.contains("shears") && !type.contains("shield") && !type.contains("elytra")) {
                        sendWarning(player, WordUtils.capitalize(item.getType().toString().split("_")[1]), toolLeft);
                    } else {
                        sendWarning(player, WordUtils.capitalize(item.getType().toString()), toolLeft);
                    }
                }
            }
        }
    }

    private void sendWarning(Player player, String item, int durability) {
        String subtitle = "";

        // If the item durability is less than 10, warn the player with the remaining durability
        if (durability <= 10) {
            subtitle = ChatColor.GRAY + ChatColor.BOLD.toString() +
                    main.confighandler.durabilityleft.replaceAll("%durability%", ChatColor.RED + ChatColor.BOLD.toString() + durability);
            if (main.isSoundEnabled(player)) { // Use helper method for sound check
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
            }
        } else {
            if (main.isSoundEnabled(player)) { // Use helper method for sound check
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
            }
        }

        player.sendTitle(ChatColor.RED + main.confighandler.lowdurability.replaceAll("%item%", WordUtils.capitalize(item.toLowerCase())), subtitle, 2, DurabilityAlert.displaytime, 2);
    }
}
