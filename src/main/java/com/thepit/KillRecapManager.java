package com.thepit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.Material;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class KillRecapManager {

    private static final HashMap<UUID, ItemStack> recapMap = new HashMap<>();

    public static void createRecap(Player victim, Player killer, int xp, int gold, int streak) {

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setTitle("Kill Recap");
        meta.setAuthor("The Pit");

        String page =
                "§l§cYOU DIED!\n\n" +
                        "§7Killed by: §c" + killer.getName() + "\n" +
                        "§7Killer Health: §a" + String.format("%.1f", killer.getHealth()) + "❤\n\n" +
                        "§b+" + xp + " XP\n" +
                        "§6+" + gold + "g\n\n" +
                        "§eKiller Streak: §a" + streak;

        meta.addPage(page);
        book.setItemMeta(meta);

        recapMap.put(victim.getUniqueId(), book);

        // Auto-remove after 30 seconds
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            recapMap.remove(victim.getUniqueId());
        }, 20 * 30);
    }

    public static ItemStack getRecap(UUID uuid) {
        return recapMap.get(uuid);
    }
}