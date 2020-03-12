package com.modernfactions;

import org.bukkit.plugin.java.JavaPlugin;

public final class ModernFactions extends JavaPlugin {

    private static MFConfig config;

    public static MFConfig getMFConfig() {
        return config;
    }

    @Override
    public void onEnable() {

        config = new MFConfig(this);

    }
}
