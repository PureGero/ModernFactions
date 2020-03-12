package com.modernfactions;

import org.bukkit.plugin.java.JavaPlugin;

public class MFConfig {

    private final JavaPlugin plugin;

    public MFConfig(JavaPlugin plugin) {
        this.plugin = plugin;

        loadConfiguration();
    }

    private void loadConfiguration() {
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }

    public int getFactionClaimPrice() {
        return plugin.getConfig().getInt("factions.faction-create-price", 0);
    }

    public int getNumberOfFreeClaims() {
        return plugin.getConfig().getInt("factions.free-claims", 0);
    }

    public int getClaimPrice() {
        return plugin.getConfig().getInt("factions.claim-price", 0);
    }

}
