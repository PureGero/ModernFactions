package com.modernfactions;

import com.modernfactions.data.MFDatabaseManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MFTutorialManager implements Runnable {
    private JavaPlugin plugin;

    public MFTutorialManager(JavaPlugin plugin) {
        this.plugin = plugin;

        // Approx every 5 mins after 2 mins
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 135*20L, 287*20L);
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            ArrayList<String> messages = getTutorialMessages(player);

            if (messages.size() > 0) {
                String message = messages.get((int) (Math.random() * messages.size()));

                MF.sendMessage(player, ChatColor.GOLD, message);
            }
        }
    }

    private ArrayList<String> getTutorialMessages(Player player) {
        ArrayList<String> messages = new ArrayList<>();

        try {
            UUID fuuid = MFDatabaseManager.getDatabase().getFaction(player.getUniqueId());

            if (fuuid == null) {
                messages.add("tutorial.createafaction");
                return messages;
            }

            int claims = MFDatabaseManager.getDatabase().getFactionClaims(fuuid);

            if (claims == 0) {
                messages.add("tutorial.claimland");
                return messages;
            }

            int memberCount = MFDatabaseManager.getDatabase().getFactionMemberCount(fuuid);

            if (memberCount < 2) {
                messages.add("tutorial.inviteplayer");
            }

            List<UUID> allies = MFDatabaseManager.getDatabase().getAddedAllies(fuuid);

            if (allies.size() == 0) {
                messages.add("tutorial.makeally");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }
}
