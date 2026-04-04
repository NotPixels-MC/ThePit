package com.thepit.Commands;

import com.thepit.Utils.EnchantUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddLivesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        Player p = (Player) sender;

        if (args.length != 2) {
            p.sendMessage("§cUsage: /addlives <lives> <maxLives>");
            return true;
        }

        int lives, maxLives;

        try {
            lives = Integer.parseInt(args[0]);
            maxLives = Integer.parseInt(args[1]);
        } catch (Exception e) {
            p.sendMessage("§cNumbers only.");
            return true;
        }

        ItemStack item = p.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            p.sendMessage("§cHold an item first.");
            return true;
        }

        EnchantUtils.setLives(item, lives, maxLives);

        p.sendMessage("§aSet item lives to §e" + lives + "§7/§c" + maxLives);
        return true;
    }
}
