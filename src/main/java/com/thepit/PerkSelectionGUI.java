package com.thepit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Set;

public class PerkSelectionGUI {

    private static final Perk[] PERKS = {
            Perk.GOLDEN_HEADS,
            Perk.FISHING_ROD,
            Perk.LAVA_BUCKET
    };

    // Perk positions in middle row (slots 11, 13, 15)
    private static final int[] PERK_POSITIONS = {11, 13, 15};

    private final PerkSlot slotToAssign;

    public PerkSelectionGUI(PerkSlot slotToAssign) {
        this.slotToAssign = slotToAssign;
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45,
            ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Select Perk for " + slotToAssign.getSlotName());

        Set<Perk> unlockedPerks = PerkManager.getUnlockedPerks(player.getUniqueId());
        Perk currentlyEquipped = PerkManager.getEquippedPerkInSlot(player.getUniqueId(), slotToAssign);
        Stats stats = StatsManager.getStats(player.getUniqueId());
        int playerGold = stats.getGold();

        for (int i = 0; i < PERKS.length; i++) {
            Perk perk = PERKS[i];
            int slot = PERK_POSITIONS[i];

            boolean isUnlocked = unlockedPerks.contains(perk);
            boolean isEquipped = perk.equals(currentlyEquipped);
            boolean canAfford = playerGold >= perk.getGoldCost();

            inv.setItem(slot, createPerkItem(perk, isUnlocked, isEquipped, canAfford, playerGold));
        }

        player.openInventory(inv);
    }

    private ItemStack createPerkItem(Perk perk, boolean isUnlocked, boolean isEquipped, boolean canAfford, int playerGold) {
        ItemStack item = new ItemStack(perk.getMaterial());
        ItemMeta meta = item.getItemMeta();

        String displayName;
        if (isEquipped) {
            displayName = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "✓ " + perk.getPerkName() + " (Current)";
        } else if (isUnlocked) {
            displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "✓ " + perk.getPerkName();
        } else {
            displayName = ChatColor.GOLD + perk.getPerkName();
        }
        meta.setDisplayName(displayName);

        String statusLine;
        if (isEquipped) {
            statusLine = ChatColor.LIGHT_PURPLE + "Currently equipped in this slot";
        } else if (isUnlocked) {
            statusLine = ChatColor.YELLOW + "Click to equip";
        } else if (canAfford) {
            statusLine = ChatColor.YELLOW + "Click to unlock (" + perk.getGoldCost() + "g)";
        } else {
            int needed = perk.getGoldCost() - playerGold;
            statusLine = ChatColor.RED + "Need " + needed + " more gold";
        }

        meta.setLore(Arrays.asList(
                perk.getDescription(),
                "",
                ChatColor.WHITE + "Cost: " + ChatColor.GOLD + perk.getGoldCost() + "g",
                statusLine
        ));

        item.setItemMeta(meta);
        return item;
    }

    public PerkSlot getSlotToAssign() {
        return slotToAssign;
    }
}

