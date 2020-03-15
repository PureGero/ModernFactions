package com.modernfactions;

import com.modernfactions.data.RegionFileCache;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * One claim is one chunk, one sector is 32x32 chunks, one region is 32x32 sectors.
 * A sector is 8KB in storage, a filled region can be as much as 8MB in storage
 */
public class MFClaimManager implements Runnable {
    private static MFClaimManager claimManager;

    public static MFClaimManager get() {
        return claimManager;
    }

    public static UUID getClaim(Location location) {
        return get().getClaim(location.getWorld().getName(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public static void setClaim(Location location, UUID fuuid) {
        get().setClaim(location.getWorld().getName(), location.getBlockX() >> 4, location.getBlockZ() >> 4, fuuid);
    }

    private final JavaPlugin plugin;

    private final List<ClaimSector> sectors = new ArrayList<>();

    public MFClaimManager(JavaPlugin plugin) {
        claimManager = this;

        this.plugin = plugin;

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 60*20L, 60*20L);
    }

    public UUID getClaim(String world, int cx, int cz) {
        return getClaimSector(world, cx >> 5, cz >> 5).fuuid[cx & 0x1F][cz & 0x1F];
    }

    public void setClaim(String world, int cx, int cz, UUID fuuid) {
        ClaimSector sector = getClaimSector(world, cx >> 5, cz >> 5);
        sector.fuuid[cx & 0x1F][cz & 0x1F] = fuuid;
        sector.needsSaving = true;
    }

    /**
     * @param world Name of the world directory
     * @param sx x >> 9
     * @param sz z >> 9
     * @return
     */
    private ClaimSector getClaimSector(String world, int sx, int sz) {
        synchronized (sectors) {
            for (ClaimSector sector : sectors) {
                if (sector.world.equals(world) && sector.sx == sx && sector.sz == sz) {
                    return sector;
                }
            }
        }

        ClaimSector sector = loadClaimSector(world, sx, sz);

        synchronized (sectors) {
            sectors.add(sector);
        }

        return sector;
    }

    private ClaimSector loadClaimSector(String world, int sx, int sz) {
        ClaimSector sector = new ClaimSector(world, sx, sz);

        try (DataInputStream in = RegionFileCache.getChunkInputStream(plugin.getServer().getWorld(world).getWorldFolder(), sx, sz)) {
            if (in != null) {
                for (int i = 0; i < sector.fuuid.length; i++) {
                    for (int j = 0; j < sector.fuuid[i].length; j++) {
                        long mostSignificant = in.readLong();
                        long leastSignificant = in.readLong();
                        if (mostSignificant != 0 || leastSignificant != 0) {
                            sector.fuuid[i][j] = new UUID(mostSignificant, leastSignificant);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sector;
    }

    private void saveClaimSector(ClaimSector sector) {
        try (DataOutputStream out = RegionFileCache.getChunkOutputStream(plugin.getServer().getWorld(sector.world).getWorldFolder(), sector.sx, sector.sz)) {
            if (out != null) {

                for (int i = 0; i < sector.fuuid.length; i++) {
                    for (int j = 0; j < sector.fuuid[i].length; j++) {
                        long mostSignificant = 0;
                        long leastSignificant = 0;
                        if (sector.fuuid[i][j] != null) {
                            mostSignificant = sector.fuuid[i][j].getMostSignificantBits();
                            leastSignificant = sector.fuuid[i][j].getLeastSignificantBits();
                        }
                        out.writeLong(mostSignificant);
                        out.writeLong(leastSignificant);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves any claim sectors that need saving
     */
    @Override
    public void run() {
        List<ClaimSector> toSave = new ArrayList<>();

        synchronized (sectors) {
            for (ClaimSector sector : sectors) {
                if (sector.needsSaving) {
                    sector.needsSaving = false;
                    toSave.add(sector);
                }
            }
        }

        for (ClaimSector sector : toSave) {
            saveClaimSector(sector);
        }
    }
}
