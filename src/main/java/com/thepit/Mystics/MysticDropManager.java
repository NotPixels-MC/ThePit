package com.thepit.Mystics;

import com.thepit.Main;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class MysticDropManager {

    public void dropMystic(Entity entity) {
        //i want it to select a random number 1-5
        int RandomMysticDrop = (int) (Math.random() * 7) + 1;

        World world = entity.getWorld();
        Location loc = entity.getLocation();

        if (RandomMysticDrop == 1) {
            ItemStack mysticItem = new ItemStack(Material.GOLD_SWORD);
            ItemMeta meta = mysticItem.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + "Mystic Sword");
            meta.setLore(java.util.Arrays.asList(
                    ChatColor.GRAY + "Kept on death",
                    "",
                    ChatColor.GRAY + "Used in the mystic well"
            ));
            meta.spigot().setUnbreakable(true);
            mysticItem.setItemMeta(meta);
            world.dropItem(loc, mysticItem);
        }

        if (RandomMysticDrop == 2) {
            ItemStack mysticItem = new ItemStack(Material.BOW);
            ItemMeta meta = mysticItem.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "Mystic Bow");
            meta.setLore(java.util.Arrays.asList(
                    ChatColor.GRAY + "Kept on death",
                    "",
                    ChatColor.GRAY + "Used in the mystic well"
            ));
            meta.spigot().setUnbreakable(true);
            mysticItem.setItemMeta(meta);
            world.dropItem(loc, mysticItem);
        }

        if (RandomMysticDrop == 3) {
            ItemStack mysticItem = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta meta = (LeatherArmorMeta) mysticItem.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Fresh Red Pants");
            meta.setLore(java.util.Arrays.asList(
                    ChatColor.RED + "As strong as iron",
                    ChatColor.GRAY + "Kept on death",
                    "",
                    ChatColor.RED + "Used in the mystic well",
                    ChatColor.RED + "Also, a fashion statement"
            ));
            meta.spigot().setUnbreakable(true);
            meta.setColor(hexToColor("#FF5555"));

            mysticItem.setItemMeta(meta);
            world.dropItem(loc, mysticItem);

        }

        if (RandomMysticDrop == 4) {
            ItemStack mysticItem = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta meta = (LeatherArmorMeta) mysticItem.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "Fresh Orange Pants");
            meta.setLore(java.util.Arrays.asList(
                    ChatColor.GOLD + "As strong as iron",
                    ChatColor.GRAY + "Kept on death",
                    "",
                    ChatColor.GOLD + "Used in the mystic well",
                    ChatColor.GOLD + "Also, a fashion statement"
            ));
            meta.spigot().setUnbreakable(true);
            meta.setColor(hexToColor("#FFAA00"));

            mysticItem.setItemMeta(meta);
            world.dropItem(loc, mysticItem);

        }

        if (RandomMysticDrop == 5) {
            ItemStack mysticItem = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta meta = (LeatherArmorMeta) mysticItem.getItemMeta();
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Fresh Yellow Pants");
            meta.setLore(java.util.Arrays.asList(
                    ChatColor.YELLOW + "As strong as iron",
                    ChatColor.GRAY + "Kept on death",
                    "",
                    ChatColor.YELLOW + "Used in the mystic well",
                    ChatColor.YELLOW + "Also, a fashion statement"
            ));
            meta.spigot().setUnbreakable(true);
            meta.setColor(hexToColor("#FFFF55"));

            mysticItem.setItemMeta(meta);
            world.dropItem(loc, mysticItem);
        }

        if (RandomMysticDrop == 6) {
            ItemStack mysticItem = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta meta = (LeatherArmorMeta) mysticItem.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Fresh Green Pants");
            meta.setLore(java.util.Arrays.asList(
                    ChatColor.GREEN + "As strong as iron",
                    ChatColor.GRAY + "Kept on death",
                    "",
                    ChatColor.GREEN + "Used in the mystic well",
                    ChatColor.GREEN + "Also, a fashion statement"
            ));
            meta.spigot().setUnbreakable(true);
            meta.setColor(hexToColor("#55FF55"));

            mysticItem.setItemMeta(meta);
            world.dropItem(loc, mysticItem);
        }

        if (RandomMysticDrop == 7) {
            ItemStack mysticItem = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta meta = (LeatherArmorMeta) mysticItem.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "Fresh Blue Pants");
            meta.setLore(java.util.Arrays.asList(
                    ChatColor.BLUE + "As strong as iron",
                    ChatColor.GRAY + "Kept on death",
                    "",
                    ChatColor.BLUE + "Used in the mystic well",
                    ChatColor.BLUE + "Also, a fashion statement"
            ));
            meta.spigot().setUnbreakable(true);
            meta.setColor(hexToColor("#5555FF"));

            mysticItem.setItemMeta(meta);
            world.dropItem(loc, mysticItem);
        }

    }

    public static Color hexToColor(String hex) {
        java.awt.Color awt = java.awt.Color.decode(hex);
        return Color.fromRGB(awt.getRed(), awt.getGreen(), awt.getBlue());
    }


    public void spawnFloatingWater(Location loc) {
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();

        // Stage 1: small burst (offset 1)
        spawnBurst(loc, 1, 100, 0);

        // Stage 2: medium burst (offset 2)
        spawnBurst(loc, 2, 250, 5);

        // Stage 3: large burst (offset 3)
        spawnBurst(loc, 3, 500, 10);

    }

    private void spawnBurst(Location loc, int radius, int amount, int delayTicks) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {

            for (int i = 0; i < amount; i++) {

                // random spawn offset based on radius
                float ox = (float) ((Math.random() - 0.5) * radius * 2);
                float oy = (float) ((Math.random() - 0.5) * radius * 2);
                float oz = (float) ((Math.random() - 0.5) * radius * 2);

                // random velocity
                float vx = (float) ((Math.random() - 0.5) * 0.4);
                float vy = (float) ((Math.random() - 0.5) * 0.4);
                float vz = (float) ((Math.random() - 0.5) * 0.4);

                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                        EnumParticle.WATER_SPLASH,
                        true,
                        (float) loc.getX() + ox,
                        (float) loc.getY() + oy + 1,
                        (float) loc.getZ() + oz,
                        vx, vy, vz,
                        0,
                        0
                );

                for (Player p : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
                }
            }

        }, delayTicks);
    }


}
