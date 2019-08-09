package com.icrazyblaze.twitchmod.chat;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.irc.BotConfig;
import com.icrazyblaze.twitchmod.irc.BotConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChatPicker {

    public static List<String> blacklist;
    public static ArrayList<String> newChats = new ArrayList<>();
    public static ArrayList<String> newChatSenders = new ArrayList<>();
    public static Path path = Paths.get(Minecraft.getMinecraft().mcDataDir.getPath(), "config/twitch-blacklist.txt");
    public static File textfile;

    public static boolean hasExecuted = false;

    public static boolean cooldownEnabled = false;
    public static String lastCommand = null;


    public static void loadBlacklistFile() {

        textfile = new File(path.toString());
        try {

            textfile.createNewFile(); // Create file if it doesn't already exist
            blacklist = Files.readAllLines(path); // Read into list

        } catch (IOException e) {
            Main.logger.error(e);
        }

        // Fix for blacklist being null - set to empty instead
        if (blacklist == null) {
            blacklist = Collections.emptyList();
        }

    }

    public static void addToBlacklist(String toAdd) {

        try {
            // Append to file
            FileWriter fr = new FileWriter(textfile, true);
            fr.write(toAdd);
            fr.close();

            // Update from file
            loadBlacklistFile();

        } catch (IOException e) {
            Main.logger.error(e);
        }

    }

    public static void clearBlacklist() {

        try {
            // Clear text file using PrintWriter
            PrintWriter pr = new PrintWriter(textfile);
            pr.close();

            // Update from file
            loadBlacklistFile();

        } catch (IOException e) {
            Main.logger.error(e);
        }

    }

    public static void checkChat(String message, String sender) {

        // Only add the message if it is not blacklisted, and if the command isn't the same as the last

        loadBlacklistFile();

        if (!blacklist.isEmpty()) {

            for (String str : blacklist) {

                if (str.contains(message)) {
                    break;

                } else {

                    if (lastCommand != null && cooldownEnabled) {

                        if (!message.equalsIgnoreCase(lastCommand)) {

                            newChats.add(message);
                            newChatSenders.add(sender);
                            break;

                        } else {
                            Main.logger.info("Command not executed: cooldown is active for this command.");
                            break;
                        }

                    } else {

                        newChats.add(message);
                        newChatSenders.add(sender);
                        break;

                    }
                }
            }

        }
        // Fix for empty blacklist bug: accept any message (also runs cooldown check)
        else if (blacklist.isEmpty()) {

            if (lastCommand != null && cooldownEnabled) {

                if (!message.equalsIgnoreCase(lastCommand)) {

                    newChats.add(message);
                    newChatSenders.add(sender);

                } else {
                    Main.logger.info("Command not executed: cooldown is active for this command.");
                }

            } else {

                newChats.add(message);
                newChatSenders.add(sender);

            }

        }

    }


    public static void pickRandomChat() {

        if (!newChats.isEmpty()) {

            String message;
            String sender;
            Random rand = new Random();
            int listRandom = rand.nextInt(newChats.size());

            message = newChats.get(listRandom);
            sender = newChatSenders.get(listRandom);

            hasExecuted = doCommand(message, sender);

            // If command is invalid
            if (!hasExecuted) {

                newChats.remove(listRandom);
                commandFailed();

            } else if (BotConfig.showChatMessages) {

                BotCommands.player().sendMessage(new TextComponentString(TextFormatting.AQUA + "Command Chosen: " + BotConfig.prefix + message));

            }

            newChats.clear();

        }

    }

    public static boolean doCommand(String message, String sender) {

        try {
            if (message.equalsIgnoreCase("poison")) {
                BotCommands.addPoison();
            } else if (message.equalsIgnoreCase("hunger")) {
                BotCommands.addHunger();
            } else if (message.equalsIgnoreCase("slowness")) {
                BotCommands.addSlowness();
            } else if (message.equalsIgnoreCase("speed") || message.equalsIgnoreCase("gottagofast")) {
                BotCommands.addSpeed();
            } else if (message.equalsIgnoreCase("nausea") || message.equalsIgnoreCase("dontfeelsogood")) {
                BotCommands.addNausea();
            } else if (message.equalsIgnoreCase("levitate") || message.equalsIgnoreCase("fly")) {
                BotCommands.addLevitation();
            } else if (message.equalsIgnoreCase("nofall")) {
                BotCommands.noFall();
            } else if (message.equalsIgnoreCase("weakness")) {
                BotCommands.addWeakness();
            } else if (message.equalsIgnoreCase("fatigue")) {
                BotCommands.addFatigue();
            } else if (message.equalsIgnoreCase("regen") || message.equalsIgnoreCase("health")) {
                BotCommands.addRegen();
            } else if (message.equalsIgnoreCase("jumpboost") || message.equalsIgnoreCase("yeet")) {
                BotCommands.addJumpBoost();
            } else if (message.equalsIgnoreCase("fire") || message.equalsIgnoreCase("burn")) {
                BotCommands.setOnFire();
            } else if (message.equalsIgnoreCase("lava") || message.equalsIgnoreCase("floorislava")) {
                BotCommands.floorIsLava();
            } else if (message.equalsIgnoreCase("deathtimer") || message.equalsIgnoreCase("timer")) {
                BotCommands.deathTimer();
            } else if (message.equalsIgnoreCase("drain") || message.equalsIgnoreCase("halfhealth")) {
                BotCommands.drainHealth();
            } else if (message.startsWith("messagebox ") && message.length() > 11) {
                BotCommands.showMessagebox(message);
            } else if (message.startsWith("sign ") && message.length() > 5) {
                BotCommands.placeSign(message);
            } else if (message.equalsIgnoreCase("anvil")) {
                BotCommands.spawnAnvil();
            } else if (message.equalsIgnoreCase("creeper")) {
                Entity ent = new EntityCreeper(BotCommands.player().world);
                BotCommands.spawnMob(ent);
            } else if (message.equalsIgnoreCase("zombie")) {
                Entity ent = new EntityZombie(BotCommands.player().world);
                BotCommands.spawnMob(ent);
            } else if (message.equalsIgnoreCase("skeleton")) {
                Entity ent = new EntitySkeleton(BotCommands.player().world);
                BotCommands.spawnMob(ent);
            } else if (message.equalsIgnoreCase("creeperscare") || message.equalsIgnoreCase("behindyou")) {
                BotCommands.creeperScare();
            } else if (message.equalsIgnoreCase("zombiescare")) {
                BotCommands.zombieScare();
            } else if (message.equalsIgnoreCase("skeletonscare")) {
                BotCommands.skeletonScare();
            } else if (message.equalsIgnoreCase("lightning")) {
                BotCommands.spawnLightning();
            } else if (message.equalsIgnoreCase("fireball")) {
                BotCommands.spawnFireball();
            } else if (message.equalsIgnoreCase("oresexplode") && !BotCommands.oresExplode) {
                BotCommands.oresExplode = true;
            } else if (message.equalsIgnoreCase("bedrock") && !BotCommands.placeBedrock) {
                BotCommands.placeBedrock = true;
            } else if (message.equalsIgnoreCase("break")) {
                BotCommands.breakBlock();
            } else if (message.equalsIgnoreCase("water") || message.equalsIgnoreCase("watersbroke")) {
                BotCommands.waterBucket();
            } else if (message.equalsIgnoreCase("dismount") || message.equalsIgnoreCase("getoff")) {
                BotCommands.dismount();
            } else if (message.equalsIgnoreCase("drop") || message.equalsIgnoreCase("throw")) {
                BotCommands.dropItem();
            } else if (message.equalsIgnoreCase("silverfish")) {
                BotCommands.monsterEgg();
            } else if (message.equalsIgnoreCase("rain") || message.equalsIgnoreCase("shaun")) {
                BotCommands.heavyRain();
            } else if (message.equalsIgnoreCase("hardmode") || message.equalsIgnoreCase("isthiseasymode")) {
                BotCommands.setDifficulty(EnumDifficulty.HARD);
            } else if (message.equalsIgnoreCase("peaceful") || message.equalsIgnoreCase("peacefulmode")) {
                BotCommands.setDifficulty(EnumDifficulty.PEACEFUL);
            } else if (message.equalsIgnoreCase("chest") || message.equalsIgnoreCase("lootbox")) {
                BotCommands.placeChest();
            } else {
                return false;
            }

            // Below will not be executed if the command does not run

            lastCommand = message;

            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public static void commandFailed() {

        if (!hasExecuted) {
            if (!newChats.isEmpty()) {
                // Choose another if the list is big enough
                pickRandomChat();
            } else {
                newChats.clear();
                Main.logger.info("Failed to execute a command.");
                return;
            }
        }
    }

}
