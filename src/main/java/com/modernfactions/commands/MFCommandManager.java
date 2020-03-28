package com.modernfactions.commands;

import org.bukkit.plugin.java.JavaPlugin;

public class MFCommandManager {

    private final JavaPlugin plugin;

    public MFCommandManager(JavaPlugin plugin) {
        this.plugin = plugin;

        registerCommands();

        registerTabCompleters();
    }

    private void registerCommands() {
        plugin.getServer().getPluginCommand("testrandomteleport").setExecutor(new TestRandomTeleport());
        plugin.getServer().getPluginCommand("modernfactions").setExecutor(new ModernFactionsCommand());
        plugin.getServer().getPluginCommand("modernfactionsadmin").setExecutor(new ModernFactionsAdminCommand());
        plugin.getServer().getPluginCommand("refer").setExecutor(new ReferCommand());
        plugin.getServer().getPluginCommand("territory").setExecutor(new TerritoryCommand());
        plugin.getServer().getPluginCommand("discord").setExecutor(new DiscordCommand());
        plugin.getServer().getPluginCommand("store").setExecutor(new StoreCommand());
    }

    private void registerTabCompleters() {
        plugin.getServer().getPluginCommand("modernfactions").setTabCompleter(new ModernFactionsTabCompleter());
        plugin.getServer().getPluginCommand("modernfactionsadmin").setTabCompleter(new ModernFactionsAdminTabCompleter());
    }

}
