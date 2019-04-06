package com.icrazyblaze.twitchmod.util;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.chat.ChatPicker;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;

public class TickHandler {

	public static int chatTicks = 0;
	
	public static int chatSecondsDefault = 20;
	public static int chatSeconds = chatSecondsDefault;
	
	public static int secondsTicks = 0;
	public static int secondsLeft = 60;
	
	public static boolean killTimer = false;
	
    @SubscribeEvent
    public void tickTimer(TickEvent.PlayerTickEvent event) {        		

        if (event.phase == TickEvent.Phase.END && event.player != null) {

            chatTicks++;
            
            if (chatTicks == 40) {
            	
            	if (chatSeconds > 0) {
            		chatSeconds--;
            	}
                
                chatTicks = 0;
            }
            if (chatSeconds == 0) {
                if (ChatPicker.newChats.size() > 0) {
                	ChatPicker.pickRandomChat();
                	
                	chatSeconds = chatSecondsDefault;
                }

                chatTicks = 0;
            }
            
            if (killTimer) {
	            secondsTicks++;
	            
	            if (secondsTicks == 40) {
	            	
	            	if (secondsLeft > 0) {
	            		secondsLeft--;
	            	}
	                
	                secondsTicks = 0;
	            }
	            if (secondsLeft == 0) {
	                BotCommands.killPlayer();
	                killTimer = false;
	            }
            }
            
        }
    }
	
}
