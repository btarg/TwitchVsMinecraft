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
    public void tickTimer(TickEvent.ServerTickEvent event) {

        if (event.phase == TickEvent.Phase.END && enabled) {

            chatTicks++;

            if (chatTicks == 20) { // 20 serverticks = 1 second

                if (chatSeconds > 0) {
                    chatSeconds--; // Seconds left decreases by 1
                }

                chatTicks = 0;
            }
            if (chatSeconds == 0) {

                ChatPicker.pickRandomChat();

                // Reset timer
                chatSeconds = chatSecondsDefault;
                chatTicks = 0;

            }

            if (killTimer) {

                timerTicks++;

                if (timerTicks == 20) {

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
