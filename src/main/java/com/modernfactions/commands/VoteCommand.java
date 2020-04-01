package com.modernfactions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VoteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        sender.sendMessage("1. https://www.planetminecraft.com/server/modern-factions-4002768/vote/");
        sender.sendMessage("2. https://minecraft-mp.com/server/249439/vote/");
        sender.sendMessage("3. https://minecraftlist.org/vote/15756");
        sender.sendMessage("4. https://minelist.net/vote/2888");

        return true;
    }
}
