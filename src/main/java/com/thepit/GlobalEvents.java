package com.thepit;

import com.thepit.Megastreaks.MegastreakTypes;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class GlobalEvents {

    public static void broadcast(String message) {
        Bukkit.broadcastMessage(message);
    }

    public static void playSound(Sound sound, float volume, float pitch) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), sound, volume, pitch);
        }
    }

    public static void announceMegastreak(Player player, MegastreakTypes type) {
        String msg = "§6MEGASTREAK! §e" + player.getName() + " §7activated §c" + type.name();
        broadcast(msg);

        // Optional dramatic sound
        playSound(Sound.ENDERDRAGON_GROWL, 1f, 1f);
    }
}