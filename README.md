## Current development branch is [feature/tier_one](https://github.com/SamB440/Tale-of-Kingdoms/tree/feature/tier_one)
<br/>
<div align="center">
  <a href="https://github.com/SamB440/tale-of-kingdoms">
    <img src="https://github.com/SamB440/Tale-of-Kingdoms/blob/master/src/main/resources/assets/taleofkingdoms/logo.png?raw=true" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Tale of Kingdoms</h3>

  <p>
    The adventure RPG mod
    <br/>
    <br/>
    <a href="#getting-started"><strong>Explore the docs »</strong></a>
    <br/>
    <br/>
    <a href="/issues/new">Report Bug</a>
    .
    <a href="/issues/new">Request Feature</a>
  </p>

![Build](https://img.shields.io/github/actions/workflow/status/SamB440/Tale-of-Kingdoms/build.yml?branch=master)
![Forks](https://img.shields.io/github/forks/SamB440/Tale-of-Kingdoms) 
![Stargazers](https://img.shields.io/github/stars/SamB440/Tale-of-Kingdoms) 
![Issues](https://img.shields.io/github/issues/SamB440/Tale-of-Kingdoms) 
![License](https://img.shields.io/github/license/SamB440/Tale-of-Kingdoms)
[![Crowdin](https://badges.crowdin.net/tale-of-kingdoms/localized.svg)](https://crowdin.com/project/tale-of-kingdoms)
</div>

## Table Of Contents

* [About the Project](#about-the-project)
* [Getting Started](#getting-started)
    * [Prerequisites](#prerequisites)
    * [Installation](#installation)
    * [Developing](#developing)
        * [Hot-swapping](#hot-swapping)
        * [Upgrading textures](#upgrading-textures)
* [Usage](#usage)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [License](#license)
* [Authors](#authors)
* [Acknowledgements](#acknowledgements)
* [Links](#links)
* [FAQ](#faq)
* [Copyright](#copyright)

## About The Project

![Screen Shot](https://i.imgur.com/CB6vQ.png)

Tale of Kingdoms: A new Conquest is a revival of the Tale of Kingdoms mod for 1.16+. Currently we are in alpha. Content seen is based on original versions. 

Enter a dangerous realm of knights and honour, fight your way through the hordes of evil and forge your very own kingdom under your reign. Begin your adventure. Prove to the Guildmaster that you are worthy. Be the hero you were born for.

Please note that this mod is a complete recode unlike previous Tale of Kingdoms versions.

## Getting Started

Tale of Kingdoms builds on JDK 17 and uses Gradle to manage dependencies.

### Prerequisites

You just need Java 17. If you don't have it:

Latest from https://adoptopenjdk.net / https://adoptium.net, or;
```sh
sudo apt install adoptopenjdk-17-hotspot
```

### Installation

1. Clone the repo

```sh
git clone https://github.com/SamB440/Tale-of-Kingdoms.git
```

2. cd into project

```sh
cd tale-of-kingdoms
```

3. Run gradle build

```sh
./gradlew build
```

4. Get your jar file

Jar file is in `build/libs/tale-of-kingdoms-{VERSION}.jar`.

5. Install Fabric

Get it from https://fabricmc.net/use/

6. Install jar file to the mods folder

Put the tale of kingdoms jar file in your `.minecraft/mods` folder.

7. Run Minecraft!

### Developing

#### Hot-swapping
To enable hot-swapping, do the following (in IntelliJ):
1. Use `CTRL + ALT + S`
2. Go to `Build, Execution, Deployment > Debugger > HotSwap`
3. Check `enable HotSwap`, `Build project before reloading classes`
4. Uncheck `enable JVM will hang warning`
5. Set reload classes after compilation to `always`.

Then follow this tutorial: https://medium.com/@jonastm/jdk-17-with-dcevm-and-hotswapagent-4fee7095617a

**Important**: Make sure you download the correct JBR, it should have this format (note the **sdk**): ` jbrsdk_dcevm-17_0_1-windows-x64-b164.4.tar.gz`

Then you need to disable using Gradle for hot-swapping and instead use your IDE.

**IntelliJ**:
- `CTRL+ALT+S`
- `Build, Execution, Deployment > Build Tools > Gradle`
- `Build and run using: IntelliJ`
- `Run tests using: IntelliJ`

Now navigate to `Run > Edit Configurations`. Select `Application > Minecraft Client`. 
Add the `-XX:+AllowEnhancedClassRedefinition` flag, and the `-XX:HotswapAgent=fatjar` flag.

Ensure your `Working Directory` is set to the `run` folder in your project.

You then need to add mixin hot-swaps: https://fabricmc.net/wiki/tutorial:mixin_hotswaps

**Final Steps**
Ensure the Gradle JDK is set to JBR for the project.

You can now run the Minecraft Client task by going to `ALT+SHIFT+F9 > Minecraft Client`!

You can reload a class by doing `CTRL+SHIFT+F9` or right-clicking it in the explorer then selecting 'Recompile x.class'

Common issues:
* "Error opening zip file or JAR manifest missing": Your mixin hot-swaps link is outdated. Go through the "mixin hotswaps" tutorial again.

#### Upgrading textures
Some textures are in the old 64x32 format. 1.9+ uses 64x64 for skin textures.
Upgrading these skins can be done by going to https://www.minecraftskins.com/skin-editor/,
selecting "upload from computer" then "download".

## Roadmap

See the [open issues](/issues) for a list of proposed features (and known issues).

## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.
* If you have suggestions for adding or removing projects, feel free to [open an issue](/issues/new) to discuss it, or directly create a pull request after you edit the *README.md* file with necessary changes.
* Please make sure you check your spelling and grammar.
* Create individual PR for each suggestion.
* Please also read through the [Contributing file](CONTRIBUTING.md) before posting your first idea as well.

### Creating A Pull Request

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the GNU GPL v3 License. See [LICENSE](LICENSE.md) for more information.

## Authors

* **SamB440** - [SamB440](https://github.com/SamB440) - *Maintainer/creator*
* **JordanPlayz158** - [JordanPlayz158](https://github.com/jordanplayz158) - *Developer*
* **michaelb229** - [michaelb229](https://github.com/epicwarrior229) - *Maintainer*

## Acknowledgements

* Crowdin for providing us with an OS license
* Translators for their contributions
* Sheepguard for music and translations

## Links
Curseforge: https://www.curseforge.com/minecraft/mc-mods/tale-of-kingdoms-a-new-conquest

Website: https://www.convallyria.com

Discord: https://discord.gg/fh62mxU

Server (Coming soon): beta.islandearth.net

## FAQ
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

## Copyright
Code is GNU GPL v3 unless otherwise specified in header of class file.

All other content is subject to their own licence. Builds are All Rights Reserved by Convallyria. Music is All Rights Reserved by the author(s) and is subject to the terms made by the author(s).
