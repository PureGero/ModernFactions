package com.modernfactions.listener;

import com.modernfactions.RandomTeleport;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * The listener that listens for when it should randomly teleport people
 */
public class RandomTeleportListener implements Listener {

    @EventHandler
    public void onPlayerFirstJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (!player.hasPlayedBefore()) {

            player.teleport(RandomTeleport.getRandomTeleportLocation());

        }
    }

    @EventHandler
    public void onPlayerRespawnNotAtBed(PlayerRespawnEvent e) {
        if (!e.isBedSpawn()) {

            e.setRespawnLocation(RandomTeleport.getRandomTeleportLocation());

        }
    }
}
