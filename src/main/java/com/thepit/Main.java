package com.thepit;

import com.thepit.Commands.PerksCommand;
import com.thepit.Commands.PrestigeCommand;
import com.thepit.Commands.SetGoldCommand;
import com.thepit.Commands.SetXPCommand;
import com.thepit.Perks.PerkEffects;
import com.thepit.Prestige.PrestigeManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private PrestigeManager prestigeManager;
    private StatsManager statsManager;



    private static Main instance;

    @Override
    public void onEnable() {
        try {
            // Set instance FIRST
            instance = this;

            saveDefaultConfig();

            // Initialize GUIs

            // Load config
            Config.load();

            prestigeManager = new PrestigeManager();
            statsManager = new StatsManager();



            // Register events
            getServer().getPluginManager().registerEvents(new Events(), this);
            getServer().getPluginManager().registerEvents(new PerkEffects(this), this);

            // Start scoreboard task (updates every 20 ticks)
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


    }

    public static Main getInstance() {
        return instance;
    }



}
