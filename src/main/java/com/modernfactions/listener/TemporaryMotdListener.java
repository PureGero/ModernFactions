package com.modernfactions.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class TemporaryMotdListener implements Listener {

    private static String motd = null;

    public static void setMotd(String motd) {
        TemporaryMotdListener.motd = motd;
    }

    public static void clearMotd() {
        motd = null;
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent e) {
        if (motd != null) {
            e.setMotd(motd);
        }
    }

}
