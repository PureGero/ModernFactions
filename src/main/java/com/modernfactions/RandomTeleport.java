package com.modernfactions;

import org.bukkit.*;
import org.bukkit.entity.Player;

/**
 * Randomly teleport a player to somewhere in the world
 */
public class RandomTeleport {

    public static Location getRandomTeleportLocation() {
        return getRandomTeleportLocation(Bukkit.getWorlds().get(0));
    }

    public static Location getRandomTeleportLocation(World world) {
        WorldBorder border = world.getWorldBorder();
        Location borderCenter = border.getCenter().clone();
        double borderSize = border.getSize();

        Location teleportTo = borderCenter.add(Math.random() * borderSize - borderSize/2,
                                               0,
                                               Math.random() * borderSize - borderSize/2);

        teleportTo.getChunk().load();

        teleportTo.setX(teleportTo.getBlockX() + 0.5);
        teleportTo.setY(world.getHighestBlockYAt(teleportTo) + 1);
        teleportTo.setZ(teleportTo.getBlockZ() + 0.5);

        Material blockUnderneath = teleportTo.getBlock().getRelative(0, -1, 0).getType();

        if (teleportTo.getBlockY() > 60 &&
                blockUnderneath != Material.LAVA &&
                blockUnderneath != Material.WATER) {
            // Seems like a good place to teleport them
            return teleportTo;
        } else {
            // Bad place to teleport them, let's try again
            return getRandomTeleportLocation(world);
        }
    }

}
