# Twitch Vs Minecraft
[![Build status](https://ci.appveyor.com/api/projects/status/9b887bfebvnuvixy?svg=true)](https://ci.appveyor.com/project/iCrazyBlaze/twitchvsminecraft)
[![](http://cf.way2muchnoise.eu/full_twitch-vs-minecraft_downloads.svg)](https://minecraft.curseforge.com/projects/twitch-vs-minecraft)

A Minecraft mod for Forge inspired by [Kaze Emanuar](https://www.youtube.com/channel/UCuvSqzfO_LV_QzHdmEj84SQ) that lets Twitch viewers interact with the game to provide a fun challenge for streamers.

[View the project on CurseForge for more info!](https://minecraft.curseforge.com/projects/twitch-vs-minecraft)

# How it works
This mod integrates PircBot, a Java IRC API. It uses PircBot to connect to Twitch's IRC server and read a Twitch channel's chat. Every time a new chat message is recieved that isn't blacklisted, it is added to a list. Every 20 seconds (this can be changed), a random message from the list is chosen, and if it's a valid command, e.g. "!creeper", the list of new chat messages will be cleared, the timer will restart and the command will be executed.

# Twitch OAuth key
As stated on CurseForge, you will need a Twitch OAuth key. You can get this [here.](https://twitchapps.com/tmi)

You should keep this key private and safe. This key is stored in the mod's config file - **DO NOT** share this file with others!

Follow the instructions on the TwitchApps page for how to revoke access to the Twitch API if you want to stay extra safe.

# Getting started
To setup a modding workspace using eclipse, use
```
./gradlew setupDecompWorkspace
./gradlew eclipse
```

If you're using [IntelliJ IDEA](https://www.jetbrains.com/idea/), choose **"import project"** from the main screen and import the mod's `build.gradle` file. You are then able to run `setupDecompWorkspace` from the **Gradle** tab on the right of the screen, or from a terminal. Use `genIntellijRuns` to generate run configurations.

When moving over to IDEA from Eclipse, follow the above steps and then run `cleanEclipse` from the Gradle tab to remove all of Eclipse's files.

# Building from source
To build the project using a terminal, type
```
./gradlew build
```
Or find it in the Gradle tab in IDEA.

The build will be located in the **build/libs** folder, alongside the "sources" file. **The sources file is not a mod!**
