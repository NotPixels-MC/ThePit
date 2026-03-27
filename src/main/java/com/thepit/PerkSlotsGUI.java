package com.thepit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class PerkSlotsGUI {

    private static final PerkSlot[] SLOTS = {
            PerkSlot.SLOT_1,
            PerkSlot.SLOT_2,
            PerkSlot.SLOT_3
    };

    // Slot positions (middle row - slots 11, 13, 15)
    private static final int[] SLOT_POSITIONS = {11, 13, 15};

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, ChatColor.YELLOW + "" + ChatColor.BOLD + "Perk Slots");

        Map<PerkSlot, Perk> equippedPerks = PerkManager.getAllEquippedPerks(player.getUniqueId());
        Set<Perk> unlockedPerks = PerkManager.getUnlockedPerks(player.getUniqueId());

        // Display unlocked perks at top (slots 9-17) for reference
        int posIndex = 0;
        for (Perk perk : unlockedPerks) {
            if (posIndex >= 9) break;
            inv.setItem(9 + posIndex, createUnlockedPerkDisplay(perk));
            posIndex++;
        }

        // Display slots in middle row
        for (int i = 0; i < SLOTS.length; i++) {
            PerkSlot slot = SLOTS[i];
            int invSlot = SLOT_POSITIONS[i];
            Perk equippedPerk = equippedPerks.get(slot);
            inv.setItem(invSlot, createSlotItem(slot, equippedPerk));
        }

        player.openInventory(inv);
    }

    private ItemStack createUnlockedPerkDisplay(Perk perk) {
        ItemStack item = new ItemStack(perk.getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + perk.getPerkName() + " (info only)");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + perk.getDescription(),
                ChatColor.DARK_GRAY + "Your unlocked perks"
        ));

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createSlotItem(PerkSlot slot, Perk equipped) {
        ItemStack item;
        ItemMeta meta;

        if (equipped != null) {
            // Equipped state
            item = new ItemStack(equipped.getMaterial());
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + slot.getSlotName());
            meta.setLore(Arrays.asList(
                    ChatColor.AQUA + equipped.getPerkName(),
                    "",
                    ChatColor.YELLOW + equipped.getDescription(),
                    "",
                    ChatColor.GRAY + "(Click to change)"
            ));
        } else {
            // Empty slot
            item = new ItemStack(Material.DIAMOND_BLOCK);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + slot.getSlotName());
            meta.setLore(Arrays.asList(
                    ChatColor.GREEN + "Unlocked & Empty",
                    "",
                    ChatColor.YELLOW + "Click to select a perk"
            ));
        }

        item.setItemMeta(meta);
        return item;
    }

    public PerkSelectionGUI getSelectionGUI(PerkSlot slot) {
        return new PerkSelectionGUI(slot);
    }
}

