package com.modernfactions.commands;

import com.modernfactions.Factions;
import com.modernfactions.MF;
import com.modernfactions.Roles;
import com.modernfactions.data.MFDatabaseManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

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
                help(sender, label, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                sender.spigot().sendMessage(new ComponentBuilder()
                        .append(MF.getMessage(sender, "command.failed"))
                        .color(ChatColor.RED).create());
            }
        }

        return false;
    }

    public void help(CommandSender sender, String label, String[] args) {
        ComponentBuilder builder = new ComponentBuilder();
        builder.append(" # --- ===   ").color(ChatColor.BLUE);
        builder.append(MF.getMessage(sender, "help")).color(ChatColor.AQUA);
        builder.append("   === --- # ").color(ChatColor.BLUE);
        builder.append("\n");

        builder.append(MF.getMessage(sender, "command.create.usage", label)).color(ChatColor.AQUA);
        builder.append(": ");
        builder.append(MF.getMessage(sender, "command.create.description")).color(ChatColor.BLUE);
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
                    sender.spigot().sendMessage(new ComponentBuilder()
                            .append(MF.getMessage(sender, "command.create.success", name))
                            .color(ChatColor.GREEN).create());
                } else {
                    sender.spigot().sendMessage(new ComponentBuilder()
                            .append(MF.getMessage(sender, "command.create.error.alreadyexists", name))
                            .color(ChatColor.RED).create());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                sender.spigot().sendMessage(new ComponentBuilder()
                        .append(MF.getMessage(sender, "command.error", e.getMessage()))
                        .color(ChatColor.RED).create());
            }
        }
    }
}
