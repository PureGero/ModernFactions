package com.modernfactions.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class BlockPos {
    private UUID worldUuid;
    private int x;
    private int y;
    private int z;

    public BlockPos(UUID world, int x, int y, int z) {
        this.worldUuid = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public UUID getWorld() {
        return worldUuid;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Location toLocation() {
        World world = Bukkit.getWorld(worldUuid);

        if (world == null) {
            return null;
        }

        return new Location(world, x + 0.5, y, z + 0.5);
    }
}
