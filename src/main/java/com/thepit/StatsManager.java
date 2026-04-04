package com.thepit;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class StatsManager {

    private final File statsFolder;

    public StatsManager(JavaPlugin plugin) {
        this.statsFolder = new File(plugin.getDataFolder(), "stats");
        if (!statsFolder.exists()) statsFolder.mkdirs();
    }

    public void save(Stats stats) {
        File file = new File(statsFolder, stats.getUUID() + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        config.set("xp", stats.getXP());
        config.set("gold", stats.getGold());
        config.set("kills", stats.getKills());
        config.set("deaths", stats.getDeaths());
        config.set("prestige", stats.getPrestige());

        config.set("perks", Arrays.asList(
                stats.getPerk(1),
                stats.getPerk(2),
                stats.getPerk(3)
        ));

        config.set("unlocked-perks", stats.getUnlockedPerks());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stats load(UUID uuid) {
        File file = new File(statsFolder, uuid + ".yml");
        Stats stats = new Stats(uuid);

        if (!file.exists()) return stats;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        stats.setRawXP(config.getInt("xp", 0));
        stats.setGold(config.getInt("gold", 0));
        stats.setKills(config.getInt("kills", 0));
        stats.setDeaths(config.getInt("deaths", 0));
        stats.setPrestige(config.getInt("prestige", 0));

        List<String> perkList = config.getStringList("perks");
        for (int i = 0; i < perkList.size() && i < 3; i++) {
            stats.setPerk(i + 1, perkList.get(i));
        }

        for (String perk : config.getStringList("unlocked-perks")) {
            stats.unlockPerk(perk);
        }

        return stats;
    }
}
