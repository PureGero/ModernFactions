package com.modernfactions.listener;

import com.modernfactions.MF;
import com.modernfactions.ModernFactions;
import com.vexsoftware.votifier.model.VotifierEvent;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VotifierListener implements Listener {

    @EventHandler
    public void onVote(VotifierEvent e) {
        Player player = Bukkit.getPlayerExact(e.getVote().getUsername());

        if (player != null) {
            MF.sendMessage(player, ChatColor.GOLD, "vote.thankyou", e.getVote().getServiceName());
            ModernFactions.getEconomy().giveMoney(player.getUniqueId(), 500);
        }
    }

}
