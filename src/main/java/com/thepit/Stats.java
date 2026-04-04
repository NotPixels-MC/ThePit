package com.thepit;

import com.thepit.Megastreaks.MegastreakTypes;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Stats {

    private final UUID uuid;

    private int killstreak = 0;
    private int bestKillstreak = 0;
    private int kills = 0;
    private int deaths = 0;
    private String[] perks = new String[3];
    private List<String> unlockedPerks = new ArrayList<>();
    private MegastreakTypes megastreak = MegastreakTypes.OVERDRIVE; // or NONE if you add it
    private MegastreakTypes selectedMegastreak = MegastreakTypes.OVERDRIVE;

    private int xp = 0;
    private int gold = 0;
    private int prestige = 0;
    private int peruncombo = 0;

    public Stats(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getKillstreak() {
        return killstreak;
    }

    public void addKill() {
        kills++;
        killstreak++;

        if (killstreak > bestKillstreak) {
            bestKillstreak = killstreak;
        }
    }

    public void addDeath() {
        deaths++;
        killstreak = 0;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBestKillstreak() {
        return bestKillstreak;
    }

    // -------------------------
    // XP + LEVEL SYSTEM
    // -------------------------

    public int getLevel() {
        int level = 1;
        int remainingXP = xp;

        while (level < 120) {
            int needed = getXPNeeded(level);
            if (remainingXP < needed) break;

            remainingXP -= needed;
            level++;
        }

        return level;
    }


    public String getDisplayLevel(int level, int prestige) {

        String bracketColor = getBracketColor(prestige);
        String levelColor = "§7";

        if (level >= 0)   levelColor = "§7";
        if (level >= 10)  levelColor = "§9";
        if (level >= 20)  levelColor = "§3";
        if (level >= 30)  levelColor = "§2";
        if (level >= 40)  levelColor = "§a";
        if (level >= 50)  levelColor = "§e";
        if (level >= 60)  levelColor = "§6§l";
        if (level >= 70)  levelColor = "§c§l";
        if (level >= 80)  levelColor = "§4§l";
        if (level >= 90)  levelColor = "§5§l";
        if (level >= 100) levelColor = "§d§l";
        if (level >= 110) levelColor = "§f§l";
        if (level == 120) levelColor = "§b§l";

        return bracketColor + "[" + levelColor + level + bracketColor + "]";
    }

    public String getBracketColor(int prestige) {
        String bracketcolor = "§7";

        if (getPrestige() >= 1) {
            bracketcolor = "§9";
        }

        if (getPrestige() >= 5) {

            bracketcolor = "§e";
        }

        if (getPrestige() >= 10) {

            bracketcolor = "§6";
        }

        if (getPrestige() >= 15) {

            bracketcolor = "§c";
        }

        if (getPrestige() >= 20) {

            bracketcolor = "§5";
        }

        if (getPrestige() >= 25) {

            bracketcolor = "§d";
        }

        if (getPrestige() >= 30) {

            bracketcolor = "§f";
        }


        return bracketcolor;

    }

    public String getPrestigeRomanNumeral(int prestige) {
        if (prestige <= 0) return "0"; // non-prestiged players

        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] numerals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (prestige >= values[i]) {
                prestige -= values[i];
                result.append(numerals[i]);
            }
        }

        return result.toString();
    }

    public int getXP() {
        return xp;
    }

    public void addXP(int amount, Player player) {
        int oldLevel = getLevel();

        xp += amount;

        int newLevel = getLevel();

        if (newLevel > oldLevel) {
            player.sendTitle("§a§lLEVEL UP!", "§e" + oldLevel + " §7→ §a" + newLevel);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1.5f);
        }
    }

    public int getXPNeeded(int level) {
        if (level >= 120) return 0;

        int base;

        if (level < 10) base = 15;
        else if (level < 20) base = 30;
        else if (level < 30) base = 50;
        else if (level < 40) base = 75;
        else if (level < 50) base = 125;
        else if (level < 60) base = 300;
        else if (level < 70) base = 600;
        else if (level < 80) base = 800;
        else if (level < 90) base = 900;
        else if (level < 100) base = 1000;
        else if (level < 110) base = 1200;
        else base = 1500;

        return (int) Math.ceil(base * getPrestigeXPMultiplier());
    }

    public void setRawXP(int amount) {
        this.xp = amount;
    }

    public int getXPLeft() {
        int level = getLevel();
        return getXPNeeded(level) - getXPIntoLevel();
    }


    public int getXPIntoLevel() {
        int remainingXP = xp;

        for (int lvl = 1; lvl < getLevel(); lvl++) {
            remainingXP -= getXPNeeded(lvl);
        }

        return remainingXP;
    }


    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void addGold(int amount) {
        gold += amount;
        if (gold < 0) gold = 0; // safety
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public double getPrestigeXPMultiplier() {
        int p = prestige;

        if (p == 1)  return 1.1;   // +10%
        if (p == 2)  return 1.20;  // +20%
        if (p == 3)  return 1.30;  // +30%
        if (p == 4)  return 1.40;  // +40%
        if (p == 5)  return 1.50;  // +50%
        if (p == 6)  return 1.75;  // +75%
        if (p == 7)  return 2.0;   // +100%
        if (p == 8)  return 2.50;  // +150%
        if (p == 9)  return 3.0;   // +200%
        if (p == 10) return 4.0;   // +300%
        if (p == 11) return 5.0;   // +400%
        if (p == 12) return 6.0;   // +500%
        if (p == 13) return 7.0;   // +600%
        if (p == 14) return 8.0;   // +700%
        if (p == 15) return 9.0;   // +800%
        if (p == 16) return 10.0;  // +900%
        if (p == 17) return 12.0;  // +1100%
        if (p == 18) return 14.0;  // +1300%
        if (p == 19) return 16.0;  // +1500%
        if (p == 20) return 18.0;  // +1900%
        if (p == 21) return 20.0;  // +2300%
        if (p == 22) return 24.0;  // +2700%
        if (p == 23) return 28.0;  // +3100%
        if (p == 24) return 32.0;  // +3900%
        if (p == 25) return 36.0;  // +4000%
        if (p == 26) return 40.0;  // +4900%
        if (p == 27) return 45.0;  // +5900
        if (p == 28) return 50.0; // +9900%
        if (p == 29) return 75.0; // +14900%
        if (p == 30) return 100.0; // +19900%
        if (p >= 31) return 101.0; // +20000% (capped)

        return 1.0; // no prestige
    }


    //title sending method using reflection (for 1.8.8 compatibility)

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            // Times packet
            Object timesPacket = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle")
                    .getConstructor(
                            Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle$EnumTitleAction"),
                            Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent"),
                            int.class, int.class, int.class
                    )
                    .newInstance(
                            Enum.valueOf(
                                    (Class<Enum>) Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle$EnumTitleAction"),
                                    "TIMES"
                            ),
                            null,
                            fadeIn, stay, fadeOut
                    );

            // Title packet
            Object titlePacket = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle")
                    .getConstructor(
                            Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle$EnumTitleAction"),
                            Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent")
                    )
                    .newInstance(
                            Enum.valueOf(
                                    (Class<Enum>) Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle$EnumTitleAction"),
                                    "TITLE"
                            ),
                            Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent$ChatSerializer")
                                    .getMethod("a", String.class)
                                    .invoke(null, "{\"text\":\"" + title + "\"}")
                    );

            // Subtitle packet
            Object subtitlePacket = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle")
                    .getConstructor(
                            Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle$EnumTitleAction"),
                            Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent")
                    )
                    .newInstance(
                            Enum.valueOf(
                                    (Class<Enum>) Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutTitle$EnumTitleAction"),
                                    "SUBTITLE"
                            ),
                            Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent$ChatSerializer")
                                    .getMethod("a", String.class)
                                    .invoke(null, "{\"text\":\"" + subtitle + "\"}")
                    );

            // Send packets
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = handle.getClass().getField("playerConnection").get(handle);

            connection.getClass().getMethod("sendPacket",
                            Class.forName("net.minecraft.server.v1_8_R3.Packet"))
                    .invoke(connection, timesPacket);

            connection.getClass().getMethod("sendPacket",
                            Class.forName("net.minecraft.server.v1_8_R3.Packet"))
                    .invoke(connection, titlePacket);

            connection.getClass().getMethod("sendPacket",
                            Class.forName("net.minecraft.server.v1_8_R3.Packet"))
                    .invoke(connection, subtitlePacket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPerk(int slot, String perkName) {
        // slot is 1, 2, or 3 — convert to array index
        perks[slot - 1] = perkName;
    }

    public String getPerk(int slot) {
        return perks[slot - 1];
    }

    public List<String> getUnlockedPerks() {
        return unlockedPerks;
    }

    public void unlockPerk(String perk) {
        if (!unlockedPerks.contains(perk)) {
            unlockedPerks.add(perk);
        }
    }

    public boolean hasUnlocked(String perk) {
        return unlockedPerks.contains(perk);
    }

    public boolean hasEquipped(String perkId) {
        perkId = perkId.toLowerCase();

        for (String p : perks) {
            if (p != null && p.equalsIgnoreCase(perkId)) {
                return true;
            }
        }
        return false;
    }


    //Megastreaks


    public MegastreakTypes getMegastreak() {
        return megastreak;
    }

    public void setMegastreak(MegastreakTypes type) {
        this.megastreak = type;
    }

    public MegastreakTypes getSelectedMegastreak() {
        return selectedMegastreak;
    }

    public void setSelectedMegastreak(MegastreakTypes type) {
        this.selectedMegastreak = type;
    }


    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addCombo() {
        peruncombo++;
    }

    public int getPeruncombo() {
        return peruncombo;
    }

    public void resetCombo() {
        peruncombo = 0;
    }
}