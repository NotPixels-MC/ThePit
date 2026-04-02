package com.thepit.Commands;

import com.thepit.Prestige.PrestigeManager;
import com.thepit.Stats;
import com.thepit.StatsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class PrestigeCommand implements CommandExecutor {

    private final PrestigeManager prestigeManager;
    private final StatsManager statsManager;

    public PrestigeCommand(PrestigeManager prestigeManager, StatsManager statsManager) {
        this.prestigeManager = prestigeManager;
        this.statsManager = statsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("myplugin.prestige")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to do that.");
            return true;
        }

        Stats stats = statsManager.getStats(player.getUniqueId());

        if (!prestigeManager.canPrestige(player, stats)) {
            return true;
        }

        prestigeManager.prestige(player, stats);
        player.sendMessage(ChatColor.GREEN + "You have prestiged successfully!");
        return true;
    }
}