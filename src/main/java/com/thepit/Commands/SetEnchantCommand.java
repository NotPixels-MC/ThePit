package com.thepit.Commands;

import com.thepit.Enchants.EnchantTypes;
import com.thepit.Utils.EnchantUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetEnchantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("thepit.setenchant")) {
            sender.sendMessage("§cNo permission.");
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage("§cUsage: /setenchant <player> <enchant> <level>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        String enchantName = args[1];
        int level;

        try {
            level = Integer.parseInt(args[2]);
        } catch (Exception e) {
            sender.sendMessage("§cLevel must be a number.");
            return true;
        }

        ItemStack item = target.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            sender.sendMessage("§cPlayer is not holding an item.");
            return true;
        }

        // Find enchant
        EnchantTypes enchant = EnchantTypes.getByName(enchantName);
        if (enchant == null) {
            sender.sendMessage("§cUnknown enchant.");
            return true;
        }

        // Validate item type
        if (!enchant.isValidForItem(item.getType())) {
            sender.sendMessage("§cYou cannot apply this enchant to that item.");
            return true;
        }

        // Apply enchant
        EnchantUtils.applyEnchant(item, enchant, level);


        sender.sendMessage("§aApplied enchant §e" + enchant.getDisplayName() + " " + level + " §ato " + target.getName());
        target.sendMessage("§aYour item was enchanted with §e" + enchant.getDisplayName() + " " + level);

        return true;
    }
}
