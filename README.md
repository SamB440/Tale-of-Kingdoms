About
=====
*Tale of Kingdoms: A new Conquest* is a revival of the Tale of Kingdoms mod. 
Currently we are in alpha. Content seen is based on original versions.

Links
=====
Website: https://www.convallyria.com

Discord: https://discord.gg/fh62mxU

Server (Coming soon): beta.islandearth.net

FAQ
=====
Q: Who is working on it? 
A: There are only a few people working on this. As such, this is going to take quite a while! Contributions are extremely welcome.

Q: How can I help? 
A: To see what needs help with, check out the open issues. Clone the repository and make a merge request.

Q: Will you be adding more to the mod? 
A: Yes, we will be adding more to the mod.

Q: Where can I donate? 
A: Currently you cannot donate simply because I have not set anything up. However, in the future you will be able to.

Q: What versions will it be for? 
A: As only a few people are working on it, only 1.16+ and Fabric will be supported.

Compiling
=======
Tale of Kingdoms builds on JDK 16 and uses Gradle.

`git clone https://gitlab.com/SamB440/tale-of-kingdoms.git`

`cd tale-of-kingdoms`

`./gradlew build`

Jar file is at `build/libs/tale-of-kingdoms-1.0.0.jar`

Hot-swapping
=======
To enable hot-swapping, do the following (in IntelliJ):
1. Use `CTRL + ALT + S`
2. Go to `Build, Execution, Deployment > Debugger > HotSwap`
3. Check `enable HotSwap`, `Build project before reloading classes`
4. Uncheck `enable JVM will hang warning`
5. Set reload classes after compilation to `always`.

You can reload a class by right clicking it in the explorer then selecting 'Recompile x.class'

Run the `runClient` task by pressing `ALT + SHIFT + F9`, going to `tale-of-kingdoms [runClient]`, pressing the arrow and selecting debug. 
See things change as you code! Do **not** place WorldEdit in the `run/mods` folder - it's handled 
by the `build.gradle`. You can try other mods but they are not guaranteed to work in a development environment.

Upgrading textures
=====
Some textures are in the old 64x32 format. 1.9+ uses 64x64 for skin textures.
Upgrading these skins can be done by going to https://www.minecraftskins.com/skin-editor/,
selecting "upload from computer" then "download".

Minecraft Server
=======
IslandEarth - https://www.islandearth.net

Copyright
=====
Code is GNU GPL v3 unless otherwise specified in header of class file.

All other content is subject to their own licence. Builds are All Rights Reserved by Convallyria. Music is All Rights Reserved by the author(s) and is subject to the terms made by the author(s).
