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

        registerVotifierListener();
    }

    private void registerVotifierListener() {
        try {
            if (Class.forName("com.vexsoftware.votifier.model.VotifierEvent") != null) {
                plugin.getServer().getPluginManager().registerEvents(new VotifierListener(), plugin);
                return;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        plugin.getLogger().info("Votifier is not installed, votes have been disabled");
    }
}
