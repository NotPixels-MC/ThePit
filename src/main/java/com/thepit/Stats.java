package com.thepit;

import java.util.UUID;

public class Stats {

    private final UUID uuid;

    private int killstreak = 0;
    private int bestKillstreak = 0;
    private int kills = 0;
    private int deaths = 0;

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

    public int getXP() {
        return xp;
    }

    public void addXP(int amount) {
        if (level >= 120) return; // max level

        xp += amount;

        while (xp >= getXPNeeded(level) && level < 120) {
            xp -= getXPNeeded(level);
            level++;
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

}