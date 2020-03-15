package com.modernfactions;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;

public class MF {

    /**
     * Translate and send a message to a CommandSender
     */
    public static void sendMessage(CommandSender sender, String name, Object... args) {
        sender.spigot().sendMessage(MFTranslationManager.get().getMessage(sender, name, args));
    }

    /**
     * Translate and send a message to a CommandSender with a certain color
     */
    public static void sendMessage(CommandSender sender, ChatColor color, String name, Object... args) {
        sender.spigot().sendMessage(getMessage(sender, color, name, args));
    }

    /**
     * Translate a message
     */
    public static BaseComponent[] getMessage(CommandSender sender, String name, Object... args) {
        return MFTranslationManager.get().getMessage(sender, name, args);
    }

    /**
     * Translate a message and give it a color
     */
    public static BaseComponent[] getMessage(CommandSender sender, ChatColor color, String name, Object... args) {
        return new ComponentBuilder().append(getMessage(sender, name, args)).color(color).create();
    }
}
