package com.modernfactions.commands;

import org.bukkit.plugin.java.JavaPlugin;

public class MFCommandManager {

    private final JavaPlugin plugin;

    public MFCommandManager(JavaPlugin plugin) {
        this.plugin = plugin;

        registerCommands();
    }

    private void registerCommands() {
        plugin.getServer().getPluginCommand("testrandomteleport").setExecutor(new TestRandomTeleport());
        plugin.getServer().getPluginCommand("modernfactions").setExecutor(new ModernFactionsCommand());
        plugin.getServer().getPluginCommand("refer").setExecutor(new ReferCommand());
        plugin.getServer().getPluginCommand("territory").setExecutor(new TerritoryCommand());
    }

}
