package com.thepit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreboardManager {

    private Player player;
    private Scoreboard scoreboard;
    private Objective objective;
    private Stats stats;

    public ScoreboardManager(Player player) {
        this.player = player;
        this.stats = StatsManager.getStats(player.getUniqueId());
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("ThePit", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "THE PIT");
        player.setScoreboard(scoreboard);
    }

    public void update() {
        // Null safety check
        if (stats == null || objective == null || scoreboard == null) {
            return;
        }

        // Clear all scores
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        int line = 9;

        // Line 9: DATE (top)
        String date = new SimpleDateFormat("MM/dd/yy").format(new Date());
        setScore(ChatColor.GRAY + "Date: " + ChatColor.YELLOW + date, line--);

        // Line 8: BLANK LINE
        setScore("  ", line--);

        // Line 7: Prestige (only if not 0)
        if (stats.getPrestige() > 0) {
            setScore(ChatColor.WHITE + "Prestige: " + ChatColor.RESET + ChatColor.YELLOW + stats.getPrestigeRomanNumeral(stats.getPrestige()), line--);
        }

        // Line 6: Level
        setScore(ChatColor.WHITE + "Level: " + ChatColor.RESET + stats.getDisplayLevel(stats.getLevel(), stats.getPrestige()), line--);

        // Line 5: XP Left
        int xpLeft = stats.getXPLeft();
        setScore(ChatColor.WHITE + "Needed XP: " + ChatColor.AQUA + xpLeft, line--);

        // Line 4: BLANK LINE
        setScore(" ", line--);

        setScore(ChatColor.WHITE + "Gold: " + ChatColor.GOLD + stats.getGold() + "g ", line--);

        setScore("   ", line--);

        // Line 3: Status
        setScore(ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Idle", line--);

        // Killstreak (if > 1)
        if (stats.getKillstreak() > 1) {
            setScore(ChatColor.WHITE + "Streak: " + ChatColor.GREEN + stats.getKillstreak(), line--);
        }

        // Line 2: BLANK LINE
        setScore("", line--);

        // Line 1: Server IP (bottom)
        setScore(ChatColor.YELLOW + Config.getServerIP(), line--);
    }

    private void setScore(String text, int score) {
        Score s = objective.getScore(text);
        s.setScore(score);
    }
}
