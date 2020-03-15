package com.modernfactions.listener;

import com.modernfactions.data.MFDatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.SQLException;

public class ChatListener implements Listener {

    @EventHandler
    public void appendFactionPrefix(AsyncPlayerChatEvent e) {
        try {
            String factionName = MFDatabaseManager.getDatabase().getFactionNameFromMember(e.getPlayer().getUniqueId());

            if (factionName != null) {
                e.setFormat(e.getFormat().replace("%1$s", "[" + factionName + "] %1$s"));
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}
