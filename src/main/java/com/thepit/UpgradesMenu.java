package com.thepit;

import com.thepit.Perks.PerkInfo;
import com.thepit.Perks.PerkRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class UpgradesMenu {

    public void openUpgradesMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, "Upgrades");

        Stats stats = Main.getInstance().getStats(player.getUniqueId());

        int level = stats.getLevel();

        // Equipped perks
        String perk1 = stats.getPerk(1);
        String perk2 = stats.getPerk(2);
        String perk3 = stats.getPerk(3);

        // Build slots
        inv.setItem(12, buildPerkSlot(perk1, 10, level, 1));
        inv.setItem(13, buildPerkSlot(perk2, 35, level, 2));
        inv.setItem(14, buildPerkSlot(perk3, 70, level, 3));
        inv.setItem(15, buildKillstreakButton());


        player.openInventory(inv);
    }

    private ItemStack buildPerkSlot(String perkId, int requiredLevel, int playerLevel, int slotNumber) {

        // Slot locked
        if (playerLevel < requiredLevel) {
            ItemStack locked = new ItemStack(Material.BEDROCK);
            ItemMeta meta = locked.getItemMeta();
            meta.setDisplayName("§cLocked");
            meta.setLore(Arrays.asList(
                    "§7Select a perk to fill this slot.",
                    "",
                    "§cReach level §7[" + getLevelColor(requiredLevel) + requiredLevel + "§7] §cto unlock"
            ));
            locked.setItemMeta(meta);
            return locked;
        }

        // Slot unlocked — default icon
        Material icon = Material.DIAMOND_BLOCK;
        String displayName = "§aPerk Slot #" + slotNumber;

        // If a perk is selected, use its real icon + name
        if (perkId != null && PerkRegistry.perks.containsKey(perkId)) {
            PerkInfo info = PerkRegistry.perks.get(perkId);
            icon = info.icon;
            displayName = "§a" + info.displayName;
        }

        ItemStack perk = new ItemStack(icon);
        ItemMeta meta = perk.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(
                "§7Select a perk to fill this slot.",
                "",
                "§eClick to choose perk!"
        ));
        perk.setItemMeta(meta);

        return perk;
    }

    private String getLevelColor(int level) {
        if (level >= 70) return "§c§l";
        if (level >= 40) return "§a";
        if (level >= 20) return "§3";
        if (level >= 10) return "§9";
        return "§7";
    }

    private ItemStack buildKillstreakButton() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§bChoose Killstreak");
        meta.setLore(Arrays.asList(
                "§7Select your megastreak.",
                "",
                "§eClick to open!"
        ));

        item.setItemMeta(meta);
        return item;
    }

}