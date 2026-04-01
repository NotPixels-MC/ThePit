package com.thepit;

import org.bukkit.entity.Player;

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

    private int level = 1;
    private int xp = 0;
    private int gold = 0;
    private int prestige = 0;

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
        if (level >= 60)  levelColor = "§6";
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


    public int getXP() {
        return xp;
    }

    public void addXP(int amount, Player player) {
        if (level >= 120) return; // max level

        xp += amount;

        int oldLevel = level;

        while (xp >= getXPNeeded(level) && level < 120) {
            xp -= getXPNeeded(level);
            level++;
        }

        if (level > oldLevel) {
            sendTitle(player, "§a§lLEVEL UP!", "§e" + oldLevel + " §7→ §a" + level, 10, 40, 10);
        }

    }

    public void setXP(int amount, Player player) {
        if (amount < 0) amount = 0;

        this.xp = amount;

        int oldLevel = level;

        // Recalculate level based on XP
        while (xp >= getXPNeeded(level) && level < 120) {
            xp -= getXPNeeded(level);
            level++;
        }

        // If XP is too low for current level, level down
        while (level > 0 && xp < 0) {
            level--;
            xp += getXPNeeded(level);
        }

        if (level > oldLevel) {
            sendTitle(player, "§a§lLEVEL UP!", "§e" + oldLevel + " §7→ §a" + level, 10, 40, 10);
        }
    }

    public int getXPNeeded(int level) {
        if (level >= 120) return 0;

        if (level < 10) return 15;
        if (level < 20) return 30;
        if (level < 30) return 50;
        if (level < 40) return 75;
        if (level < 50) return 125;
        if (level < 60) return 300;
        if (level < 70) return 600;
        if (level < 80) return 800;
        if (level < 90) return 900;
        if (level < 100) return 1000;
        if (level < 110) return 1200;

        return 1500; // 110–119
    }

    public int getXPLeft() {
        if (level >= 120) return 0;
        return getXPNeeded(level) - xp;
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

}