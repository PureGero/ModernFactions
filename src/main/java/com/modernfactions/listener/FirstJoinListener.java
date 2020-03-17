package com.modernfactions.listener;

import com.modernfactions.ModernFactions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FirstJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            String welcomeMessage = ModernFactions.getMFConfig().getWelcomeMessage();

            if (welcomeMessage != null && welcomeMessage.length() > 0) {
                if (welcomeMessage.contains("%s")) {
                    welcomeMessage = String.format(welcomeMessage, e.getPlayer().getName());
                }

                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + welcomeMessage);
            }
        }
    }

}
