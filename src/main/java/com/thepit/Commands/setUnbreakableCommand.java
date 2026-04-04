package com.thepit.Commands;

import com.thepit.Utils.EnchantUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class setUnbreakableCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("thepit.setunbreakable")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        ItemStack item = player.getInventory().getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage("You must be holding an item to make it unbreakable.");
            return true;
        }

        EnchantUtils.makeItemUnbreakable(item);
        player.playSound(player.getLocation(), "minecraft:entity.player.levelup", 1.0f, 1.0f);

        player.sendMessage("The item in your main hand is now unbreakable.");
        return true;
    }
}
