package com.modernfactions;

import com.modernfactions.commands.MFCommandManager;
import com.modernfactions.data.MFDatabaseManager;
import com.modernfactions.listener.MFListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModernFactions extends JavaPlugin {

    private static ModernFactions modernFactions;
    private static MFConfig config;
    private static MFEconomyManager economy;

    public static ModernFactions get() {
        return modernFactions;
    }

    public static MFConfig getMFConfig() {
        return config;
    }

    public static MFEconomyManager getEconomy() {
        return economy;
    }

    @Override
    public void onEnable() {
        modernFactions = this;

        // Load the config
        config = new MFConfig(this);

        // Initialise managers
        new MFTranslationManager(this);
        new MFCommandManager(this);
        new MFListenerManager(this);
        new MFDatabaseManager(this);
        economy = new MFEconomyManager(this);

    }
}
