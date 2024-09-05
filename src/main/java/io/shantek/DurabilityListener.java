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

    public DurabilityListener(DurabilityAlert plugin) {
        main = plugin;
    }

    @EventHandler
    private void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (!player.hasPermission("durabilityalert.alert")) {
            return;
        }

        String type = item.getType().toString().toLowerCase();
        PlayerSettings settings = main.getPlayerSettings(player);

        if (!settings.isWarningsEnabled()) {
            return; // Exit early if warnings are disabled
        }

        boolean isArmor = isArmor(type);
        boolean isTool = isTool(type);

        // Check if the item should trigger a warning
        if (!isArmor && !isTool) {
            return; // Exit early if it's neither armor nor tool
        }

        // Check for enchanted item alert condition
        if (item.getEnchantments().isEmpty() && settings.isEnchantedItemsOnly()) {
            return; // Exit early if player only wants alerts for enchanted items
        }

        int threshold = isArmor ? settings.getArmorThreshold() : settings.getToolsThreshold();
        int durabilityLeft = calculateDurabilityLeft(item);
        float percentLeft = calculateDurabilityPercent(item);

        boolean shouldAlert = shouldTriggerAlert(settings, percentLeft, durabilityLeft, threshold);

        if (shouldAlert) {
            String itemName = getFormattedItemName(type, item);
            sendWarning(player, itemName, durabilityLeft, settings.isSoundEnabled());
        }
    }

    // Calculate how much durability is left
    private int calculateDurabilityLeft(ItemStack item) {
        return item.getType().getMaxDurability() - ((Damageable) Objects.requireNonNull(item.getItemMeta())).getDamage();
    }

    // Calculate the percentage of durability left
    private float calculateDurabilityPercent(ItemStack item) {
        return ((float) calculateDurabilityLeft(item) / item.getType().getMaxDurability()) * 100;
    }

    // Check whether to trigger an alert based on settings
    private boolean shouldTriggerAlert(PlayerSettings settings, float percentLeft, int durabilityLeft, int threshold) {
        if (settings.getAlertType() == PlayerSettings.AlertType.PERCENT) {
            return percentLeft <= threshold;
        } else {
            return durabilityLeft <= threshold;
        }
    }

    private boolean isArmor(String type) {
        return type.contains("helmet") || type.contains("chestplate") || type.contains("leggings")
                || type.contains("boots") || type.contains("elytra");
    }

    private boolean isTool(String type) {
        return type.contains("pickaxe") || type.contains("axe") || type.contains("shovel")
                || type.contains("sword") || type.contains("hoe") || type.contains("fishing")
                || type.contains("shears") || type.contains("shield");
    }

    private String getFormattedItemName(String type, ItemStack item) {
        String[] parts = item.getType().toString().split("_");
        if (parts.length > 1) {
            return WordUtils.capitalize(parts[1].toLowerCase());
        }
        return WordUtils.capitalize(type.toLowerCase());
    }

    private void sendWarning(Player player, String item, int durability, boolean soundEnabled) {
        String subtitle = buildSubtitle(durability);
        if (soundEnabled) {
            playWarningSound(player, durability);
        }
        String title = ChatColor.RED + main.configHandler.lowDurability.replaceAll("%item%", item);
        player.sendTitle(title, subtitle, 2, DurabilityAlert.getDisplayTime(), 2);
    }

    private String buildSubtitle(int durability) {
        if (durability <= 10) {
            return ChatColor.GRAY.toString() + ChatColor.BOLD + main.configHandler.durabilityLeft
                    .replaceAll("%durability%", ChatColor.RED.toString() + ChatColor.BOLD + durability);
        }
        return "";
    }

    private void playWarningSound(Player player, int durability) {
        Sound sound = durability <= 10 ? Sound.BLOCK_NOTE_BLOCK_SNARE : Sound.BLOCK_NOTE_BLOCK_BASEDRUM;
        player.playSound(player.getLocation(), sound, 1, 1);
    }
}
