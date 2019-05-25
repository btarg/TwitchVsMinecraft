package com.icrazyblaze.twitchmod.util;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.chat.ChatPicker;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickHandler {

    public static boolean enabled = true;

    public static int chatTicks = 0;
    public static int chatSecondsDefault = 20;
    public static int chatSeconds = chatSecondsDefault;

    public static int timerTicks = 0;
    public static int timerSeconds = 60;

    public static boolean killTimer = false;

    @SubscribeEvent
    public void tickTimer(TickEvent.PlayerTickEvent event) {

        if (event.phase == TickEvent.Phase.END && event.player != null && enabled) {

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
                timerTicks++;

                if (timerTicks == 40) {

                    if (timerSeconds > 0) {
                        timerSeconds--;
                    }

                    timerTicks = 0;
                }
                if (timerSeconds == 0) {
                    BotCommands.killPlayer();
                    killTimer = false;
                }
            }

        }
    }

}
