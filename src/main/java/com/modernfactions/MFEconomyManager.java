package com.modernfactions;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.CommandSender;
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

    /**
     * Returns the translation text for either singular, dual or plural number of coins
     * E.g. 1 coin (singular)
     *      2 coins (dual)
     *      3,4,5... coins (plural)
     * Note that there should never be 0 coins, it should say free instead
     * @param sender The sender to use the locale of
     * @param coins The number of coins (coins >= 1)
     */
    private BaseComponent[] getCoinsText(CommandSender sender, double coins) {
        if (coins < 2) {
            return MF.getMessage(sender, "economy.coins.singular", coins);
        } else if (coins < 3) {
            return MF.getMessage(sender, "economy.coins.dual", coins);
        } else {
            return MF.getMessage(sender, "economy.coins.plural", coins);
        }
    }

    /**
     * Returns the translation text, without any text component formatting,
     * for either singular, dual or plural number of coins
     * E.g. 1 coin (singular)
     *      2 coins (dual)
     *      3,4,5... coins (plural)
     * Note that there should never be 0 coins, it should say free instead
     * @param sender The sender to use the locale of
     * @param coins The number of coins (coins >= 1)
     */
    private String getCoinsTextRaw(CommandSender sender, double coins) {
        return TextComponent.toPlainText(getCoinsText(sender, coins));
    }

    public void giveMoney(UUID player, double amount) {
        if (amount == 0) {
            return;
        }

        economy.depositPlayer(plugin.getServer().getOfflinePlayer(player), amount);

        Player onlinePlayer = plugin.getServer().getPlayer(player);
        if (onlinePlayer != null) {
            MF.sendMessage(onlinePlayer, ChatColor.GOLD, "economy.add", getCoinsTextRaw(onlinePlayer, amount));
        }
    }

    public boolean takeMoney(UUID player, double amount) {
        if (amount == 0) {
            return true;
        }

        EconomyResponse response = economy.withdrawPlayer(plugin.getServer().getOfflinePlayer(player), amount);

        Player onlinePlayer = plugin.getServer().getPlayer(player);
        if (onlinePlayer != null) {
            if (response.transactionSuccess()) {
                MF.sendMessage(onlinePlayer, ChatColor.GOLD, "economy.take", getCoinsTextRaw(onlinePlayer, amount));
            } else {
                MF.sendMessage(onlinePlayer, ChatColor.RED, "economy.notenough",
                        getCoinsTextRaw(onlinePlayer, amount),
                        getCoinsTextRaw(onlinePlayer, economy.getBalance(onlinePlayer)));
            }
        }

        return response.transactionSuccess();
    }
}
