package com.modernfactions.data;

import com.modernfactions.ModernFactions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlDatabase implements IDatabase {
    private static final String tables =
            "CREATE TABLE IF NOT EXISTS faction_names (\n" +
                    "fuuid CHAR(36) PRIMARY KEY,\n" +
                    "name VARCHAR(32) UNIQUE\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS faction_members (\n" +
                    "uuid CHAR(36) PRIMARY KEY,\n" +
                    "fuuid CHAR(36) NOT NULL,\n" +
                    "role INT NOT NULL DEFAULT 0\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS faction_claims (\n" +
                    "fuuid CHAR(36),\n" +
                    "wuuid CHAR(36),\n" +
                    "claims INT NOT NULL DEFAULT 0,\n" +
                    "PRIMARY KEY (fuuid, wuuid)\n" +
            ")";

    private Connection connection = null;

    public MysqlDatabase() {
        setupTables();
    }

    private Connection connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    String.format(
                        "jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false",
                        ModernFactions.getMFConfig().getMysqlHost(),
                        ModernFactions.getMFConfig().getMysqlPort(),
                        ModernFactions.getMFConfig().getMysqlDatabase(),
                        ModernFactions.getMFConfig().getMysqlUser(),
                        ModernFactions.getMFConfig().getMysqlPassword()
                    )
            );
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
    }

    private void setupTables() {
        for (String table : tables.split(";")) {
            if (table.length() == 0) {
                continue;
            }

            try (PreparedStatement statement = connect().prepareStatement(table)) {
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
