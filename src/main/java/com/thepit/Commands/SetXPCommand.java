package com.thepit.Commands;

import com.thepit.Stats;
import com.thepit.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetXPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("thepit.setxp")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§cUsage: /setxp <player> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cAmount must be a number.");
            return true;
        }

        Stats stats = StatsManager.getStats(target.getUniqueId());
        stats.setXP(amount, target);

        sender.sendMessage("§aSet §e" + target.getName() + "§a's XP to §b" + amount + " XP§a.");
        target.sendMessage("§eYour XP has been set to §b" + amount + " XP§e.");

        return true;
    }
}