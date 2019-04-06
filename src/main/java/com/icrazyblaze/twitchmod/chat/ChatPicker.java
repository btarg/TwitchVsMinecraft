package com.icrazyblaze.twitchmod.chat;

import java.util.ArrayList;
import java.util.Random;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.pircbot.Config;
import com.icrazyblaze.twitchmod.util.PrintToChat;

import net.minecraft.util.text.TextFormatting;

public class ChatPicker {
	
    public static ArrayList<String> newChats = new ArrayList<String>();
    public static ArrayList<String> newChatSenders = new ArrayList<String>();

    public static void pickRandomChat() {
        String message;
        String sender;
        Random rand = new Random();
        int listRandom = rand.nextInt(newChats.size());

        message = newChats.get(listRandom);
        sender = newChats.get(listRandom);


        if (message.equalsIgnoreCase("!poison")) {
            BotCommands.addPoison();
        }
        else if (message.equalsIgnoreCase("!hunger")) {
            BotCommands.addHunger();
        }
        else if (message.equalsIgnoreCase("!slowness")) {
            BotCommands.addSlowness();
        }
        else if (message.equalsIgnoreCase("!speed")) {
            BotCommands.addSpeed();
        }
        else if (message.equalsIgnoreCase("!nausea")) {
            BotCommands.addNausea();
        }
        else if (message.equalsIgnoreCase("!fire")) {
            BotCommands.setOnFire();
        }
        else if (message.equalsIgnoreCase("!lava")) {
            BotCommands.floorIsLava();
        }
        else if (message.equalsIgnoreCase("!deathtimer")) {
            BotCommands.deathTimer();
        }
        else if (message.equalsIgnoreCase("!anvil")) {
            BotCommands.spawnAnvil();
        }
        else if (message.equalsIgnoreCase("!creeper")) {
            BotCommands.spawnCreeper();
        }
        else if (message.equalsIgnoreCase("!lightning")) {
            BotCommands.spawnLightning();
        }
        else if (message.equalsIgnoreCase("!fireball")) {
        	BotCommands.spawnFireball();
        }
        else if (message.equalsIgnoreCase("!oresexplode") && !BotCommands.oresExplode) {
            BotCommands.oresExplode = true;
        }
        
        // If command is invalid
        else {
        	if (newChats.size() > 1) {
        		// Choose another if the list is big enough
        		newChats.remove(listRandom);
        		pickRandomChat();
        	}
        	else {
        		newChats.clear();
        		return;
        	}
        }
        
        if (Config.showChatMessages) {
        	PrintToChat.print(TextFormatting.AQUA, "Command Chosen: " + message);
        }
        
        newChats.clear();

    }
	
}
