package com.icrazyblaze.twitchmod.chat;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.irc.BotConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.*;
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
import java.util.*;


public class ChatPicker {

    public static List<String> blacklist;
    public static ArrayList<String> newChats = new ArrayList<>();
    private static ArrayList<String> newChatSenders = new ArrayList<>();
    private static Path path = Paths.get(Minecraft.getMinecraft().gameDir.getPath(), "config/twitch-blacklist.txt");
    private static File textfile;

    private static Map<String, Runnable> commands = new HashMap<>();

    private static boolean hasExecuted = false;

    public static boolean cooldownEnabled = false;
    private static String lastCommand = null;


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

            // New line fix
            fr.write(System.lineSeparator() + toAdd);

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

    public static void checkChat(String message, String sender, boolean forceCommands) {

        // Skip checking if force commands is enabled
        if (forceCommands) {

            doCommand(message, sender);
            return;

        }

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

            } else if (BotConfig.showChatMessages && BotConfig.showCommands) {

                BotCommands.player().sendMessage(new TextComponentString(TextFormatting.AQUA + "Command Chosen: " + BotConfig.prefix + message));

            }

            newChats.clear();

        }

    }

    static void registerCommand(Runnable runnable, String... keys) {

        /*
        This code is used to add multiple aliases for commands using hashmaps.
        Thank you gigaherz, very cool!
        */
        for(String key : keys) {
            commands.put(key, runnable);
        }

    }

    public static boolean doCommand(String message, String sender) {

        /*
        This is where messages from Twitch chat are checked.
        If the command doesn't run this method returns false.

        The registerCommand method takes two arguments: a runnable, and any number of command aliases.

        registerCommand(() -> BotCommands.myCommand(), "mycommand", "mycommandalias");
         */

        registerCommand(BotCommands::addPoison, "poison");
        registerCommand(BotCommands::addHunger, "hunger");
        registerCommand(BotCommands::addSlowness, "slowness");
        registerCommand(BotCommands::addSpeed, "speed", "gottagofast");
        registerCommand(BotCommands::addNausea, "nausea", "dontfeelsogood");
        registerCommand(BotCommands::addLevitation, "levitate", "fly");
        registerCommand(BotCommands::noFall, "nofall", "float");
        registerCommand(BotCommands::addWeakness, "weakness");
        registerCommand(BotCommands::addRegen, "regen", "heal", "health");
        registerCommand(BotCommands::addJumpBoost, "jumpboost", "yeet");
        registerCommand(BotCommands::setOnFire, "fire", "burn");
        registerCommand(BotCommands::floorIsLava, "lava", "floorislava");
        registerCommand(BotCommands::deathTimer, "timer", "deathtimer");
        registerCommand(BotCommands::drainHealth, "drain", "halfhealth");
        registerCommand(BotCommands::spawnAnvil, "anvil"); // Gaiet's favourite command <3
        registerCommand(() -> BotCommands.spawnMobBehind(new EntityCreeper(BotCommands.player().world)), "creeper", "awman");
        registerCommand(() -> BotCommands.spawnMobBehind(new EntityZombie(BotCommands.player().world)), "zombie");
        registerCommand(() -> BotCommands.spawnMob(new EntityEnderman(BotCommands.player().world)), "enderman");
        registerCommand(() -> BotCommands.spawnMobBehind(new EntityWitch(BotCommands.player().world)), "witch");
        registerCommand(() -> BotCommands.spawnMobBehind(new EntitySkeleton(BotCommands.player().world)), "skeleton");
        registerCommand(BotCommands::creeperScare, "creeperscare", "behindyou");
        registerCommand(BotCommands::zombieScare, "zombiescare", "bruh");
        registerCommand(BotCommands::skeletonScare, "skeletonscare", "spook");
        registerCommand(BotCommands::witchScare, "witchscare");
        registerCommand(BotCommands::spawnLightning, "lightning");
        registerCommand(BotCommands::spawnFireball, "fireball");
        registerCommand(() -> BotCommands.oresExplode = true, "oresexplode");
        registerCommand(() -> BotCommands.placeBedrock = true, "bedrock");
        registerCommand(BotCommands::waterBucket, "water", "watersbroke");
        registerCommand(BotCommands::breakBlock, "break");
        registerCommand(BotCommands::dismount, "dismount", "getoff");
        registerCommand(BotCommands::dropItem, "drop", "throw");
        registerCommand(BotCommands::monsterEgg, "silverfish");
        registerCommand(BotCommands::heavyRain, "rain", "shaun");
        registerCommand(() -> BotCommands.setDifficulty(EnumDifficulty.HARD), "hardmode", "isthiseasymode");
        registerCommand(() -> BotCommands.setDifficulty(EnumDifficulty.PEACEFUL), "peaceful", "peacefulmode");
        registerCommand(BotCommands::placeChest, "chest", "lootbox");
        registerCommand(() -> BotCommands.setTime(1000), "day", "setday");
        registerCommand(() -> BotCommands.setTime(13000), "night", "setnight");
        registerCommand(() -> BotCommands.messWithInventory(sender), "itemroulette", "roulette");
        registerCommand(BotCommands::spawnCobweb, "cobweb", "stuck", "gbj");


        try {

            if (message.startsWith("messagebox ") && message.length() > 11) {
                BotCommands.showMessagebox(message);
            } else if (message.startsWith("addmessage ") && message.length() > 11) {
                BotCommands.addToMessages(message);
            } else if (message.startsWith("sign ") && message.length() > 5) {
                BotCommands.placeSign(message);
            } else if (message.startsWith("rename ") && message.length() > 7) {
                BotCommands.renameItem(message);
            } else {

                // Invoke command from message
                commands.get(message).run();

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
