package com.thepit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PerkSlotsClickListener implements Listener {

    private final PerkSlotsGUI gui;

    public PerkSlotsClickListener(PerkSlotsGUI gui) {
        this.gui = gui;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!e.getView().getTitle().contains("Perk Slots")) return;

        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) return;

        int slot = e.getSlot();

        // Check if clicking on perk slots (middle row: 11, 13, 15)
        if (slot == 11 || slot == 13 || slot == 15) {
            handleSlotClick(player, slot);
        }
    }

    private void handleSlotClick(Player player, int slot) {
        PerkSlot perkSlot;
        if (slot == 11) perkSlot = PerkSlot.SLOT_1;
        else if (slot == 13) perkSlot = PerkSlot.SLOT_2;
        else perkSlot = PerkSlot.SLOT_3;

        // Open selection menu for this slot
        PerkSelectionGUI selectionGUI = new PerkSelectionGUI(perkSlot);
        selectionGUI.open(player);
    }

}

