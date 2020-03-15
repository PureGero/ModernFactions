package com.modernfactions.data;

import com.modernfactions.ModernFactions;
import org.bukkit.Location;

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
            ");\n" +
            "CREATE TABLE IF NOT EXISTS faction_referrals (\n" +
                    "uuid CHAR(36) PRIMARY KEY,\n" +
                    "other_uuid CHAR(36)\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS faction_homes (\n" +
                    "fuuid CHAR(36) PRIMARY KEY,\n" +
                    "wuuid CHAR(36) NOT NULL,\n" +
                    "x INT NOT NULL,\n" +
                    "y INT NOT NULL,\n" +
                    "z INT NOT NULL\n" +
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

    @Override
    public String getFactionNameFromMember(UUID uuid) throws SQLException {
        ResultSet set = executeQuery(
                "SELECT name FROM faction_names, faction_members\n" +
                "WHERE faction_names.fuuid = faction_members.fuuid AND uuid = ?",
                uuid.toString()
        );

        if (set.next()) {
            return set.getString("name");
        } else {
            return null;
        }
    }

    @Override
    public boolean hasReferredAPlayer(UUID uuid) throws SQLException {
        ResultSet set = executeQuery(
                "SELECT other_uuid FROM faction_referrals WHERE uuid = ?",
                uuid.toString()
        );

        if (set.next()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void referAPlayer(UUID uuid, UUID to) throws SQLException {
        execute(
                "INSERT INTO faction_referrals (uuid, other_uuid) VALUES (?, ?)",
                uuid.toString(),
                to.toString()
        );
    }

    @Override
    public int getFactionClaims(UUID fuuid) throws SQLException {
        ResultSet set = executeQuery(
                "SELECT SUM(claims) AS claims_total FROM faction_claims WHERE fuuid = ?",
                fuuid.toString()
        );

        if (set.next()) {
            return set.getInt("claims_total");
        } else {
            return 0;
        }
    }

    @Override
    public void increaseBy1FactionClaim(UUID fuuid, UUID wuuid) throws SQLException {
        execute(
                "INSERT INTO faction_claims (fuuid, wuuid, claims) VALUES (?, ?, 1)\n" +
                        "ON DUPLICATE KEY UPDATE claims = claims + 1",
                fuuid.toString(),
                wuuid.toString()
        );
    }

    @Override
    public int getFactionMemberCount(UUID fuuid) throws SQLException {
        ResultSet set = executeQuery(
                "SELECT COUNT(*) AS member_count FROM faction_members WHERE fuuid = ?",
                fuuid.toString()
        );

        if (set.next()) {
            return set.getInt("member_count");
        } else {
            return 0;
        }
    }

    @Override
    public UUID getFaction(UUID uuid) throws SQLException {
        ResultSet set = executeQuery(
                "SELECT fuuid FROM faction_members WHERE uuid = ?",
                uuid.toString()
        );

        if (set.next()) {
            return UUID.fromString(set.getString("fuuid"));
        } else {
            return null;
        }
    }

    @Override
    public BlockPos getFactionHome(UUID fuuid) throws SQLException {
        ResultSet set = executeQuery(
                "SELECT wuuid, x, y, z FROM faction_homes WHERE fuuid = ?",
                fuuid.toString()
        );

        if (set.next()) {
            return new BlockPos(UUID.fromString(set.getString("wuuid")), set.getInt("x"), set.getInt("y"), set.getInt("z"));
        } else {
            return null;
        }
    }

    @Override
    public void setFactionHome(UUID fuuid, Location location) throws SQLException {
        String wuuid = location.getWorld().getUID().toString();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        execute(
                "INSERT INTO faction_homes (fuuid, wuuid, x, y, z) VALUES (?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE wuuid = ?, x = ?, y = ?, z = ?",
                fuuid.toString(),
                wuuid,
                x,
                y,
                z,
                wuuid,
                x,
                y,
                z
        );
    }
}
