package com.modernfactions.commands;

import com.modernfactions.MF;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        MF.sendMessage(sender, ChatColor.GOLD, "store.link", "http://store.modernfactions.com/");

        return true;
    }
}
