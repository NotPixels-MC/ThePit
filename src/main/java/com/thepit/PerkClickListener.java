package com.thepit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PerkClickListener implements Listener {

    private final PerksGUI gui;

    public PerkClickListener(PerksGUI gui) {
        this.gui = gui;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!e.getView().getTitle().contains("Perks")) return;

        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) return;

        int slot = e.getSlot();

        // Check if clicking on a perk (middle row: 11, 13, 15)
        if (slot == 11 || slot == 13 || slot == 15) {
            handlePerkPurchase(player, slot);
            gui.open(player);
        }
    }

    private void handlePerkPurchase(Player player, int slot) {
        Perk perk;
        if (slot == 11) perk = Perk.GOLDEN_HEADS;
        else if (slot == 13) perk = Perk.FISHING_ROD;
        else perk = Perk.LAVA_BUCKET;

        Stats stats = StatsManager.getStats(player.getUniqueId());
        boolean isUnlocked = PerkManager.isPerkUnlocked(player.getUniqueId(), perk);

        // If unlocked, toggle selection
        if (isUnlocked) {
            Perk currentSelected = PerkManager.getSelectedPerk(player.getUniqueId());
            if (currentSelected != null && currentSelected.equals(perk)) {
                // Deselect
                PerkManager.deselectPerk(player.getUniqueId());
                player.sendMessage(ChatColor.GOLD + "Deselected " + perk.getPerkName());
            } else {
                // Select
                PerkManager.selectPerk(player.getUniqueId(), perk);
                player.sendMessage(ChatColor.LIGHT_PURPLE + "Selected " + perk.getPerkName());
            }
        } else {
            // Not unlocked, try to unlock
            if (stats.getGold() < perk.getGoldCost()) {
                int needed = perk.getGoldCost() - stats.getGold();
                player.sendMessage(ChatColor.RED + "You need " + needed + " more gold!");
                return;
            }

            // Unlock and auto-select
            PerkManager.unlockPerk(player.getUniqueId(), perk);
            PerkManager.selectPerk(player.getUniqueId(), perk);
            stats.addGold(-perk.getGoldCost());

            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "✓ Unlocked " + perk.getPerkName() +
                    ChatColor.RESET + ChatColor.GRAY + " for " + ChatColor.GOLD + perk.getGoldCost() + "g");
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Selected " + perk.getPerkName());
        }

        gui.open(player);
    }
}

