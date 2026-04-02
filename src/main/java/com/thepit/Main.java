package com.thepit;

import com.thepit.Commands.PerksCommand;
import com.thepit.Commands.PrestigeCommand;
import com.thepit.Commands.SetGoldCommand;
import com.thepit.Commands.SetXPCommand;
import com.thepit.Perks.PerkEffects;
import com.thepit.Prestige.PrestigeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private PrestigeManager prestigeManager;
    private StatsManager statsManager;

    private static Main instance;

    @Override
    public void onEnable() {
        try {
            instance = this;

            saveDefaultConfig();

            Config.load();

            prestigeManager = new PrestigeManager();
            statsManager = new StatsManager();

            // Initialize NPC systems


            // Create a bot
            Location botSpawn = getBotSpawn();


            // Register events
            getServer().getPluginManager().registerEvents(new Events(), this);
            getServer().getPluginManager().registerEvents(new PerkEffects(this), this);

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

    public Location getBotSpawn() {
        FileConfiguration config = getConfig();

        double x = config.getDouble("bot-spawn.x");
        double y = config.getDouble("bot-spawn.y");
        double z = config.getDouble("bot-spawn.z");
        float yaw = (float) config.getDouble("bot-spawn.yaw");
        float pitch = (float) config.getDouble("bot-spawn.pitch");
        String worldName = config.getString("bot-spawn.world");

        World world = Bukkit.getWorld(worldName);

        return new Location(world, x, y, z, yaw, pitch);
    }


}