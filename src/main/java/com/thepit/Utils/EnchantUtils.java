package com.thepit.Utils;

import com.thepit.Enchants.PitEnchant;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantUtils {

    public static void applyEnchant(ItemStack item, PitEnchant enchant, int level) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        lore.removeIf(line -> ChatColor.stripColor(line).startsWith(enchant.displayName));
        lore.add("§9" + enchant.displayName + " " + TextUtils.toRoman(level));

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}