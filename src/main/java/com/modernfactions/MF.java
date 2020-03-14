package com.modernfactions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MF {

    /**
     * Translate and send a message to a CommandSender
     */
    public static void sendMessage(CommandSender sender, String name, Object... args) {
        sender.spigot().sendMessage(MFTranslationManager.get().getMessage(sender, name, args));
    }
}
