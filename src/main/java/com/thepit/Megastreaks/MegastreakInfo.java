package com.thepit.Megastreaks;

import org.bukkit.Material;
import java.util.List;

public class MegastreakInfo {

    private final Material icon;
    private final List<String> lore;

    public MegastreakInfo(Material icon, List<String> lore) {
        this.icon = icon;
        this.lore = lore;
    }

    public Material getIcon() {
        return icon;
    }

    public List<String> getLore() {
        return lore;
    }
}
