package com.thepit.Utils;

public class XPUtils {

    public static int getStreakXP(int streak, int sweatyLevel) {

        int maxXP = 30;
        int streakXPBonus = 0;

        if (sweatyLevel >= 2) {
            maxXP = ((50 * sweatyLevel) + 30);
        }

        if (streak == 1 || streak == 2 || streak == 3) {
            streakXPBonus += 4;
        }

        if (streak <= 3 && streak <= 4) {
            streakXPBonus += 3;
        }

        if (streak >= 5 && streak <= 19) {
            streakXPBonus += 10;
        }

        if (streak >= 20) {
            int HighStreakBonus = streak;

            HighStreakBonus /= 10;

            HighStreakBonus *= 3;

            if (HighStreakBonus > maxXP) {
                HighStreakBonus = maxXP;
            }

            streakXPBonus += HighStreakBonus;

        }

        if (sweatyLevel >= 1) streakXPBonus = (int)(streakXPBonus * (0.2 * sweatyLevel + 1));

        return streakXPBonus;
    }
}
