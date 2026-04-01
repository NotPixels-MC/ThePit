package com.thepit.Enchants;

import org.bukkit.Material;

import java.util.HashMap;

public class EnchantRegistry {

    public static HashMap<String, PitEnchant> enchants = new HashMap<>();

    public static void registerDefaults() {
        register(new PitEnchant("executioner", "Executioner", 255));
        register(new PitEnchant("lifesteal", "Lifesteal", 255));
        register(new PitEnchant("combo_damage", "Combo: Damage", 255));
        // add more here
    }

    public static void register(PitEnchant enchant) {
        enchants.put(enchant.id.toLowerCase(), enchant);
    }

    public static PitEnchant get(String id) {
        return enchants.get(id.toLowerCase());
    }



}