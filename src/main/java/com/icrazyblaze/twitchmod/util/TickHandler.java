package com.icrazyblaze.twitchmod.util;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.chat.ChatPicker;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class TickHandler {

    public static boolean enabled = true;

    public static int chatTicks = 0;
    public static int chatSecondsDefault = 30;
    public static int chatSeconds = chatSecondsDefault;

    public static int timerTicks = 0;
    public static int timerSeconds = 60;
    public static boolean killTimer = false;

    public static int messageTicks = 0;
    public static int messageSecondsDefault = 300;
    public static int messageSeconds = messageSecondsDefault;

    @SubscribeEvent
    public void tickTimer(TickEvent.ServerTickEvent event) {

        /*
        This method is used for timers such as the death timer as thread.sleep cannot be called while playing.
        Countdown timers look like this:
        ===================================================================================

        if (condition) {

            ticks++;

            if (ticks == 20) {

                if (seconds > 0) {
                    seconds--;
                }

                ticks = 0;

                if (seconds == 0) {
                    // do something
                    seconds = defaultAmountOfSeconds;
                    ticks = 0;
                }

            }

        }

        ===================================================================================
        Timers are set and reset externally by changing the condition.
        The example can also be configured to count up.

         */

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

            messageTicks++;

            if (messageTicks == 20) {

                if (messageSeconds < messageSecondsDefault) {
                    messageSeconds++;
                }

                messageTicks = 0;

            }
            if (messageSeconds == messageSecondsDefault) {

                BotCommands.chooseRandomMessage();

                messageTicks = 0;
                messageSeconds = 0;

            }
        }
    }

}
