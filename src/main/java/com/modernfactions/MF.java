package com.modernfactions;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

public class MF {

    /**
     * Translate and send a message to a CommandSender
     */
    public static void sendMessage(CommandSender sender, String name, Object... args) {
        sender.spigot().sendMessage(MFTranslationManager.get().getMessage(sender, name, args));
    }

    /**
     * Translate a message
     */
    public static BaseComponent[] getMessage(CommandSender sender, String name, Object... args) {
        return MFTranslationManager.get().getMessage(sender, name, args);
    }
}
