package com.thepit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PerkSelectionClickListener implements Listener {

    private final PerkSlotsGUI slotsGUI;

    public PerkSelectionClickListener(PerkSlotsGUI slotsGUI) {
        this.slotsGUI = slotsGUI;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        String title = e.getView().getTitle();
        if (!title.contains("Select Perk for")) return;

        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) return;

        int slot = e.getSlot();

        // Check if clicking on a perk (middle row: 11, 13, 15)
        if (slot == 11 || slot == 13 || slot == 15) {
            handlePerkSelection(player, slot, title);
        }
    }

    private void handlePerkSelection(Player player, int slot, String title) {
        // Determine which perk was clicked
        Perk perk;
        if (slot == 11) perk = Perk.GOLDEN_HEADS;
        else if (slot == 13) perk = Perk.FISHING_ROD;
        else perk = Perk.LAVA_BUCKET;

        // Determine which slot to assign to
        PerkSlot perkSlot;
        if (title.contains("Slot 1")) perkSlot = PerkSlot.SLOT_1;
        else if (title.contains("Slot 2")) perkSlot = PerkSlot.SLOT_2;
        else perkSlot = PerkSlot.SLOT_3;

        Stats stats = StatsManager.getStats(player.getUniqueId());
        boolean isUnlocked = PerkManager.isPerkUnlocked(player.getUniqueId(), perk);

        // If not unlocked, try to unlock (purchase)
        if (!isUnlocked) {
            if (stats.getGold() < perk.getGoldCost()) {
                int needed = perk.getGoldCost() - stats.getGold();
                player.sendMessage(ChatColor.RED + "You need " + needed + " more gold!");
                return;
            }

            // Unlock and deduct gold
            PerkManager.unlockPerk(player.getUniqueId(), perk);
            stats.addGold(-perk.getGoldCost());

            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "✓ Unlocked " + perk.getPerkName() +
                    ChatColor.RESET + ChatColor.GRAY + " for " + ChatColor.GOLD + perk.getGoldCost() + "g");
        }

        // Equip the perk to the slot
        PerkManager.equipPerkInSlot(player.getUniqueId(), perkSlot, perk);
        player.sendMessage(ChatColor.GREEN + "Equipped " + perk.getPerkName() +
                " to " + perkSlot.getSlotName());

        // Go back to slots menu
        slotsGUI.open(player);
    }
}

