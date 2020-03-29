package com.modernfactions.listener;

import org.bukkit.plugin.java.JavaPlugin;

public class MFListenerManager {

    private final JavaPlugin plugin;

    public MFListenerManager(JavaPlugin plugin) {
        this.plugin = plugin;

        registerListener();
    }

    private void registerListener() {
        plugin.getServer().getPluginManager().registerEvents(new RandomTeleportListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ChatListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ProtectionListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new FirstJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new TemporaryMotdListener(), plugin);
    }
}
