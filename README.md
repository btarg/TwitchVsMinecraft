# Twitch Vs Minecraft
A Minecraft mod for Forge inspired by [Kaze Emanuar](https://www.youtube.com/channel/UCuvSqzfO_LV_QzHdmEj84SQ) that lets Twitch viewers interact with the game to provide a fun challenge for streamers.

[View the project on CurseForge for more info!](https://minecraft.curseforge.com/projects/twitch-vs-minecraft)

# How it works
This mod integrates PircBot, a Java IRC API. It uses PircBot to connect to Twitch's IRC server and read a Twitch channel's chat. Every time a new chat message is recieved that starts with "!", it is added to a list. Every 20 seconds (this can be changed), a random message from the list is chosen, and if it's a valid command, e.g. "!creeper", the list of new chat messages will be cleared, the timer will restart and the command will be executed.

# Twitch OAuth key
As stated on CurseForge, you will need a Twitch OAuth key. You can get this [here.](https://twitchapps.com/tmi)

You should keep this key private and safe. This key is stored in the mod's config file - **DO NOT** share this file with others!

Follow the instructions on the TwitchApps page for how to revoke access to the Twitch API if you want to stay extra safe.

# Building from source
To setup a modding workspace, use
```
./gradlew setupDecompWorkspace
./gradlew eclipse
```

To build the project, use
```
./gradlew build
```
The build will be located in the **build/libs** folder.
