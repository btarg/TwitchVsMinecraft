package com.icrazyblaze.twitchmod.irc;


import com.google.common.collect.ImmutableMap;
import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.chat.ChatPicker;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PingEvent;

import java.util.Objects;


public class TwitchBot extends ListenerAdapter {

    private static boolean forceCommands = false;

    public TwitchBot() {
        ChatPicker.loadBlacklistFile();
    }


    @Override
    public void onMessage(MessageEvent event) {

        String message = event.getMessage();
        String sender = Objects.requireNonNull(event.getUser()).getNick();
        ImmutableMap<String, String> tags = event.getV3Tags();

        TextFormatting format = TextFormatting.WHITE;

        TextComponentString showText;
        String role = null;

        if (BotConfig.showChatMessages) {

            if (tags != null) {

                if (tags.get("badges").contains("broadcaster/1")) {
                    format = TextFormatting.GOLD;
                    forceCommands = true; // Force commands to execute instantly for broadcaster testing
                    role = "Broadcaster";
                } else if (tags.get("badges").contains("subscriber/1")) {
                    format = TextFormatting.AQUA;
                    role = "Subscriber";
                } else if (tags.get("badges").contains("moderator/1")) {
                    format = TextFormatting.GREEN;
                    role = "Moderator";
                }
            }

            if (!message.startsWith(BotConfig.prefix) || BotConfig.showCommands) {

                showText = new TextComponentString(TextFormatting.WHITE + "<" + TextFormatting.DARK_PURPLE + "Twitch " + format + sender + TextFormatting.WHITE + "> " + message);

                if (role != null) {
                    showText.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(format + role)));
                }

                BotCommands.player().sendMessage(showText);

            }

        }

        if (message.equalsIgnoreCase(BotConfig.prefix + "help") || message.equalsIgnoreCase(BotConfig.prefix + "commands")) {

            event.respond("Click here for a list of commands: http://bit.ly/2UfBCiL");

        } else if (message.equalsIgnoreCase(BotConfig.prefix + "modlink")) {

            event.respond("Click here to download the mod: http://bit.ly/TwitchVsMinecraft");

        } else if (message.equalsIgnoreCase(BotConfig.prefix + "blacklist")) {

            event.respond("Blacklisted commands: " + ChatPicker.blacklist.toString());

        } else if (message.startsWith(BotConfig.prefix)) {

            // Remove the prefix
            message = message.substring(BotConfig.prefix.length());

            // Add command to queue
            ChatPicker.newChats.add(message);
            ChatPicker.newChatSenders.add(sender);

        }

    }

    public void onConnect(ConnectEvent event) {
        BotCommands.player().sendMessage(new TextComponentString(TextFormatting.DARK_GREEN + "Bot connected! Use /ttv to see details."));
    }


    public void onDisconnect(DisconnectEvent event) {
        BotCommands.player().sendMessage(new TextComponentString(TextFormatting.DARK_RED + "Bot disconnected."));
    }


    // Prevent the bot from being kicked
    @Override
    public void onPing(PingEvent event) throws Exception {
        BotConnection.bot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

}

