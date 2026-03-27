package com.thepit;

import org.bukkit.Material;

public enum Perk {

    GOLDEN_HEADS(
            "Golden Heads",
            "Heal on kill",
            Material.GOLDEN_APPLE,
            500
    ),

    FISHING_ROD(
            "Fishing Rod",
            "Knockback on hit",
            Material.FISHING_ROD,
            750
    ),

    LAVA_BUCKET(
            "Lava Bucket",
            "Drop lava under enemies",
            Material.LAVA_BUCKET,
            1000
    );

    private final String name;
    private final String description;
    private final Material material;
    private final int goldCost;

    Perk(String name, String description, Material material, int goldCost) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.goldCost = goldCost;
    }

    public String getPerkName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }

    public int getGoldCost() {
        return goldCost;
    }
}

