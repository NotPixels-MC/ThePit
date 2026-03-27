package com.thepit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private static PerkSlotsGUI perkSlotsGUI;

    @Override
    public void onEnable() {
        try {
            // Set instance FIRST
            instance = this;

            // Initialize GUIs
            perkSlotsGUI = new PerkSlotsGUI();

            // Load config
            Config.load();

            // Register events
            getServer().getPluginManager().registerEvents(new Events(), this);
            getServer().getPluginManager().registerEvents(new PerkSlotsClickListener(perkSlotsGUI), this);
            getServer().getPluginManager().registerEvents(new PerkSelectionClickListener(perkSlotsGUI), this);

            // Register commands
            getCommand("perkslots").setExecutor(new PerkSlotsCommand(perkSlotsGUI));

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
