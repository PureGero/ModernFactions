package com.modernfactions.data;

import com.modernfactions.ModernFactions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlDatabase implements IDatabase {
    private static final String tables =
            "CREATE TABLE IF NOT EXISTS faction_names {" +
                    "fuuid CHAR(36) PRIMARY KEY," +
                    "name VARCHAR(32) UNIQUE" +
            "}" +
            "CREATE TABLE IF NOT EXISTS faction_members {" +
                    "uuid CHAR(36) PRIMARY KEY," +
                    "fuuid CHAR(36) NOT NULL," +
                    "role INT NOT NULL DEFAULT 0" +
            "}" +
            "CREATE TABLE IF NOT EXISTS faction_claims {" +
                    "fuuid CHAR(36)," +
                    "wuuid CHAR(36)," +
                    "claims INT NOT NULL DEFAULT 0" +
                    "PRIMARY KEY (fuuid, wuuid)" +
            "}";

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
                        "jdbc:mysql://%s:%d/%s?user=%s&password=%s",
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
        try (PreparedStatement statement = connect().prepareStatement(tables)) {
            statement.execute();
        } catch (SQLException e) {

        }
    }
}
