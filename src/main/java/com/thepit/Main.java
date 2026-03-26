package com.thepit;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable(){
        try {
            // Set instance FIRST
            instance = this;

            // Load config
            Config.load();

            // Register events
            getServer().getPluginManager().registerEvents(new Events(), this);

            // Start scoreboard task (updates every 20 ticks)
            ScoreboardTask.start();

            getLogger().info("The Pit plugin enabled!");
        } catch (Exception e) {
            getLogger().severe("Failed to enable ThePit plugin!");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static Main getInstance() {
        return instance;
    }

}
