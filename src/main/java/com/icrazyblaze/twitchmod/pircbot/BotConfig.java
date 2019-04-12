package com.icrazyblaze.twitchmod.pircbot;

import com.icrazyblaze.twitchmod.util.PrintToChat;

import net.minecraft.util.text.TextFormatting;

public class BotConfig {
	
	public static boolean isConnected = false;
	public static String TWITCH_KEY = null;
	public static String CHANNEL_NAME = null;
	
	public static boolean showChatMessages = false;

	public static void main() throws Exception {

		TwitchBot bot = new TwitchBot();
		bot.setVerbose(true);
		
		bot.connect("irc.twitch.tv", 6667, TWITCH_KEY);
		bot.joinChannel("#" + CHANNEL_NAME);
	
	}

}
