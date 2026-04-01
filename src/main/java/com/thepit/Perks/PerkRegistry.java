package com.thepit.Perks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class PerkRegistry {

    public static final Map<String, PerkInfo> perks = new LinkedHashMap<>();

    static {

        // ============================
        //  PRESTIGE 0 — GOLD PERKS
        // ============================

        perks.put("ghead", new PerkInfo(
                "ghead", "Golden Heads", Material.GOLD_NUGGET, 500, 10, 0,
                "Golden apples you earn turn into",
                "§6Golden Heads§7.",
                "",
                "§7Speed I (0:08)",
                "§7Regen II (0:05)",
                "§73❤ Absorption"
        ));

        perks.put("rod", new PerkInfo(
                "rod", "Fishing Rod", Material.FISHING_ROD, 1000, 10, 0,
                "Spawn with a fishing rod."
        ));

        perks.put("lava", new PerkInfo(
                "lava", "Lava Bucket", Material.LAVA_BUCKET, 1000, 10, 0,
                "Spawn with a lava bucket.",
                "Lava damage counts toward kills."
        ));

        perks.put("strength", new PerkInfo(
                "strength", "Strength-Chaining", Material.REDSTONE, 2000, 20, 0,
                "+8% damage for 7s on kill.",
                "Stacks up to +40%."
        ));

        perks.put("safety", new PerkInfo(
                "safety", "Safety First", Material.CHAINMAIL_HELMET, 3000, 20, 0,
                "Spawn with a chainmail helmet."
        ));

        perks.put("mineman", new PerkInfo(
                "mineman", "Mineman", Material.DIAMOND_PICKAXE, 3000, 30, 0,
                "Spawn with 24 cobblestone",
                "and an Efficiency IV pickaxe."
        ));

        perks.put("bonk", new PerkInfo(
                "bonk", "Bonk!", Material.ANVIL, 2000, 35, 0,
                "First hit you take per life",
                "is blocked and gives Resistance I."
        ));

        perks.put("trickle", new PerkInfo(
                "trickle", "Trickle-down", Material.GOLD_INGOT, 1000, 40, 0,
                "Gold ingots heal 2❤",
                "and give +10g."
        ));

        perks.put("lucky", new PerkInfo(
                "lucky", "Lucky Diamond", Material.DIAMOND_CHESTPLATE, 4000, 40, 0,
                "30% chance to upgrade",
                "iron armor drops to diamond."
        ));

        perks.put("spammer", new PerkInfo(
                "spammer", "Spammer", Material.ARROW, 4000, 40, 0,
                "Get 3 arrows when hitting",
                "a player with a bow."
        ));

        perks.put("bounty", new PerkInfo(
                "bounty", "Bounty Hunter", Material.GOLD_LEGGINGS, 2000, 50, 0,
                "+4g on kills.",
                "+1% damage per 100g bounty."
        ));

        perks.put("streaker", new PerkInfo(
                "streaker", "Streaker", Material.WHEAT, 8000, 50, 0,
                "Triple streak XP bonus."
        ));

        perks.put("gladiator", new PerkInfo(
                "gladiator", "Gladiator", Material.BONE, 4000, 60, 0,
                "-3% damage per nearby player.",
                "Up to -30%."
        ));

        perks.put("vampire", new PerkInfo(
                "vampire", "Vampire", Material.FERMENTED_SPIDER_EYE, 4000, 60, 0,
                "Don't earn golden apples.",
                "Heal 0.5❤ on hit.",
                "Heal 1.5❤ on arrow crit.",
                "Regen I on kill."
        ));


        // ============================
        //  RENOWN PERKS (Prestige gated)
        // ============================

        perks.put("overheal", new PerkInfo(
                "overheal", "Overheal", Material.BREAD, 6000, 70, 1,
                "Double item healing limits."
        ));

        perks.put("barbarian", new PerkInfo(
                "barbarian", "Barbarian", Material.IRON_AXE, 3000, 30, 2,
                "Replace swords with axes.",
                "Axes deal +0.5❤ more damage."
        ));

        perks.put("dirty", new PerkInfo(
                "dirty", "Dirty", Material.DIRT, 8000, 80, 2,
                "Gain Resistance II (0:04)",
                "on kill."
        ));

        perks.put("rambo", new PerkInfo(
                "rambo", "Rambo", Material.STICK, 6000, 70, 3,
                "Heal to full on kill.",
                "Max health -2❤."
        ));

        perks.put("olympus", new PerkInfo(
                "olympus", "Olympus", new ItemStack(Material.POTION, 1, (short) 0).getType(), 6000, 70, 4,
                "Receive Olympus Potions on kill.",
                "Speed I (0:24)",
                "Regen III (0:10)",
                "Resistance II (0:04)",
                "+27 XP"
        ));

        perks.put("assistant", new PerkInfo(
                "assistant", "Assistant to the Streaker", Material.SPRUCE_FENCE, 8000, 50, 5,
                "Assists count toward streaks."
        ));

        perks.put("firststrike", new PerkInfo(
                "firststrike", "First Strike", Material.COOKED_CHICKEN, 8000, 80, 5,
                "+35% damage on first hit.",
                "Gain Speed I (0:05)."
        ));

        perks.put("coopcat", new PerkInfo(
                "coopcat", "Co-op Cat", Material.MONSTER_EGGS, 6000, 50, 6,
                "+50% XP and gold on assists."
        ));

        perks.put("marathon", new PerkInfo(
                "marathon", "Marathon", Material.LEATHER_BOOTS, 8000, 90, 6,
                "Cannot wear boots.",
                "+18% damage with Speed.",
                "-18% damage taken with Speed."
        ));

        perks.put("soup", new PerkInfo(
                "soup", "Soup", Material.MUSHROOM_SOUP, 8000, 90, 7,
                "Receive Tasty Soup on kill.",
                "Speed I (0:07)",
                "Instant Health 1.5❤",
                "Absorption 1❤",
                "+15% next hit"
        ));

        perks.put("recon", new PerkInfo(
                "recon", "Recon", Material.EYE_OF_ENDER, 6000, 60, 7,
                "Every 4th arrow:",
                "+50% damage",
                "+40 XP"
        ));

        perks.put("conglomerate", new PerkInfo(
                "conglomerate", "Conglomerate", Material.HAY_BLOCK, 20000, 50, 8,
                "Kills/assists give no XP.",
                "20% XP converted to gold."
        ));

        perks.put("kungfu", new PerkInfo(
                "kungfu", "Kung Fu Knowledge", Material.RAW_BEEF, 10000, 100, 9,
                "Punch to deal 3.7❤ damage.",
                "Crits deal 3.95❤.",
                "Every 4th hit: Speed II (0:05)."
        ));

        perks.put("thick", new PerkInfo(
                "thick", "Thick", Material.APPLE, 10000, 90, 11,
                "+2❤ max health."
        ));
    }
}