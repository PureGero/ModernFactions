package com.modernfactions.data;

import com.modernfactions.ModernFactions;

import java.sql.*;
import java.util.UUID;

public class MysqlDatabase implements IDatabase {
    private static final String tables =
            "CREATE TABLE IF NOT EXISTS faction_names (\n" +
                    "fuuid CHAR(36) PRIMARY KEY,\n" +
                    "name VARCHAR(32) UNIQUE\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS faction_members (\n" +
                    "uuid CHAR(36) PRIMARY KEY,\n" +
                    "fuuid CHAR(36) NOT NULL,\n" +
                    "role TINYINT NOT NULL DEFAULT 0\n" +
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

    private PreparedStatement preparedStatement(String sql, Object... args) throws SQLException {
        PreparedStatement statement = connect().prepareStatement(sql);

        for (int i = 0; i < args.length; i++) {
            statement.setObject(i + 1, args[i]);
        }

        return statement;
    }

    private boolean execute(String sql, Object... args) throws SQLException {
        return preparedStatement(sql, args).execute();
    }

    private ResultSet executeQuery(String sql, Object... args) throws SQLException {
        return preparedStatement(sql, args).executeQuery();
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

    @Override
    public void setFactionName(UUID fuuid, String name) throws SQLException {
        execute(
                "INSERT INTO faction_names (fuuid, name) VALUES (?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE name = ?",
                fuuid.toString(),
                name,
                name
        );
    }

    @Override
    public String getFactionName(UUID fuuid) throws SQLException {
        ResultSet set = executeQuery(
                "SELECT name FROM faction_names WHERE fuuid = ?",
                fuuid.toString()
        );

        if (set.next()) {
            return set.getString("name");
        } else {
            return null;
        }
    }

    @Override
    public void addFactionMember(UUID fuuid, UUID uuid) throws SQLException {
        execute(
                "INSERT INTO faction_members (fuuid, uuid) VALUES (?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE fuuid = ?",
                fuuid.toString(),
                uuid.toString(),
                fuuid.toString()
        );
    }

    @Override
    public void setFactionMemberRole(UUID fuuid, UUID uuid, int role) throws SQLException {
        execute(
                "INSERT INTO faction_members (fuuid, uuid, role) VALUES (?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE fuuid = ?, role = ?",
                fuuid.toString(),
                uuid.toString(),
                role,
                fuuid.toString(),
                role
        );
    }
}
