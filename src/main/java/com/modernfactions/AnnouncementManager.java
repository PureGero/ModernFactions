package com.modernfactions;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AnnouncementManager implements Runnable {

    public AnnouncementManager(JavaPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, this, 5*60*20L, 5*60*20L);
    }

    private static final String[] ANNOUNCEMENTS = new String[] {
            "Join our discord server at /discord",
            "Vote for us with /vote to earn some coins",
            "Refer a friend with /refer to get some coins",
            "Buy stuff from the in-game shop at /shop, and sell stuff with /sell",
            "Check out the online store at /store",
            "See the ranks with /ranks, and rankup to the next rank with /rankup",
            "View your faction's territory with /terr, and claim more territory with /f claim",
    };

    private String lastMessage = null;
    private ArrayList<String> currentList = new ArrayList<>();

    public void run() {
        if (currentList.size() == 0) {
            currentList.addAll(Arrays.asList(ANNOUNCEMENTS));
            Collections.shuffle(currentList);
        }

        int i = 0;
        if (currentList.get(i).equals(lastMessage))
            i ++;
        String msg = currentList.get(i);
        lastMessage = msg;
        currentList.remove(i);

        // <ModernFactions> msg
        TranslatableComponent chatMessage = new TranslatableComponent("chat.type.text");
        TextComponent username = new TextComponent("ModernFactions");
        username.setColor(ChatColor.AQUA);
        chatMessage.addWith(username);
        chatMessage.addWith(msg);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(chatMessage);
        }
    }

}
