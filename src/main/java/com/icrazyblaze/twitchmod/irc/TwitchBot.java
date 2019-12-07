package com.icrazyblaze.twitchmod.irc;


import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.chat.ChatPicker;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PingEvent;


public class TwitchBot extends ListenerAdapter {

    private static boolean forceCommands = false;

    public TwitchBot() {

        ChatPicker.loadBlacklistFile();

    }


    @Override
    public void onMessage(MessageEvent event) throws Exception {

        String message = event.getMessage();
        String sender = event.getUser().getNick();

        TextFormatting format = TextFormatting.WHITE;


        if (BotConfig.showChatMessages) {

            if (event.getTags().get("badges").contains("broadcaster/1")) {
                format = TextFormatting.GOLD;
                forceCommands = true; // Force commands to execute instantly for broadcaster testing
            } else if (event.getTags().get("badges").contains("subscriber/1")) {
                format = TextFormatting.AQUA;
            } else if (event.getTags().get("badges").contains("moderator/1")) {
                format = TextFormatting.GREEN;
            }

            if (!message.startsWith(BotConfig.prefix) || BotConfig.showCommands) {

                BotCommands.player().sendMessage(new TextComponentString(TextFormatting.WHITE + "<" + TextFormatting.DARK_PURPLE + "Twitch " + format + sender + TextFormatting.WHITE + "> " + message));

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

