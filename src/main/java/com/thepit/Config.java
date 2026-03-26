package com.thepit;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    private static FileConfiguration config;

    public static void load() {
        config = Main.getInstance().getConfig();
        Main.getInstance().saveDefaultConfig();
    }

    public static String getServerIP() {
        if (config == null) {
            return "play.example.com";
        }
        return config.getString("server-ip", "play.example.com");
    }
}

