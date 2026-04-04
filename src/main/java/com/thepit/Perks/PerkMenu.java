package com.thepit.Perks;

import com.thepit.Main;
import com.thepit.Stats;
import com.thepit.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PerkMenu {

    public static Map<UUID, Integer> editingSlot = new HashMap<>();

    public void openPerkMenu(Player player, int slot) {

        Inventory inv = Bukkit.createInventory(null, 54, "Choose a perk");

        Stats stats = Main.getInstance().getStats(player.getUniqueId());



        // Fill everything with border glass
        ItemStack filler = new ItemStack(Material.AIR);
        for (int i = 0; i < 54; i++) inv.setItem(i, filler);

        // Title in top center


        // Back button bottom center
        inv.setItem(49, createItem(Material.ARROW, "§eBack"));

        /*
         * Middle area: rows 2–5, columns 2–8
         * Rows: 1..4 (0-based row index)
         * Cols: 1..7 (0-based col index)
         *
         * Slot index = row * 9 + col
         */

        List<PerkInfo> sortedPerks = new ArrayList<>(PerkRegistry.perks.values());
        sortedPerks.sort(Comparator.comparingInt(p -> p.requiredLevel));

        int perkIndex = 0;
        for (int row = 1; row <= 4; row++) {        // rows 2–5 visually
            for (int col = 1; col <= 7; col++) {    // middle 7 columns
                if (perkIndex >= sortedPerks.size()) break;

                int slotIndex = row * 9 + col;
                PerkInfo perk = sortedPerks.get(perkIndex++);

                inv.setItem(slotIndex, createPerkItem(perk, stats));
            }
        }

        editingSlot.put(player.getUniqueId(), slot);
        player.openInventory(inv);
    }

    private ItemStack createPerkItem(PerkInfo perk, Stats stats) {

        boolean unlocked = stats.hasUnlocked(perk.id);
        int gold = stats.getGold();
        int level = stats.getLevel();

        ItemStack item = new ItemStack(perk.icon);
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();

        if (!unlocked) {
            meta.setDisplayName("§c" + perk.displayName);

            for (String line : perk.description) lore.add("§7" + line);
            lore.add("");

            lore.add("§7Cost: §6" + perk.price + "g");
            lore.add("§7Required Level: §b" + perk.requiredLevel);

            if (level < perk.requiredLevel) {
                lore.add("§cYou need level " + perk.requiredLevel + ".");
            } else if (gold < perk.price) {
                lore.add("§cNot enough gold!");
            } else {
                lore.add("§eClick to unlock!");
            }

        } else {
            meta.setDisplayName("§a" + perk.displayName);

            for (String line : perk.description) lore.add("§7" + line);
            lore.add("");

            lore.add("§eClick to select!");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}