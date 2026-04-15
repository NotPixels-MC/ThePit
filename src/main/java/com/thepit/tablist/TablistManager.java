package com.thepit.tablist;

import com.thepit.Main;
import com.thepit.Stats;
import com.thepit.Utils.Rank;
import com.thepit.Utils.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TablistManager {

    public static void start() {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                update(player);
            }
        }, 0L, 60L); // every 60 ticks
    }

    public static void update(Player player) {
        Stats stats = Main.getInstance().getStats(player.getUniqueId());
        Rank rank = RankManager.getRank(player);

        // Level like [25]
        String displayLevel = stats.getDisplayLevel(stats.getLevel(), stats.getPrestige());

        // Rank color
        String nameColor = ChatColor.translateAlternateColorCodes('&', rank.color);

        // Final tablist name
        String tabName = displayLevel + " " + nameColor + player.getName();

        player.setPlayerListName(tabName);
    }
}
