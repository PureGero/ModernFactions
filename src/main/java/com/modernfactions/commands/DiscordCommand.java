package com.modernfactions.commands;

import com.modernfactions.MF;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DiscordCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        MF.sendMessage(sender, ChatColor.GOLD, "discord.link", "https://discord.gg/uvn36MZ");

        return true;
    }
}
