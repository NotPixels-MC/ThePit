package com.thepit.Enchants;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.thepit.Utils.TextUtils.toRoman;

public class PitEnchant {

    public final String id;
    public final String displayName;
    public final int maxLevel;

    public PitEnchant(String id, String displayName, int maxLevel) {
        this.id = id;
        this.displayName = displayName;
        this.maxLevel = maxLevel;
    }

    public void applyEnchant(ItemStack item, PitEnchant enchant, int level) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        // Remove old version of this enchant
        lore.removeIf(line -> ChatColor.stripColor(line).startsWith(enchant.displayName));

        // Add new enchant line
        lore.add("§9" + enchant.displayName + " " + toRoman(level));

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}