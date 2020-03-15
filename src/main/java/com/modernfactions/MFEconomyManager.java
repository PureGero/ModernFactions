package com.modernfactions;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class MFEconomyManager {
    private Economy economy = null;
    private JavaPlugin plugin;

    public MFEconomyManager(JavaPlugin plugin) {
        this.plugin = plugin;

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            economy = rsp.getProvider();
        }
    }

    public void giveMoney(UUID player, double amount) {
        economy.depositPlayer(plugin.getServer().getOfflinePlayer(player), amount);

        Player onlinePlayer = plugin.getServer().getPlayer(player);
        if (onlinePlayer != null) {
            onlinePlayer.spigot().sendMessage(new ComponentBuilder()
                    .append(MF.getMessage(onlinePlayer, "economy.add", amount))
                    .color(ChatColor.GOLD).create());
        }
    }

    public boolean takeMoney(UUID player, double amount) {
        EconomyResponse response = economy.withdrawPlayer(plugin.getServer().getOfflinePlayer(player), amount);

        Player onlinePlayer = plugin.getServer().getPlayer(player);
        if (onlinePlayer != null) {
            if (response.transactionSuccess()) {
                onlinePlayer.spigot().sendMessage(new ComponentBuilder()
                        .append(MF.getMessage(onlinePlayer, "economy.take", amount))
                        .color(ChatColor.GOLD).create());
            } else {
                onlinePlayer.spigot().sendMessage(new ComponentBuilder()
                        .append(MF.getMessage(onlinePlayer, "economy.notenough", amount, economy.getBalance(onlinePlayer)))
                        .color(ChatColor.RED).create());
            }
        }

        return response.transactionSuccess();
    }
}
