package com.icrazyblaze.twitchmod.chat;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.irc.BotConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * This class is responsible for picking commands from chat and running them.
 */
public class ChatPicker {

    public static List<String> blacklist;
    public static ArrayList<String> newChats = new ArrayList<>();
    public static ArrayList<String> newChatSenders = new ArrayList<>();
    private static Path path = Paths.get(Minecraft.getMinecraft().gameDir.getPath(), "config/twitch-blacklist.txt");
    private static File textfile;

    private static Map<String, Runnable> commands = new HashMap<>();

    private static boolean hasExecuted = false;

    public static boolean cooldownEnabled = false;
    private static String lastCommand = null;

    public static boolean forcecommands = false;

    /**
     * Loads the blacklist file, or creates the file if it doesn't already exist.
     */
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

    /**
     * @param toAdd The string to add to the blacklist file.
     */
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

    /**
     * Clears the blacklist file.
     */
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

    /**
     * Checks the command against the blacklist, unless force commands is enabled.
     * @param message The chat message
     * @param sender The sender's name
     * @param forceCommands Should the command ignore the blacklist and run anyway?
     */
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
                    Main.logger.info("Command not executed: command is blacklisted.");
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
        else {

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


    /**
     * Picks a random chat message, and checks if it is a command.
     * If the chat message is a command, it will be run. Otherwise, a new message is picked.
     */
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

    /**
     * Adds a command to a list that ChatPicker checks.
     * The registerCommand method takes two arguments: a runnable, and any number of command aliases.
     * <pre>
     * {@code
     *     registerCommand(() -> BotCommands.myCommand(), "mycommand", "mycommandalias");
     * }
     * </pre>
     * IDEA will swap the lambda for a method reference wherever possible.
     * @param runnable The function linked to the command
     * @param keys Aliases for the command
     */
    public static void registerCommand(Runnable runnable, String... keys) {

        /*
        This code is used to add multiple aliases for commands using hashmaps.
        Thank you gigaherz, very cool!
        */
        for(String key : keys) {
            commands.put(key, runnable);
        }

    }
    /**
     * Attempts to run a command.
     * @param message The actual command, e.g. "!creeper"
     * @param sender The sender's name, which is used in some commands.
     * @return If the command doesn't run, then this method returns false.
     */
    public static boolean doCommand(String message, String sender) {

        commands.clear(); // Prevent duplication when registering commands (oops)
        registerCommand(BotCommands::addPoison, "poison");
        registerCommand(BotCommands::addHunger, "hunger");
        registerCommand(BotCommands::addSlowness, "slowness");
        registerCommand(BotCommands::addSpeed, "speed", "gottagofast");
        registerCommand(BotCommands::addNausea, "nausea", "dontfeelsogood");
        registerCommand(BotCommands::addFatigue, "fatigue");
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
        registerCommand(BotCommands::setSpawn, "spawnpoint", "setspawn");

        // Special commands below need to be length checked, so they cannot be registered in the normal way.
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
