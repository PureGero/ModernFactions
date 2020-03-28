package com.modernfactions.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ModernFactionsAdminTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            for (Method method : ModernFactionsAdminCommand.class.getMethods()) {
                if (method.getParameterCount() == 3) {
                    if (method.getName().startsWith(args[0])) {
                        tab.add(method.getName());
                    }
                }
            }
        }

        if (args.length >= 2) {
            try {
                Method method = getClass().getMethod(args[0].toLowerCase(), CommandSender.class, String.class, String[].class, List.class);

                String[] newargs = new String[args.length - 1];
                System.arraycopy(args, 1, newargs, 0, newargs.length);

                method.invoke(this, sender, alias, newargs, tab);
            } catch (NoSuchMethodException e) {

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return tab;
    }

    public void givemoney(CommandSender sender, String alias, String[] args, List<String> tab) {
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    tab.add(player.getName());
                }
            }
        }
    }
}
