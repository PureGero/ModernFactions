package com.modernfactions.commands;

import com.modernfactions.MF;
import com.modernfactions.RandomTeleport;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestRandomTeleport implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target = null;

        if (sender instanceof Player) {
            target = (Player) sender;
        }

        if (args.length >= 1) {

            target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                MF.sendMessage(sender, ChatColor.RED, "argument.entity.notfound.player");
                return false;
            }

        } else if (target == null) {

            MF.sendMessage(sender, ChatColor.RED, "permissions.requires.player");
            return false;

        }

        MF.sendMessage(sender, ChatColor.GREEN, "random.teleport.success", target.getName());

        target.teleport(RandomTeleport.getRandomTeleportLocation());

        return true;
    }
}
