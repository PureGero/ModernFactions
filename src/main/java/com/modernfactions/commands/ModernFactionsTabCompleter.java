package com.modernfactions.commands;

import com.modernfactions.data.MFDatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModernFactionsTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            for (Method method : ModernFactionsCommand.class.getMethods()) {
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

    public void h(CommandSender sender, String alias, String[] args, List<String> tab) {
        home(sender, alias, args, tab);
    }

    public void home(CommandSender sender, String alias, String[] args, List<String> tab) {
        if (!(sender instanceof Player)) {
            return;
        }

        UUID uuid = ((Player) sender).getUniqueId();

        if (args.length == 1) {
            try {
                UUID fuuid = MFDatabaseManager.getDatabase().getFaction(uuid);

                if (fuuid == null) {
                    return;
                }

                List<String> allies = MFDatabaseManager.getDatabase().getAddedMeNames(fuuid);

                for (String ally : allies) {
                    if (ally.toLowerCase().startsWith(args[0].toLowerCase())) {
                        tab.add(ally);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void enemy(CommandSender sender, String alias, String[] args, List<String> tab) {
        if (!(sender instanceof Player)) {
            return;
        }

        UUID uuid = ((Player) sender).getUniqueId();

        if (args.length == 1) {
            try {
                UUID fuuid = MFDatabaseManager.getDatabase().getFaction(uuid);

                if (fuuid == null) {
                    return;
                }

                List<String> allies = MFDatabaseManager.getDatabase().getAddedAlliesNames(fuuid);

                for (String ally : allies) {
                    if (ally.toLowerCase().startsWith(args[0].toLowerCase())) {
                        tab.add(ally);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
