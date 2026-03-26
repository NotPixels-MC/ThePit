package com.thepit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ScoreboardTask extends BukkitRunnable {

    private static HashMap<UUID, ScoreboardManager> playerScoreboards = new HashMap<>();

    @Override
    public void run() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ScoreboardManager scoreboard = playerScoreboards.get(player.getUniqueId());
                if (scoreboard == null) {
                    scoreboard = new ScoreboardManager(player);
                    playerScoreboards.put(player.getUniqueId(), scoreboard);
                }
                scoreboard.update();
            }
        } catch (Exception e) {
            System.err.println("[ThePit] Error updating scoreboards: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void removePlayer(UUID uuid) {
        playerScoreboards.remove(uuid);
    }

    public static void start() {
        ScoreboardTask task = new ScoreboardTask();
        task.runTaskTimer(Main.getInstance(), 0L, 20L); // 0 delay, 20 ticks = 1 second
    }
}

