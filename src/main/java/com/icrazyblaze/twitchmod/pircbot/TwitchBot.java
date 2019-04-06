package com.icrazyblaze.twitchmod.pircbot;

import org.jibble.pircbot.PircBot;

import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.chat.ChatPicker;
import com.icrazyblaze.twitchmod.util.PrintToChat;

import net.minecraft.util.text.TextFormatting;

public class TwitchBot extends PircBot {
	
	
	public TwitchBot() {
		this.setName("MinecraftBot");
		}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message) {

		if (Config.showChatMessages) {
			Main.logger.info(sender + ": " + message);
			PrintToChat.print(TextFormatting.DARK_PURPLE, sender + ": " + message);
		}
		
		if (message.equalsIgnoreCase("!commands")) {
			this.sendMessage(channel, "Click here for a list of commands: http://bit.ly/2UfBCiL");
		}
		else if (message.startsWith("!")) {
			ChatPicker.newChats.add(message);
			ChatPicker.newChatSenders.add(sender);
		}
	}
	
	
	public void onConnect() {
		Config.isConnected = true;
		PrintToChat.print(TextFormatting.GREEN, "Bot connected! Use /ttv to see details.");
	}
	
	
	public void onDisconnect() {
		Config.isConnected = false;
		PrintToChat.print(TextFormatting.RED, "Bot disconnected.");
	}
	
}

