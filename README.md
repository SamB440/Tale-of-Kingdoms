About
=====
*Tale of Kingdoms: A new Conquest* is a revival of the Tale of Kingdoms mod. Currently we are in pre-alpha. Content seen is based on original versions.

Links
=====
Website: https://www.islandearth.net/taleofkingdoms

Discord: https://discord.gg/fh62mxU

FAQ
=====
Q: Who is working on it?

A: I am the only developer working on this. As such, this is going to take quite a while! Contributions are extremely welcome.

Q: How do I install it?

A: You cannot currently install it. The mod is in pre-alpha. If you want, you can download the source code and compile it yourself.

Q: Will you be adding more to the mod?

A: Yes, we will be adding more to the mod. This page will only contain the core mod, allowing other mods to interact and add to it. Some "DLC" will be free, and some paid.

Q: Where can I donate?

A: Currently you cannot donate simply because I have not set anything up. However, in the future you will be able to, along with purchasing DLC.

Q: What versions will it be for?

A: As I am the only one working on it, only 1.14 will be supported until I find other developers to work on 1.12. 1.13 will never be supported as it is extremely buggy.

Forge Installation
==================
Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

Source pack installation information:

Standalone source installation
-------------------------------------------

See the Forge Documentation online for more detailed instructions:
http://mcforge.readthedocs.io/en/latest/gettingstarted/

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: You're left with a choice.
If you prefer to use Eclipse:
1. Run the following command: "gradlew genEclipseRuns" (./gradlew genEclipseRuns if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder 
   or run "gradlew eclipse" to generate the project.
(Current Issue)
4. Open Project > Run/Debug Settings > Edit runClient and runServer > Environment
5. Edit MOD_CLASSES to show [modid]%%[Path]; 2 times rather then the generated 4.

If you prefer to use IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: "gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not affect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.
or the Forge Project Discord discord.gg/UvedJ9m

Forge source installation
-------------------------------------------
MinecraftForge ships with this code and installs it as part of the forge
installation process, no further action is required on your part.

LexManos' Install Video
-------------------------------------------
https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be

For more details update more often refer to the Forge Forums:
http://www.minecraftforge.net/forum/index.php/topic,14048.0.html
