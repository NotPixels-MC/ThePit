package com.thepit;

import java.util.HashMap;
import java.util.UUID;

public class StatsManager {

    private static final HashMap<UUID, Stats> statsMap = new HashMap<>();

    public static Stats getStats(UUID uuid) {
        return statsMap.computeIfAbsent(uuid, Stats::new);
    }
}