package com.icrazyblaze.twitchmod.commands;

import java.util.ArrayList;
import java.util.List;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.pircbot.Config;
import com.icrazyblaze.twitchmod.util.ConfigSaveLoad;
import com.icrazyblaze.twitchmod.util.TickHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class TTVCommand extends CommandBase {

    private final List aliases;
	
	public TTVCommand() {
        aliases = new ArrayList(); 
        aliases.add("twitch");
	}
	
	@Override
	public String getName() {
		return "ttv";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "/ttv <key/channel> [OAuth key/channel name] OR /ttv <save/reload> OR /ttv showchat <true/false> OR /ttv seconds <seconds>";
	}
	
    @Override 
    public List getAliases()
    { 
        return this.aliases;
    } 

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
		
			if (args.length > 0) {
			
				if (args[0].equalsIgnoreCase("key") && args.length == 2) {
					Config.TWITCH_KEY = args[1];
					sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Set OAuth key."));	
					
				}
				else if (args[0].equalsIgnoreCase("channel") && args.length == 2) {
					Config.CHANNEL_NAME = args[1];
					sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Set channel name."));
				}
				else if (args[0].equalsIgnoreCase("connect")) {
					try {
						if (Config.isConnected) {
							sender.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + "Reonnecting..."));
						}
						else {
							sender.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + "Connecting..."));
						}

						Config.main();
						
					} catch (Exception e) {
						sender.sendMessage(new TextComponentString(TextFormatting.RED + e.toString()));
					}
				}
				else if (args[0].equalsIgnoreCase("save")) {
					ConfigSaveLoad.saveConfig();
					sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Saved configuration."));
				}
				else if (args[0].equalsIgnoreCase("reload")) {
					ConfigSaveLoad.loadConfig();
					sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Reloaded configuration."));
				}
				
				else if (args[0].equalsIgnoreCase("showchat") && args.length == 2) {
					
					if (args[1].equalsIgnoreCase("true")) {
						Config.showChatMessages = true;
						sender.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + "Chat is now shown."));
					}
					else if (args[1].equalsIgnoreCase("false")) {
						Config.showChatMessages = false;
						sender.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + "Chat is now hidden."));
					}
					
				}
				
				else if (args[0].equalsIgnoreCase("seconds") && args.length == 2) {
					
					try {
						
						int newInt = Integer.parseInt(args[1]);
						
						if (newInt >= 5 && newInt <= 120) {
							TickHandler.chatSecondsDefault = newInt;
							TickHandler.chatSeconds = newInt;
							sender.sendMessage(new TextComponentString(TextFormatting.AQUA + "The chat timer is now set to " + newInt + " seconds."));
						}
						else {
							sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid value."));
						}
						
					}
					catch (Exception e) {
						sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid value."));
					}
					
				}
				
				else if (args[0].equalsIgnoreCase("help")) {
					TextComponentString helpmessage = new TextComponentString(TextFormatting.RED + "Use '/help ttv' for usage, or click on this message to view a list of commands.");
					
					ClickEvent goLinkEvent = new ClickEvent(ClickEvent.Action.OPEN_URL,"http://bit.ly/2UfBCiL");
					HoverEvent goHoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to open the link in your browser"));
					helpmessage.getStyle().setClickEvent(goLinkEvent);
					helpmessage.getStyle().setHoverEvent(goHoverEvent);
					
					sender.sendMessage(helpmessage);
				}
				
				
				
				else {
					throw new WrongUsageException(getUsage(sender), new Object[0]);
				}
							

			}
			else {
				
				if (Config.isConnected) {
					sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Bot is connected."));
				}
				else {
					sender.sendMessage(new TextComponentString(TextFormatting.RED + "Bot not connected."));
				}
				
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Channel name: " + Config.CHANNEL_NAME));
				sender.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + "A new command will be chosen every " + TickHandler.chatSecondsDefault + " seconds."));
				
				TextComponentString keyMessage = new TextComponentString(TextFormatting.AQUA + "Click here to get your Twitch OAuth key!");
				
				ClickEvent goLinkEvent = new ClickEvent(ClickEvent.Action.OPEN_URL,"https://twitchapps.com/tmi/");
				HoverEvent goHoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to open the link in your browser"));
				keyMessage.getStyle().setClickEvent(goLinkEvent);
				keyMessage.getStyle().setHoverEvent(goHoverEvent);
				
				sender.sendMessage(keyMessage);
				
			}
		}
	}

}
