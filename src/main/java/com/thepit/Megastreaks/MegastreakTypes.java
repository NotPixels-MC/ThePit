package com.thepit.Megastreaks;

public enum MegastreakTypes {

    OVERDRIVE(50, 50, 100); //require kills, +50 percent of xp, +100 percent of gold add desc to gui and hardcode it later.

    private final int requiredKills;
    private final int extraXP;
    private final int extraGOLD;

    MegastreakTypes(int requiredKills, int extraXP, int extraGOLD) {
        this.requiredKills = requiredKills;
        this.extraXP = extraXP;
        this.extraGOLD = extraGOLD;
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
}
