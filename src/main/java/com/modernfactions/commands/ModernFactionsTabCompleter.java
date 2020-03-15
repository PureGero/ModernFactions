package com.modernfactions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ModernFactionsTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            for (Method method : ModernFactionsCommand.class.getMethods()) {
                if (method.getParameterCount() == 3) {
                    tab.add(method.getName());
                }
            }
        }

        return tab;
    }
}
