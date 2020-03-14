package com.modernfactions.data;

import org.bukkit.plugin.java.JavaPlugin;

public class MFDatabaseManager {

    private static MFDatabaseManager databaseManager;

    public static MFDatabaseManager get() {
        return databaseManager;
    }

    public static IDatabase getDatabase() {
        return get().database;
    }

    private final JavaPlugin plugin;
    private final IDatabase database;

    public MFDatabaseManager(JavaPlugin plugin) {
        databaseManager = this;

        this.plugin = plugin;

        database = new MysqlDatabase();
    }
}
