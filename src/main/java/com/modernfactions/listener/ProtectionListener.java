package com.modernfactions.listener;

import com.modernfactions.MF;
import com.modernfactions.MFClaimManager;
import com.modernfactions.ModernFactions;
import com.modernfactions.data.MFDatabaseManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ProtectionListener implements Listener {

    public void sendAccessDeniedMessage(Player player, UUID owner_fuuid) {
        Bukkit.getScheduler().runTaskAsynchronously(ModernFactions.get(), () -> {
            try {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        MF.getMessage(player, ChatColor.RED, "claim.accessdenied",
                                MFDatabaseManager.getDatabase().getFactionName(owner_fuuid)));
            } catch (SQLException e) {
                e.printStackTrace();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        MF.getMessage(player, ChatColor.RED, "claim.accessdenied", "SQL DATABASE ERROR"));
            }
        });
    }

    /**
     * Check if a player is allowed to modify a certain location
     * @param event A cancellable event
     * @param player The player trying to modify something
     * @param location The location the player is modifying
     */
    private void onLocationModified(Cancellable event, Player player, Location location) {
        UUID claim_fuuid = MFClaimManager.getClaim(location);

        if (claim_fuuid != null) {

            try {
                UUID fuuid = MFDatabaseManager.getDatabase().getFaction(player.getUniqueId());

                if (claim_fuuid.equals(fuuid)) {
                    return;
                }

                List<UUID> allies = MFDatabaseManager.getDatabase().getAddedMe(fuuid);

                for (UUID ally : allies) {
                    if (claim_fuuid.equals(ally)) {
                        return;
                    }
                }

                sendAccessDeniedMessage(player, claim_fuuid);
                event.setCancelled(true);

            } catch (SQLException e1) {
                e1.printStackTrace();
                player.sendMessage(ChatColor.RED + "An error occured while determining which faction you are in");
                event.setCancelled(true); // To be safe
            }

        }
    }

    /**
     * Check if a player is allowed to modify a certain block
     * @param event A cancellable event with getPlayer and getBlock methods
     */
    private void onBlockModified(Cancellable event) {
        try {
            Player player = (Player) event.getClass().getMethod("getPlayer").invoke(event);
            Block block = (Block) event.getClass().getMethod("getBlock").invoke(event);

            onLocationModified(event, player, block.getLocation());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            event.setCancelled(true); // To be safe
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        onBlockModified(e);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        UUID claim_fuuid = MFClaimManager.getClaim(e.getBlock().getLocation());

        if (claim_fuuid != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        UUID claim_fuuid = MFClaimManager.getClaim(e.getBlock().getLocation());

        Iterator<Block> i = e.blockList().iterator();
        while (i.hasNext()) {
            Block block = i.next();
            UUID fuuid = MFClaimManager.getClaim(block.getLocation());

            if (fuuid != null && !fuuid.equals(claim_fuuid)) {
                i.remove();
            }
        }
    }

    @EventHandler
    public void onBlockFertilize(BlockFertilizeEvent e) {
        onBlockModified(e);
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent e) {
        UUID claim_fuuid = MFClaimManager.getClaim(e.getBlock().getLocation());

        UUID fuuid = MFClaimManager.getClaim(e.getToBlock().getLocation());

        if (fuuid != null && !fuuid.equals(claim_fuuid)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent e) {
        if (e.getBlock().getLocation().equals(e.getNewState().getLocation())) {
            return;
        }

        UUID claim_fuuid = MFClaimManager.getClaim(e.getBlock().getLocation());

        UUID fuuid = MFClaimManager.getClaim(e.getNewState().getLocation());

        if (fuuid != null && !fuuid.equals(claim_fuuid)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent e) {
        UUID claim_fuuid = MFClaimManager.getClaim(e.getBlock().getLocation());

        Iterator<Block> i = e.getBlocks().iterator();
        while (i.hasNext()) {
            Block block = i.next();
            UUID fuuid = MFClaimManager.getClaim(block.getLocation());

            if (fuuid != null && !fuuid.equals(claim_fuuid)) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent e) {
        UUID claim_fuuid = MFClaimManager.getClaim(e.getBlock().getLocation());

        Iterator<Block> i = e.getBlocks().iterator();
        while (i.hasNext()) {
            Block block = i.next();
            UUID fuuid = MFClaimManager.getClaim(block.getLocation());

            if (fuuid != null && !fuuid.equals(claim_fuuid)) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        onBlockModified(e);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        Entity entity = e.getDamager();

        while (entity instanceof Projectile) {
            ProjectileSource source = ((Projectile) entity).getShooter();

            if (!(source instanceof Entity)) {
                break;
            }

            entity = (Entity) source;
        }

        if (entity instanceof Player) {
            onLocationModified(e, (Player) entity, e.getEntity().getLocation());
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        Iterator<Block> i = e.blockList().iterator();
        while (i.hasNext()) {
            Block block = i.next();
            UUID fuuid = MFClaimManager.getClaim(block.getLocation());

            if (fuuid != null) {
                i.remove();
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            onLocationModified(e, e.getPlayer(), e.getClickedBlock().getLocation());
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEntityEvent e) {
        onLocationModified(e, e.getPlayer(), e.getRightClicked().getLocation());
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        onBlockModified(e);
    }

}
