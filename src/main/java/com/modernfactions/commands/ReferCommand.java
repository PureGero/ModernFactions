package com.modernfactions.commands;

import com.modernfactions.MF;
import com.modernfactions.ModernFactions;
import com.modernfactions.RandomTeleport;
import com.modernfactions.data.MFDatabaseManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class ReferCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.spigot().sendMessage(new ComponentBuilder().append(
                    MF.getMessage(sender, "commands.generic.usage",
                            (Object) MF.getMessage(sender, "command.refer.usage", label))
            ).color(ChatColor.RED).create());
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            MF.sendMessage(sender, "argument.entity.notfound.player");
            return false;
        }

        UUID uuid = ((Player) sender).getUniqueId();

        try {
            if (MFDatabaseManager.getDatabase().hasReferredAPlayer(uuid)) {
                sender.spigot().sendMessage(new ComponentBuilder().append(
                        MF.getMessage(sender, "command.refer.error.alreadyreferred")
                ).color(ChatColor.RED).create());
                return false;
            }

            MFDatabaseManager.getDatabase().referAPlayer(uuid, target.getUniqueId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sender.spigot().sendMessage(new ComponentBuilder().append(
                MF.getMessage(sender, "command.refer.success", target.getName())
        ).color(ChatColor.GREEN).create());

        ModernFactions.getEconomy().giveMoney(uuid, 1000);
        ModernFactions.getEconomy().giveMoney(target.getUniqueId(), 1000);

        return true;
    }
}
