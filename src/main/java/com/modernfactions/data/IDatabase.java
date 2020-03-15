package com.modernfactions.data;

import java.sql.SQLException;
import java.util.UUID;

public interface IDatabase {

    void setFactionName(UUID fuuid, String name) throws SQLException;

    String getFactionName(UUID fuuid) throws SQLException;

    void addFactionMember(UUID fuuid, UUID uuid) throws SQLException;

    void setFactionMemberRole(UUID fuuid, UUID uuid, int role) throws SQLException;

    String getFactionNameFromMember(UUID uuid) throws SQLException;

    boolean hasReferredAPlayer(UUID uuid) throws SQLException;

    void referAPlayer(UUID uuid, UUID to) throws SQLException;

}
