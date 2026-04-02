package com.thepit.Prestige;

import com.thepit.Stats;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class PrestigeManager {

    private final PrestigeData[] prestigeData = PrestigeData.values();

    public boolean canPrestige(Player player, Stats stats) {

        int prestige = stats.getPrestige();
        int level = stats.getLevel();
        int gold = stats.getGold();

        if (level < 120) {
            player.sendMessage(ChatColor.RED + "You must be level 120 to prestige.");
            return false;
        }

        PrestigeData data = PrestigeData.get(prestige);
        if (data == null) {
            player.sendMessage(ChatColor.RED + "Prestige data not found.");
            return false;
        }

        int requiredGold = data.getGoldRequired();
        if (requiredGold != -1 && gold < requiredGold) {
            player.sendMessage(ChatColor.RED + "You need " + requiredGold + "g to prestige.");
            return false;
        }

        return true;
    }

    public void prestige(Player player, Stats stats) {

        int oldPrestige = stats.getPrestige();
        int newPrestige = oldPrestige + 1;

        // Reset level + XP
        stats.setPrestige(newPrestige);
        stats.setGold(0);
        stats.setXP(0, player);

        // Reset level manually
        try {
            java.lang.reflect.Field levelField = Stats.class.getDeclaredField("level");
            levelField.setAccessible(true);
            levelField.set(stats, 1);
        } catch (Exception ignored) {}

        // Reset perks
        for (int i = 1; i <= 3; i++) {
            stats.setPerk(i, null);
        }

        // Reset killstreaks
        try {
            java.lang.reflect.Field ks = Stats.class.getDeclaredField("killstreak");
            ks.setAccessible(true);
            ks.set(stats, 0);

            java.lang.reflect.Field bks = Stats.class.getDeclaredField("bestKillstreak");
            bks.setAccessible(true);
            bks.set(stats, 0);
        } catch (Exception ignored) {}

        // Clear inventory (perishable items)
        player.getInventory().clear();

        // Send prestige message
        player.sendMessage(ChatColor.GREEN + "You have prestiged to " + roman(newPrestige) + "!");

        // Optional: title
        stats.sendTitle(player, "§b§lPRESTIGE!", "§7You are now §e" + roman(newPrestige), 10, 40, 10);
    }

    public String roman(int number) {
        String[] romans = {
                "0","I","II","III","IV","V","VI","VII","VIII","IX","X",
                "XI","XII","XIII","XIV","XV","XVI","XVII","XVIII","XIX","XX",
                "XXI","XXII","XXIII","XXIV","XXV","XXVI","XXVII","XXVIII","XXIX","XXX",
                "XXXI","XXXII","XXXIII","XXXIV","XXXV","XXXVI","XXXVII","XXXVIII","XXXIX","XL",
                "XLI","XLII","XLIII","XLIV","XLV","XLVI","XLVII","XLVIII","XLIX","L"
        };
        return romans[number];
    }
}