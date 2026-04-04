package com.thepit.Megastreaks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MegastreakMenu {

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RED + "Choose a killstreak");

        int slot = 10;
        for (MegastreakTypes type : MegastreakTypes.values()) {
            inv.setItem(slot, createItem(type));
            slot++;
        }

        player.openInventory(inv);
    }

    private static ItemStack createItem(MegastreakTypes type) {
        MegastreakInfo info = MegastreakData.DATA.get(type);

        ItemStack item = new ItemStack(info.getIcon());
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + type.name());
        meta.setLore(info.getLore());

        item.setItemMeta(meta);
        return item;
    }
}
