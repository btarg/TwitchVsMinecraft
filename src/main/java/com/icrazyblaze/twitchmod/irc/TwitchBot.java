package com.icrazyblaze.twitchmod.irc;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.jibble.pircbot.*;

import com.google.common.collect.Lists;
import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.chat.ChatPicker;
import com.icrazyblaze.twitchmod.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

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
		}
		else {
			ChatPicker.checkChat(message, sender);
		}
	}
	
	public void onConnect() {
		BotConfig.isConnected = true;
		BotCommands.player().sendMessage(new TextComponentString(TextFormatting.DARK_GREEN + "Bot connected! Use /ttv to see details."));
	}
	
	
	public void onDisconnect() {
		BotConfig.isConnected = false;
		BotCommands.player().sendMessage(new TextComponentString(TextFormatting.DARK_RED + "Bot disconnected."));
	}
	
}

