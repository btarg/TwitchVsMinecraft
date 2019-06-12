package com.icrazyblaze.twitchmod.chat;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.irc.BotConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatPicker {

    public static List<String> blacklist;
    public static ArrayList<String> newChats = new ArrayList<String>();
    public static ArrayList<String> newChatSenders = new ArrayList<String>();
    static Path path = Paths.get(Minecraft.getMinecraft().mcDataDir.getPath(), "config/twitch-blacklist.txt");

    public static void loadBlacklistFile() {
        File textfile = new File(path.toString());

        try {
            textfile.createNewFile(); // Create file if it doesn't already exist
            blacklist = Files.readAllLines(path); // Read into list

        } catch (IOException e) {
            Main.logger.error(e);
        }

    }

    public static void checkChat(String message, String sender) {
        if (!blacklist.isEmpty()) {
            for (String str : blacklist) {
                if (message.contains(str)) {
                    break;
                } else {
                    newChats.add(message);
                    newChatSenders.add(sender);
                    break;
                }
            }
        }
        // Fix for empty blacklist bug
        else {
            if (message.startsWith("!")) {
                newChats.add(message);
                newChatSenders.add(sender);
            }
        }
    }


    public static void pickRandomChat() {
        String message;
        String sender;
        Random rand = new Random();
        int listRandom = rand.nextInt(newChats.size());

        message = newChats.get(listRandom);
        sender = newChatSenders.get(listRandom);


        if (message.equalsIgnoreCase("!poison")) {
            BotCommands.addPoison();
        } else if (message.equalsIgnoreCase("!hunger")) {
            BotCommands.addHunger();
        } else if (message.equalsIgnoreCase("!slowness")) {
            BotCommands.addSlowness();
        } else if (message.equalsIgnoreCase("!speed") || message.equalsIgnoreCase("!gottagofast")) {
            BotCommands.addSpeed();
        } else if (message.equalsIgnoreCase("!nausea") || message.equalsIgnoreCase("!dontfeelsogood")) {
            BotCommands.addNausea();
        } else if (message.equalsIgnoreCase("!levitate") || message.equalsIgnoreCase("!fly")) {
            BotCommands.addLevitation();
        } else if (message.equalsIgnoreCase("!nofall")) {
            BotCommands.noFall();
        } else if (message.equalsIgnoreCase("!regen") || message.equalsIgnoreCase("!health")) {
            BotCommands.addRegen();
        } else if (message.equalsIgnoreCase("!fire")) {
            BotCommands.setOnFire();
        } else if (message.equalsIgnoreCase("!lava") || message.equalsIgnoreCase("!floorislava")) {
            BotCommands.floorIsLava();
        } else if (message.equalsIgnoreCase("!deathtimer") || message.equalsIgnoreCase("!timer")) {
            BotCommands.deathTimer();
        } else if (message.startsWith("!messagebox ") && message.length() > 12) {
            BotCommands.showMessagebox(message);
        } else if (message.startsWith("!sign ") && message.length() > 6) {
            BotCommands.placeSign(message);
        } else if (message.equalsIgnoreCase("!anvil")) {
            BotCommands.spawnAnvil();
        } else if (message.equalsIgnoreCase("!creeper")) {
            BotCommands.spawnCreeper();
        } else if (message.equalsIgnoreCase("!creeperscare") || message.equalsIgnoreCase("!behindyou")) {
            BotCommands.creeperScare();
        } else if (message.equalsIgnoreCase("!lightning")) {
            BotCommands.spawnLightning();
        } else if (message.equalsIgnoreCase("!fireball")) {
            BotCommands.spawnFireball();
        } else if (message.equalsIgnoreCase("!oresexplode") && !BotCommands.oresExplode) {
            BotCommands.oresExplode = true;
        } else if (message.equalsIgnoreCase("!bedrock") && !BotCommands.placeBedrock) {
            BotCommands.placeBedrock = true;
        } else if (message.equalsIgnoreCase("!break")) {
            BotCommands.breakBlock();
        } else if (message.equalsIgnoreCase("!water") || message.equalsIgnoreCase("!watersbroke")) {
            BotCommands.waterBucket();
        } else if (message.equalsIgnoreCase("!dismount") || message.equalsIgnoreCase("!getoff")) {
            BotCommands.dismount();
        }

        // If command is invalid
        else {
            if (newChats.size() > 1) {
                // Choose another if the list is big enough
                newChats.remove(listRandom);
                pickRandomChat();
            } else {
                newChats.clear();
                return;
            }
        }

        if (BotConfig.showChatMessages) {
            BotCommands.player().sendMessage(new TextComponentString(TextFormatting.AQUA + "Command Chosen: " + message));
        }

        newChats.clear();

    }

}
