package com.thepit.Megastreaks;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MegastreakData {

    public static final Map<MegastreakTypes, MegastreakInfo> DATA = new HashMap<>();

    static {
        DATA.put(MegastreakTypes.OVERDRIVE, new MegastreakInfo(
                Material.BLAZE_POWDER,
                Arrays.asList(
                        ChatColor.GRAY + "Triggers on: " + ChatColor.RED +"50 kills",
                        "",
                        ChatColor.GRAY + "On trigger:",
                        ChatColor.GREEN + "■ " + ChatColor.GRAY + "Perma " + ChatColor.YELLOW + "Speed I" + ChatColor.GRAY + ".",
                        ChatColor.GREEN + "■ " + ChatColor.GRAY + "Earn " + ChatColor.AQUA + "+50% XP" + ChatColor.GRAY + " from kills.",
                        ChatColor.GREEN + "■ " + ChatColor.GRAY + "Earn " + ChatColor.GOLD + "+100% Gold" + ChatColor.GRAY + " from kills.",
                        ChatColor.GRAY + "",
                        ChatColor.GRAY + "BUT:",
                        ChatColor.RED + "■ " + ChatColor.GRAY + "Receive " +
                                ChatColor.RED + "0.1❤ " + ChatColor.GRAY + "" + ChatColor.ITALIC + "very ",
                        ChatColor.GRAY + "true damage per 5 kills over 50.",
                        ChatColor.GRAY + "",
                        ChatColor.GRAY + "On death:",
                        ChatColor.YELLOW + "■ " + ChatColor.GRAY + "Gain" + ChatColor.AQUA + "4,000 XP" + ChatColor.GRAY + "."
                )
        ));

        DATA.put(MegastreakTypes.BEASTMODE, new MegastreakInfo(
                Material.DIAMOND_HELMET,
                Arrays.asList(
                        ChatColor.GRAY + "Triggers on: " + ChatColor.RED +"50 kills",
                        ChatColor.GREEN + "■ " + ChatColor.GRAY + "Gain a " + ChatColor.AQUA + "Diamond Helmet" + ChatColor.GRAY + ".",
                        ChatColor.GREEN + "■ " + ChatColor.GRAY + "Earn " + ChatColor.AQUA + "+50% XP" + ChatColor.GRAY + " from kills.",
                        ChatColor.GREEN + "■ " + ChatColor.GRAY + "Earn " + ChatColor.GOLD + "+75% Gold" + ChatColor.GRAY + " from kills.",
                        ChatColor.GRAY + "",
                        ChatColor.GRAY + "BUT:"
                        + ChatColor.RED + "■ " + ChatColor.GRAY + "Receive" +
                                ChatColor.RED + "0.1❤" + ChatColor.GRAY + " damage per 5 kills over 50.",
                        ChatColor.GRAY + "",
                        ChatColor.GRAY + "On death:",
                        ChatColor.YELLOW + "■ " + ChatColor.GRAY + "Keep the" + ChatColor.AQUA + "Diamond Helmet" + ChatColor.GRAY + "."
                )
        ));

        DATA.put(MegastreakTypes.HIGHLANDER, new MegastreakInfo(
                Material.IRON_SWORD,
                Arrays.asList(
                        ChatColor.GOLD + "Highlander",
                        ChatColor.YELLOW + "There can only be one.",
                        "",
                        ChatColor.GRAY + "• +0% XP",
                        ChatColor.GRAY + "• +50% Gold",
                        ChatColor.GRAY + "• 0% Mystic Chance"
                )
        ));

        DATA.put(MegastreakTypes.MAGNUMOPUS, new MegastreakInfo(
                Material.BOOK,
                Arrays.asList(
                        ChatColor.GOLD + "Magnum Opus",
                        ChatColor.YELLOW + "Your masterpiece begins.",
                        "",
                        ChatColor.GRAY + "• +0% XP",
                        ChatColor.GRAY + "• +0% Gold",
                        ChatColor.GRAY + "• 0% Mystic Chance"
                )
        ));

        DATA.put(MegastreakTypes.TOTHEMOON, new MegastreakInfo(
                Material.ENDER_PEARL,
                Arrays.asList(
                        ChatColor.GOLD + "To The Moon",
                        ChatColor.YELLOW + "Shoot for the stars.",
                        "",
                        ChatColor.GRAY + "• +20% XP",
                        ChatColor.GRAY + "• +0% Gold",
                        ChatColor.GRAY + "• 0% Mystic Chance"
                )
        ));

        DATA.put(MegastreakTypes.UBERSTREAK, new MegastreakInfo(
                Material.NETHER_STAR,
                Arrays.asList(
                        ChatColor.GOLD + "Uberstreak",
                        ChatColor.YELLOW + "The ultimate challenge.",
                        "",
                        ChatColor.GRAY + "• +0% XP",
                        ChatColor.GRAY + "• +0% Gold",
                        ChatColor.GRAY + "• 50% Mystic Chance"
                )
        ));
    }
}
