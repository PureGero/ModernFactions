package com.modernfactions.commands;

import com.modernfactions.MF;
import com.modernfactions.MFClaimManager;
import com.modernfactions.ModernFactions;
import com.modernfactions.Roles;
import com.modernfactions.data.BlockPos;
import com.modernfactions.data.MFDatabaseManager;
import com.modernfactions.listener.TemporaryMotdListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.UUID;

public class ModernFactionsAdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(ModernFactions.get(), () -> {
            if (args.length > 0) {
                try {
                    Method method = getClass().getMethod(args[0].toLowerCase(), CommandSender.class, String.class, String[].class);

                    String[] newargs = new String[args.length - 1];
                    System.arraycopy(args, 1, newargs, 0, newargs.length);

                    method.invoke(this, sender, label, newargs);

                    return;
                } catch (NoSuchMethodException e) {
                    // Ignore
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    MF.sendMessage(sender, ChatColor.RED, "command.failed", e.getMessage());
                    return;
                }
            }

            sender.sendMessage("Invalid command");

        });

        return true;
    }

    public void givemoney(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /" + label + " givemoney <player> <amount>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            MF.sendMessage(sender, ChatColor.RED, "argument.entity.notfound.player");
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            MF.sendMessage(sender, ChatColor.RED, "parsing.double.invalid", args[1]);
            return;
        }

        MF.sendMessage(target, ChatColor.GREEN, "purchase.coins.thankyou", args[1]);

        if (target.hasPermission("modernfactions.vip")) {
            MF.sendMessage(target, ChatColor.GREEN, "purchase.coins.vip");
            amount *= 2;
        }

        ModernFactions.getEconomy().giveMoney(target.getUniqueId(), amount);

        sender.sendMessage("Gave " + target.getName() + " " + amount + " coins");
    }

    public void motd(CommandSender sender, String label, String[] args) {
        if (args.length < 1) {
            TemporaryMotdListener.clearMotd();

            sender.sendMessage("Cleared motd");

            return;
        }

        String motd = String.join(" ", args);

        TemporaryMotdListener.setMotd(motd);

        sender.sendMessage("Set motd to " + motd);
    }
}
