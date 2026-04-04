package com.thepit.Megastreaks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MegastreakMenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Choose a killstreak")) {
            e.setCancelled(true); // Prevent taking items

            if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) return;

            String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            try {
                MegastreakTypes chosen = MegastreakTypes.valueOf(name);
                Player player = (Player) e.getWhoClicked();

                player.sendMessage(ChatColor.GREEN + "You selected the " + chosen.name() + " megastreak!");
                player.closeInventory();

                // TODO: Apply the megastreak to the player here

            } catch (IllegalArgumentException ignored) {
                // Not a valid enum name
            }
        }
    }


}
