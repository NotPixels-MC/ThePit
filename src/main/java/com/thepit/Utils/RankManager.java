package com.thepit.Utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class RankManager {

    private static final Map<String, Rank> ranks = new HashMap<>();

    public static void loadRanks(JavaPlugin plugin) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("Ranks");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            String prefix = section.getString(key + ".prefix");
            String color = section.getString(key + ".color");
            String permission = section.getString(key + ".permission");

            ranks.put(key, new Rank(prefix, color, permission));
        }
    }

    public static Rank getRank(Player player) {
        // loop through ranks and find the highest one the player has
        for (Rank rank : ranks.values()) {
            if (player.hasPermission(rank.permission)) {
                return rank;
            }
        }
        return ranks.get("default");
    }
}
