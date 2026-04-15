package com.thepit.Enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum EnchantTypes {

    //global

    SWEATY("Sweaty", "ALL", new String[]{
            "§b+20%§7 XP from streak XP bonus",
            "§b+40%§7 XP from streak XP bonus and §b+50 max XP §7per kill",
            "§b+60%§7 XP from streak XP bonus and §b+100 max XP §7per kill"
    }, 1),

    XPBOOST("XP Boost", "ALL", new String[]{
            "Earn §b+10% XP §7from kills",
            "Earn §b+20% XP §7from kills",
            "Earn §b+30% XP §7from kills"
    }, 1),

    //sword

    EXECUTIONER("§dRARE! §9Executioner", "SWORD", new String[]{
            "Hitting an enemy to below §c1❤§7 instantly kills them",
            "Hitting an enemy to below §c1.5❤§7 instantly kills them",
            "Hitting an enemy to below §c2❤§7 instantly kills them"
    }, 2),

    LIFESTEAL("Lifesteal", "SWORD", new String[]{
            "Heal for §c13%§7 of damage dealt up to §c1.5❤",
            "Heal for §c13%§7 of damage dealt up to §c1.5❤",
            "Heal for §c13%§7 of damage dealt up to §c1.5❤"
    }, 2),


    PERUN("§dRARE! §9Combo: Perun's Wrath", "SWORD", new String[]{
            "Each §efifth §7hit strikes §elightning §7for §c1.5❤",
            "Each §efourth §7hit strikes §elightning §7for §c2❤",
            "Each §efourth §7hit strikes §elightning §7for §c1❤ §7+ §c1❤ §7per §cdiamond piece §7on your victim. %wrap%§fLightning deals true damage"
    }, 2),

    SPIKEY("Spikey", "SWORD", new String[]{
            "Hits deal §c0.125❤ §7true damage",
            "Hits deal §c0.25❤ §7true damage",
            "Hits deal §c0.5❤ §7true damage"
    }, 1),

    SHARP("Sharp", "SWORD", new String[]{
            "Deal §c+4%§7 melee damage",
            "Deal §c+7%§7 melee damage",
            "Deal §c+12%§7 melee damage"
    }, 1),

    PAINFOCUS("Pain Focus", "SWORD", new String[]{
            "Deal §c+1%§7 damage per §c❤§7 you're missing",
            "Deal §c+2%§7 damage per §c❤§7 you're missing",
            "Deal §c+5%§7 damage per §c❤§7 you're missing"
    }, 1),

    //Pants

    PEROXIDE("Peroxide", "PANTS", new String[]{
            "Heal §c0.7❤ when hit (1.5s cooldown)",
            "Heal §c0.7❤ when hit (1.5s cooldown)",
            "Heal §c0.7❤ when hit (1.5s cooldown)"
    }, 1);




    private final String displayName;
    private final String type;
    private final String[] descriptions;
    private final int enchantItemTier;

    EnchantTypes(String displayName, String type, String[] descriptions, int enchantItemTier) {
        this.displayName = displayName;
        this.type = type;
        this.descriptions = descriptions;
        this.enchantItemTier = enchantItemTier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getEnchantRequiredItemTier() {
        return enchantItemTier;
    }

    public String getType() {
        return type;
    }

    public String getDescription(int level) {
        return descriptions[Math.max(0, Math.min(level - 1, descriptions.length - 1))];
    }

    // -----------------------------
    // FIND ENCHANT BY NAME
    // -----------------------------
    public static EnchantTypes getByName(String name) {
        for (EnchantTypes e : values()) {
            String stripped = ChatColor.stripColor(e.displayName);

            if (stripped.equalsIgnoreCase(name) ||
                    e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    // -----------------------------
    // VALIDATE ITEM TYPE
    // -----------------------------
    public boolean isValidForItem(Material mat) {

        switch (type) {

            case "ALL":
                return true;

            case "SWORD":
                return mat.name().endsWith("_SWORD");

            case "BOW":
                return mat == Material.BOW;

            case "PANTS":
                return mat == Material.LEATHER_LEGGINGS;

            default:
                return false;
        }
    }
}
