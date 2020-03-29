package com.modernfactions.listener;

import com.modernfactions.MF;
import com.modernfactions.data.MFDatabaseManager;
import com.modernfactions.util.CountryUtil;
import com.modernfactions.util.LanguageUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.chat.ComponentSerializer;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.SQLException;
import java.util.UUID;

public class ChatListener implements Listener {
    private Chat vaultChat;

    public ChatListener() {
        vaultChat = Bukkit.getServicesManager().load(Chat.class);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void overrideChat(AsyncPlayerChatEvent e) {
        for (Player player : e.getRecipients()) {
            TranslatableComponent chat = new TranslatableComponent("chat.type.text");

            chat.addWith(formatName(e.getPlayer(), player));
            chat.addWith(formatMessage(e.getPlayer(), player, e.getMessage()));

            String json = ComponentSerializer.toString(chat);

            if (json.contains("{}")) {
                System.out.println(json);
                player.sendMessage("<" + e.getPlayer().getName() + "> " + e.getMessage());
                continue;
            }

            player.spigot().sendMessage(chat);

            if (player == e.getPlayer()) {
                Bukkit.getConsoleSender().spigot().sendMessage(chat);
            }
        }

        e.setCancelled(true);
    }

    private BaseComponent formatName(Player sender, Player receiver) {
        ComponentBuilder name = new ComponentBuilder();

        if (sender.hasPermission("modernfactions.vip")) {
            name.append(MF.getMessage(receiver, "vip")).color(ChatColor.GOLD).bold(true)
                .event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder().append(MF.getMessage(receiver, "vip.advertise")).color(ChatColor.GOLD).create()
                ));
            name.append(" ");
            name.reset();
        }

        try {
            UUID sender_fuuid = MFDatabaseManager.getDatabase().getFaction(sender.getUniqueId());
            UUID receiver_fuuid = MFDatabaseManager.getDatabase().getFaction(receiver.getUniqueId());

            if (sender_fuuid != null) {
                String factionName = MFDatabaseManager.getDatabase().getFactionName(sender_fuuid);

                String allianceStatus = "faction.status.enemies";
                ChatColor allianceColor = ChatColor.RED;

                if (sender_fuuid.equals(receiver_fuuid)) {
                    allianceStatus = "faction.status.mine";
                    allianceColor = ChatColor.GREEN;
                } else if (receiver_fuuid != null &&
                        MFDatabaseManager.getDatabase().areAllies(sender_fuuid, receiver_fuuid)) {
                    allianceStatus = "faction.status.allies";
                    allianceColor = ChatColor.YELLOW;
                }

                int claims = MFDatabaseManager.getDatabase().getFactionClaims(sender_fuuid);
                int members = MFDatabaseManager.getDatabase().getFactionMemberCount(sender_fuuid);

                TextComponent factionText = new TextComponent("[" + factionName + "] ");
                factionText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder()
                        .append(MF.getMessage(receiver, "faction.name", factionName)).append("\n")
                        .append(MF.getMessage(receiver, allianceColor, allianceStatus)).append("\n")
                        .append(MF.getMessage(receiver, ChatColor.GOLD, "faction.members", members)).append("\n")
                        .append(MF.getMessage(receiver, ChatColor.GOLD, "faction.claims", claims)).append("\n")
                .create()));
                factionText.setColor(allianceColor);
                name.append(factionText);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        String prefix = vaultChat.getPlayerPrefix(sender);

        if (prefix != null && prefix.length() > 0) {
            BaseComponent[] prefixComponent = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', prefix));
            name.append(prefixComponent)
                .event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder().append(MF.getMessage(receiver, "rank.name", TextComponent.toPlainText(prefixComponent)))
                                .append("\n").append(MF.getMessage(receiver, "rank.viewall")).create()
                ));
            name.append(" ");
        }

        TextComponent playerName = new TextComponent(sender.getDisplayName());
        //playerName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ComponentBuilder("{id:" + sender.getUniqueId() + ",type:minecraft:player,name:" + sender.getName() + "}").create()));
        playerName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder()
                        .append(sender.getName()).append("\n")
                        .append(LanguageUtil.getLanguageName(sender.getLocale())).append("\n")
                        .append(CountryUtil.getCountry(sender.getAddress().getAddress().getHostAddress()))
                        .create()));
        name.append(playerName);

        String suffix = vaultChat.getPlayerSuffix(sender);

        if (suffix != null && suffix.length() > 0) {
            name.append(" ");
            name.append(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', suffix)));
        }

        return new TextComponent(name.create());
    }

    private BaseComponent formatMessage(Player sender, Player receiver, String message) {

        // Coloured chat
        if (sender.hasPermission("modernfactions.vip")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        return new TextComponent(TextComponent.fromLegacyText(message));
    }
}
