package me.darkolythe.durabilityalert;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.List;

public class DurabilityListener implements Listener {

    private DurabilityAlert main;
    DurabilityListener(DurabilityAlert plugin) {
        main = plugin;
    }

    @EventHandler
    private void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        String type = item.getType().toString().toLowerCase();
        List<Integer> data = main.getPlayerData(event.getPlayer());

        boolean isDamaged = false;
        int percent = 0;

        if (type.contains("helmet") || type.contains("chestplate") || type.contains("leggings") || type.contains("boots")) {
            percent = data.get(1);
            isDamaged = true;
        } else if (type.contains("pickaxe") || type.contains("axe") || type.contains("shovel") || type.contains("sword") || type.contains("hoe") || type.contains("fishing") || type.contains("shear")) {
            percent = data.get(2);
            isDamaged = true;
        }

        if (isDamaged && data.get(0) == 1) {
            int toolPercent = (int)(((float)(item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage())) / ((float)(item.getType().getMaxDurability())) * 100);
            if ((toolPercent) <= percent) {
                sendWarning(player, WordUtils.capitalize(item.getType().toString().toLowerCase().replace("_", " ")));
            }
        }
    }

    private void sendWarning(Player player, String item) {
        player.sendTitle("", ChatColor.RED + "Low durability on " + item, 2, 10, 2);
    }
}
