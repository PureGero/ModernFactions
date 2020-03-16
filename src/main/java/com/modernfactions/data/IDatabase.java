package com.modernfactions.data;

import org.bukkit.Location;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface IDatabase {

    void setFactionName(UUID fuuid, String name) throws SQLException;

    String getFactionName(UUID fuuid) throws SQLException;

    UUID getFactionByName(String name) throws SQLException;

    void addFactionMember(UUID fuuid, UUID uuid) throws SQLException;

    List<UUID> getFactionMembers(UUID fuuid) throws SQLException;

    void setFactionMemberRole(UUID fuuid, UUID uuid, int role) throws SQLException;

    String getFactionNameFromMember(UUID uuid) throws SQLException;

    boolean hasReferredAPlayer(UUID uuid) throws SQLException;

    void referAPlayer(UUID uuid, UUID to) throws SQLException;

    int getFactionClaims(UUID fuuid) throws SQLException;

    void increaseBy1FactionClaim(UUID fuuid, UUID wuuid) throws SQLException;

    int getFactionMemberCount(UUID fuuid) throws SQLException;

    UUID getFaction(UUID uuid) throws SQLException;

    BlockPos getFactionHome(UUID fuuid) throws SQLException;

    void setFactionHome(UUID fuuid, Location location) throws SQLException;

    /** Checks if fuuid has added added_fuuid (added_fuuid has access to fuuid's stuff) */
    boolean areAllies(UUID fuuid, UUID added_fuuid) throws SQLException;

    /** Gives added_fuuid access to fuuid's stuff */
    void addAlly(UUID fuuid, UUID added_fuuid) throws SQLException;

    /** Get a list of which factions have access to fuuid's stuff */
    List<UUID> getAddedAllies(UUID fuuid) throws SQLException;

    /** Get a list of names of which factions have access to fuuid's stuff */
    List<String> getAddedAlliesNames(UUID fuuid) throws SQLException;

    /** Get a list of which factions fuuid has access to their stuff */
    List<UUID> getAddedMe(UUID added_fuuid) throws SQLException;

    /** Get a list of names of which factions fuuid has access to their stuff */
    List<String> getAddedMeNames(UUID added_fuuid) throws SQLException;

    /** Revokes added_fuuid from accessing fuuid's stuff */
    void removeAlly(UUID fuuid, UUID added_fuuid) throws SQLException;
}
