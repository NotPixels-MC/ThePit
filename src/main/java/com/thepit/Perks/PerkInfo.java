package com.thepit.Perks;

import org.bukkit.Material;

public class PerkInfo {

    public String id;
    public String displayName;
    public Material icon;
    public int price;
    public int requiredLevel;
    public int requiredPrestige;
    public String[] description;

    public PerkInfo(String id, String displayName, Material icon, int price, int requiredLevel, int requiredPrestige, String... description) {
        this.id = id;
        this.displayName = displayName;
        this.icon = icon;
        this.price = price;
        this.requiredLevel = requiredLevel;
        this.requiredPrestige = requiredPrestige;
        this.description = description;
    }
}