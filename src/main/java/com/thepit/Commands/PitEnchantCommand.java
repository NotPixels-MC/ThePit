package com.thepit.Commands;

import com.thepit.Enchants.EnchantRegistry;
import com.thepit.Enchants.PitEnchant;
import com.thepit.Utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.thepit.Utils.EnchantUtils.applyEnchant;
import static com.thepit.Utils.TextUtils.toRoman;

public class PitEnchantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        Player p = (Player) sender;

        if (args.length < 2) {
            p.sendMessage("§cUsage: /pitench <enchant> <level>");
            return true;
        }

        String id = args[0].toLowerCase();
        int level;

        try {
            level = Integer.parseInt(args[1]);
        } catch (Exception e) {
            p.sendMessage("§cInvalid level.");
            return true;
        }

        PitEnchant enchant = EnchantRegistry.get(id);

        if (enchant == null) {
            p.sendMessage("§cUnknown enchant: " + id);
            return true;
        }

        if (level < 1 || level > enchant.maxLevel) {
            p.sendMessage("§cLevel must be between 1 and " + enchant.maxLevel);
            return true;
        }

        ItemStack item = p.getItemInHand();

        if (item == null || item.getType().toString().equals("AIR")) {
            p.sendMessage("§cHold an item first.");
            return true;
        }

        applyEnchant(item, enchant, level);

        p.sendMessage("§aApplied §e" + enchant.displayName + " " + toRoman(level) + " §ato your item.");
        return true;
    }

}