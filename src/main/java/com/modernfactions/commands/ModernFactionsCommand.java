package com.modernfactions.commands;

import com.modernfactions.*;
import com.modernfactions.data.MFDatabaseManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.UUID;

public class ModernFactionsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            try {
                Method method = getClass().getMethod(args[0], CommandSender.class, String.class, String[].class);

                String[] newargs = new String[args.length - 1];
                System.arraycopy(args, 1, newargs, 0, newargs.length);

                method.invoke(this, sender, label, newargs);

                return true;
            } catch (NoSuchMethodException e) {

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                MF.sendMessage(sender, ChatColor.RED, "command.failed", e.getMessage());
                return false;
            }
        }

        help(sender, label, args);

        return false;
    }

    public void help(CommandSender sender, String label, String[] args) {
        ComponentBuilder builder = new ComponentBuilder();
        builder.append(" # --- ===    ").color(ChatColor.BLUE);
        builder.append(MF.getMessage(sender, "help")).color(ChatColor.AQUA);
        builder.append("    === --- # ").color(ChatColor.BLUE);
        builder.append("\n");

        builder.append(MF.getMessage(sender, "command.create.usage", label)).color(ChatColor.AQUA);
        builder.append(": ");
        builder.append(MF.getMessage(sender, "command.create.description")).color(ChatColor.BLUE);
        builder.append("\n");

        builder.append(MF.getMessage(sender, "command.info.usage", label)).color(ChatColor.AQUA);
        builder.append(": ");
        builder.append(MF.getMessage(sender, "command.info.description")).color(ChatColor.BLUE);
        builder.append("\n");

        builder.append(MF.getMessage(sender, "command.claim.usage", label)).color(ChatColor.AQUA);
        builder.append(": ");
        builder.append(MF.getMessage(sender, "command.claim.description")).color(ChatColor.BLUE);
        builder.append("\n");

        builder.append(" # --- === ").color(ChatColor.BLUE);
        builder.append(MF.getMessage(sender, "help.page", 1, 1)).color(ChatColor.AQUA);
        builder.append(" === --- # ").color(ChatColor.BLUE);

        sender.spigot().sendMessage(builder.create());
    }

    public void create(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            sender.spigot().sendMessage(new ComponentBuilder().append(
                    MF.getMessage(sender, "commands.generic.usage",
                            (Object) MF.getMessage(sender, "command.create.usage", label))
                    ).color(ChatColor.RED).create());
        } else {
            UUID newFuuid = UUID.randomUUID();
            UUID uuid = ((Entity) sender).getUniqueId();
            String name = args[0];
            try {
                MFDatabaseManager.getDatabase().setFactionName(newFuuid, name);
                if (name.equals(MFDatabaseManager.getDatabase().getFactionName(newFuuid))) {
                    MFDatabaseManager.getDatabase().setFactionMemberRole(newFuuid, uuid, Roles.OWNER);
                    MF.sendMessage(sender, ChatColor.GREEN, "command.create.success", name);
                } else {
                    MF.sendMessage(sender, ChatColor.RED, "command.create.error.alreadyexists", name);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                MF.sendMessage(sender, ChatColor.RED, "command.error", e.getMessage());
            }
        }
    }

    public void info(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        UUID fuuid = MFClaimManager.getClaim(player.getLocation());

        if (fuuid == null) {
            MF.sendMessage(sender, ChatColor.RED, "command.info.error.nofaction");
            return;
        }

        try {
            MF.sendMessage(sender, ChatColor.GOLD, "command.info.success",
                    MFDatabaseManager.getDatabase().getFactionName(fuuid),
                    MFDatabaseManager.getDatabase().getFactionMemberCount(fuuid));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void claim(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        UUID claim_fuuid = MFClaimManager.getClaim(player.getLocation());

        try {
            if (claim_fuuid != null) {
                MF.sendMessage(sender, ChatColor.RED, "command.claim.error.alreadyclaimed",
                        MFDatabaseManager.getDatabase().getFactionName(claim_fuuid));
                return;
            }

            UUID fuuid = MFDatabaseManager.getDatabase().getFaction(player.getUniqueId());

            if (fuuid == null) {
                MF.sendMessage(sender, ChatColor.RED, "command.claim.error.notinfaction");
                return;
            }

            int claimsCounts = MFDatabaseManager.getDatabase().getFactionClaims(fuuid);

            if (claimsCounts >= ModernFactions.getMFConfig().getNumberOfFreeClaims() &&
                    !ModernFactions.getEconomy().takeMoney(player.getUniqueId(), ModernFactions.getMFConfig().getClaimPrice())) {
                // takeMoney send the error message
                return;
            }

            MFDatabaseManager.getDatabase().increaseBy1FactionClaim(fuuid, player.getWorld().getUID());
            MFClaimManager.setClaim(player.getLocation(), fuuid);

            MF.sendMessage(sender, ChatColor.GOLD, "command.claim.success",
                    player.getLocation().getBlockX() >> 4 << 4,
                    player.getLocation().getBlockZ() >> 4 << 4,
                    MFDatabaseManager.getDatabase().getFactionName(fuuid));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
