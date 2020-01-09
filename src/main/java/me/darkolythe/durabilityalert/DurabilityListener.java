package me.darkolythe.durabilityalert;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class DurabilityListener implements Listener {

    @EventHandler
    private void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        String type = item.getType().toString().toLowerCase();
        if (type.contains("helmet") || type.contains("chestplate") || type.contains("leggings") || type.contains("boots")) {
            System.out.println("armour");
        } else if (type.contains("pickaxe") || type.contains("axe") || type.contains("shovel") || type.contains("sword") || type.contains("hoe") || type.contains("fishing") || type.contains("shear")) {
            System.out.println("tool");
        }
    }
}
