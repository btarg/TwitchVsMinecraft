package com.icrazyblaze.twitchmod.irc;


import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.chat.ChatPicker;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PingEvent;


public class TwitchBot extends ListenerAdapter {

    public TwitchBot() {

        ChatPicker.loadBlacklistFile();

    }


    @Override
    public void onMessage(MessageEvent event) throws Exception {

        String message = event.getMessage();
        String sender = event.getUser().getNick();


        if (BotConfig.showChatMessages) {

            BotCommands.player().sendMessage(new TextComponentString(TextFormatting.WHITE + "<" + TextFormatting.DARK_PURPLE + "Twitch " + TextFormatting.WHITE + sender + "> " + message));
        }

        if (message.equalsIgnoreCase(BotConfig.prefix + "help") || message.equalsIgnoreCase(BotConfig.prefix + "commands")) {

            event.respond("Click here for a list of commands: http://bit.ly/2UfBCiL");

        } else if (message.startsWith(BotConfig.prefix)) {

            // Remove the prefix
            message = message.substring(BotConfig.prefix.length());
            ChatPicker.checkChat(message, sender);

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

