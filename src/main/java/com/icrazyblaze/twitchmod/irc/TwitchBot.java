package com.icrazyblaze.twitchmod.irc;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.chat.ChatPicker;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jibble.pircbot.PircBot;

public class TwitchBot extends PircBot {

    public TwitchBot() {
        this.setName("MinecraftBot");
        ChatPicker.loadBlacklistFile();
    }


    public void onMessage(String channel, String sender, String login, String hostname, String message) {

        if (BotConfig.showChatMessages) {
            BotCommands.player().sendMessage(new TextComponentString(TextFormatting.WHITE + "<" + TextFormatting.DARK_PURPLE + "Twitch " + TextFormatting.WHITE + sender + "> " + message));
        }

        if (message.equalsIgnoreCase("!help") || message.equalsIgnoreCase("!commands")) {
            this.sendMessage(channel, "Click here for a list of commands: http://bit.ly/2UfBCiL");
        } else {
            ChatPicker.checkChat(message, sender);
        }
    }

    public void onConnect() {
        BotConnection.isConnected = true;
        BotCommands.player().sendMessage(new TextComponentString(TextFormatting.DARK_GREEN + "Bot connected! Use /ttv to see details."));
    }


    public void onDisconnect() {
        BotConnection.isConnected = false;
        BotCommands.player().sendMessage(new TextComponentString(TextFormatting.DARK_RED + "Bot disconnected."));
    }

}

