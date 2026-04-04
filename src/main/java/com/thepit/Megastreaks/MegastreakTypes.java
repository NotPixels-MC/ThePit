package com.thepit.Megastreaks;

public enum MegastreakTypes {

    OVERDRIVE(50, 50, 100, 0.0), //require kills, +50%XP, +100%GOLD, 0.0% mysticchance.
    BEASTMODE(50, 50, 75, 0.0),
    HIGHLANDER(50, 0, 50, 0.0),
    MAGNUMOPUS(50, 0, 0, 0.0),
    TOTHEMOON(100, 20, 0, 0.0),
    UBERSTREAK(100, 0, 0, 50.0);

    private final int requiredKills;
    private final int extraXP;
    private final int extraGOLD;
    private final double extraMysticChance;

    MegastreakTypes(int requiredKills, int extraXP, int extraGOLD, double extraMysticChance) {
        this.requiredKills = requiredKills;
        this.extraXP = extraXP;
        this.extraGOLD = extraGOLD;
        this.extraMysticChance = extraMysticChance;
    }

    public int getRequiredKills() {
        return requiredKills;
    }

    public int getExtraXP() {
        return extraXP;
    }

    public int getExtraGOLD() {
        return extraGOLD;
    }

    public double getExtraMysticChance() {
        return extraMysticChance;
    }
}
