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

    public String getMysqlHost() {
        return plugin.getConfig().getString("factions.mysql.host", "localhost");
    }

    public int getMysqlPort() {
        return plugin.getConfig().getInt("factions.mysql.port", 3306);
    }

    public String getMysqlDatabase() {
        return plugin.getConfig().getString("factions.mysql.db", "factions");
    }

    public String getMysqlUser() {
        return plugin.getConfig().getString("factions.mysql.user", "root");
    }

    public String getMysqlPassword() {
        return plugin.getConfig().getString("factions.mysql.password", "");
    }

}
