package com.modernfactions.commands;

import com.modernfactions.MF;
import com.modernfactions.MFClaimManager;
import com.modernfactions.ModernFactions;
import com.modernfactions.data.MFDatabaseManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TerritoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        try {
            UUID fuuid = MFDatabaseManager.getDatabase().getFaction(player.getUniqueId());
            List<UUID> allies = MFDatabaseManager.getDatabase().getAddedMe(fuuid);

            for (int i = 0; i < 32; i++) {
                TerritoryView view = new TerritoryView(player, i, fuuid, allies);
                Bukkit.getScheduler().runTaskLater(ModernFactions.get(), view, i);
                Bukkit.getScheduler().runTaskLater(ModernFactions.get(), view, 10*20 + i);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    class TerritoryView implements Runnable {
        Block center;
        Player player;
        int radius;
        UUID fuuid;
        List<UUID> allies;
        boolean show = true;

        public TerritoryView(Player player, int radius, UUID fuuid, List<UUID> allies) {
            this.center = player.getLocation().getBlock();
            this.player = player;
            this.radius = radius;
            this.fuuid = fuuid;
            this.allies = allies;
        }

        @Override
        public void run() {
            for (int i = 0; i <= radius; i++) {
                doBlock(radius, i);
                doBlock(radius, -i);
                doBlock(-radius, i);
                doBlock(-radius, -i);
                doBlock(i, radius);
                doBlock(-i, radius);
                doBlock(i, -radius);
                doBlock(-i, -radius);
            }

            show = !show;
        }

        private void doBlock(int x, int z) {
            Block block = center.getRelative(x, 0, z);

            while (block.getType().isSolid()) {
                block = block.getRelative(0, 1, 0);
            }
            while (!block.getType().isSolid()) {
                block = block.getRelative(0, -1, 0);
            }

            if (show) {
                UUID claim_fuuid = MFClaimManager.getClaim(block.getLocation());
                Material glass = Material.WHITE_STAINED_GLASS;

                if (claim_fuuid != null) {
                    glass = Material.RED_STAINED_GLASS;
                }

                if (claim_fuuid != null && allies.contains(claim_fuuid)) {
                    glass = Material.YELLOW_STAINED_GLASS;
                }

                if (claim_fuuid != null && claim_fuuid.equals(fuuid)) {
                    glass = Material.LIME_STAINED_GLASS;
                }

                player.sendBlockChange(block.getLocation(), glass.createBlockData());
            } else {
                player.sendBlockChange(block.getLocation(), block.getBlockData());
            }
        }
    }
}
