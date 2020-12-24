About
=====
*Tale of Kingdoms: A new Conquest* is a revival of the Tale of Kingdoms mod. Currently we are in pre-alpha. Content seen is based on original versions.

Links
=====
Website: https://www.convallyria.com

Discord: https://discord.gg/fh62mxU

FAQ
=====
Q: Who is working on it?
A: There are only a few people working on this. As such, this is going to take quite a while! Contributions are extremely welcome.

Q: How can I help?
A: To see what needs help with, check out the open [issues](https://gitlab.com/SamB440/tale-of-kingdoms/-/issues). Clone the repository and make a [merge request](https://gitlab.com/SamB440/tale-of-kingdoms/-/merge_requests).

Q: How do I install it?
A: You cannot currently install it. The mod is in pre-alpha. If you want, you can download the source code and compile it yourself.

Q: Will you be adding more to the mod?
A: Yes, we will be adding more to the mod. This page will only contain the core mod, allowing other mods to interact and add to it. Some addons will be made to the base mod that you will be able to download for free.

Q: Where can I donate?
A: Currently you cannot donate simply because I have not set anything up. However, in the future you will be able to.

Q: What versions will it be for?
A: As I am the only one working on it, only 1.15+ will be supported.

Compiling
=======
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

Run the `runClient` task by pressing `ALT + SHIFT + F9`, going to `tale-of-kingdoms [runClient]`, pressing the arrow and selecting debug. See things change as you code! Do not place WorldEdit in the mods folder - it's handled 
by the `build.gradle`, however in some cases it is needed (?). You can try other mods but they are not guaranteed to work in a development environment.

Upgrading textures
=====
Some textures are in the old 64x32 format. 1.9+ uses 64x64 for skin textures.
Upgrading these skins can be done by going to https://www.minecraftskins.com/skin-editor/,
selecting "upload from computer" then "download".

Minecraft Server
=======
IslandEarth - https://www.islandearth.net
