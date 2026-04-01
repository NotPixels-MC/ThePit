package com.thepit.Commands;

import com.thepit.Stats;
import com.thepit.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGoldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Permission check
        if (!sender.hasPermission("thepit.setgold")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        // Usage check
        if (args.length != 2) {
            sender.sendMessage("§cUsage: /setgold <player> <amount>");
            return true;
        }

        // Get target player
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        // Parse gold amount
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cAmount must be a number.");
            return true;
        }

        // Apply gold
        Stats stats = StatsManager.getStats(target.getUniqueId());
        stats.setGold(amount);

        sender.sendMessage("§aSet §e" + target.getName() + "§a's gold to §6" + amount + "g§a.");
        target.sendMessage("§eYour gold has been set to §6" + amount + "g§e.");

        return true;
    }
}