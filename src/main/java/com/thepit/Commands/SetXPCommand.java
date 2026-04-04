package com.thepit.Commands;

import com.thepit.Main;
import com.thepit.Stats;
import com.thepit.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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

        Stats stats = Main.getInstance().getStats(target.getUniqueId());

        int oldLevel = stats.getLevel();

// set total XP
        stats.setRawXP(amount);

        int newLevel = stats.getLevel();

// level-up animation if needed
        if (newLevel > oldLevel) {
            target.sendTitle("§a§lLEVEL UP!", "§e" + oldLevel + " §7→ §a" + newLevel);
            target.playSound(target.getLocation(), Sound.LEVEL_UP, 1f, 1.5f);
        }

        sender.sendMessage("§aSet §e" + target.getName() + "§a's XP to §b" + amount + " XP§a.");
        target.sendMessage("§eYour XP has been set to §b" + amount + " XP§e.");


        return true;
    }
}