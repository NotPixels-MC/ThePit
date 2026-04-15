package com.thepit;

import com.thepit.Commands.*;
import com.thepit.Megastreaks.MegastreakMenuListener;
import com.thepit.Mystics.MysticSounds;
import com.thepit.Perks.PerkEffects;
import com.thepit.Prestige.PrestigeManager;
import com.thepit.Utils.Rank;
import com.thepit.Utils.RankManager;
import com.thepit.tablist.TablistManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {

    private PrestigeManager prestigeManager;
    private StatsManager statsManager;

    private Map<UUID, Stats> statsMap = new HashMap<>();

    private static Main instance;

    @Override
    public void onEnable() {
        try {
            instance = this;

            saveDefaultConfig();
            Config.load();
            RankManager.loadRanks(this);

            prestigeManager = new PrestigeManager();
            MysticSounds sounds = new MysticSounds(this);
            statsManager = new StatsManager(this);

            // Register events
            getServer().getPluginManager().registerEvents(new Events(), this);
            getServer().getPluginManager().registerEvents(new PerkEffects(this), this);
            getServer().getPluginManager().registerEvents(new MegastreakMenuListener(), this);


            // ⭐ REGISTER STATS LOADING/SAVING ⭐
            registerStatsEvents();

            ScoreboardTask.start();

            getLogger().info("The Pit plugin enabled!");
        } catch (Exception e) {
            getLogger().severe("Failed to enable ThePit plugin!");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }

        UpgradesMenu upgradesMenu = new UpgradesMenu();

        getCommand("perks").setExecutor(new PerksCommand(upgradesMenu));
        getCommand("setxp").setExecutor(new SetXPCommand());
        getCommand("setgold").setExecutor(new SetGoldCommand());
        getCommand("prestige").setExecutor(
                new PrestigeCommand(prestigeManager, statsManager)
        );
        getCommand("setenchant").setExecutor(new SetEnchantCommand());
        getCommand("addlives").setExecutor(new AddLivesCommand());
        getCommand("setunbreakable").setExecutor(new setUnbreakableCommand());

        TablistManager.start();

    }

    public static Main getInstance() {
        return instance;
    }

    public Stats getStats(UUID uuid) {
        return statsMap.get(uuid);
    }

    private void registerStatsEvents() {
        getServer().getPluginManager().registerEvents(new Listener() {

            @EventHandler
            public void onJoin(PlayerJoinEvent e) {
                UUID uuid = e.getPlayer().getUniqueId();

                Stats stats = statsManager.load(uuid);
                statsMap.put(uuid, stats);

                System.out.println("Loaded stats for " + uuid);
            }

            @EventHandler
            public void onQuit(PlayerQuitEvent e) {
                UUID uuid = e.getPlayer().getUniqueId();

                Stats stats = statsMap.get(uuid);
                if (stats != null) {
                    statsManager.save(stats);
                    System.out.println("Saved stats for " + uuid);
                }

                statsMap.remove(uuid);
            }

        }, this);
    }

    public static Map<String, Rank> ranks = new HashMap<>();

}
