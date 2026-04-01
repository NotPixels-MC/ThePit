package com.thepit.Commands;

import com.thepit.UpgradesMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerksCommand implements CommandExecutor {

    private final UpgradesMenu upgradesMenu;

    public PerksCommand(UpgradesMenu upgradesMenu) {
        this.upgradesMenu = upgradesMenu;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        upgradesMenu.openUpgradesMenu(player);
        return true;
    }
}