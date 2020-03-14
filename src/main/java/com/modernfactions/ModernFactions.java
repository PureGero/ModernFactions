package com.modernfactions;

import com.modernfactions.commands.MFCommandManager;
import com.modernfactions.listener.MFListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModernFactions extends JavaPlugin {

    private static ModernFactions modernFactions;
    private static MFConfig config;

    public static ModernFactions get() {
        return modernFactions;
    }

    public static MFConfig getMFConfig() {
        return config;
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

    }
}
