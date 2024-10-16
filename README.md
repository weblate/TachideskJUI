
![image](resources/icon.png)
# Suwayomi-JUI
A free and open source manga reader to read manga from a [Suwayomi-Server][tachidesk-server] instance.

Suwayomi-JUI can run Suwayomi-Server on its own on desktop platforms, or connect to an already hosted server. 

Android and any desktop platform that runs Java can run it. On most platforms are binaries available if you don't want to install Java yourself.

## Is this application usable? Should I test it?
Here is a list of current features for interaction with Suwayomi-JUI:

- Managing installed Extensions.
- Interaction with your library.
- Browsing installed sources.
- Viewing manga and chapters.
- Reading, downloading, and managing chapters.
- Viewing chapter updates
- Globally search sources

**Note:** Keep in mind that Suwayomi-JUI is alpha software, so it can have issues. See [General troubleshooting](#general-troubleshooting) and [Support and help](#support-and-help) if it happens.

### Supported Suwayomi-Server versions
These are the versions of [Suwayomi-Server][tachidesk-server] that JUI supports.
#### [Release build][release]
- [Suwayomi-Server][tachidesk-server] v1.0.0+
#### [Preview build][preview]
- [Suwayomi-Server Preview][tachidesk-server-preview] v1.0.0-r1498+

## Downloading and Running the app
### All Desktop Operating Systems (x64, Java Not Included)
You should have [Java(JRE or JDK) 17](https://adoptium.net/) or newer.

Download the latest jar release for your OS from [the releases section][release] (Or from [the preview releases][preview]).

Double-click on the jar file or run `java -jar Suwayomi-JUI-os-arch-X.Y.Z.jar` from a Terminal/Command Prompt window to run the app.

### Windows (x64, Java 8+ required for server)
#### Installer
Download the latest msi release from [the releases section][release] (Or from [the preview releases][preview]).
#### [Chocolatey](https://community.chocolatey.org/packages/tachidesk-jui)
`choco install tachidesk-jui`
#### Winget
`winget install tachidesk-jui`
#### Scoop
```shell
scoop bucket add extras
scoop install tachidesk-jui
```

### MacOS (x64, Java 8+ required for server)
Download the latest dmg release from [the releases section][release] (Or from [the preview releases][preview]).

### Debian based Linux (x64, Java 8+ required for server)
Download the latest deb release from [the releases section][release] (Or from [the preview releases][preview]).

### Fedora based Linux (x64, Java 8+ required for server)
Download the latest rpm release from [the releases section][release] (Or from [the preview releases][preview]).

### Arch based Linux (x64, Java Included)
Download the latest release from [the aur](https://aur.archlinux.org/packages/tachidesk-jui/).

If you use yay, you can run `yay -S tachidesk-jui` inside a terminal window.

### Android (Requires external server)
Download the latest apk from [the releases section][release] (Or from [the preview releases][preview]).

## General troubleshooting
### I'm having issues starting the application
Make sure you have used either an installer, or you have Java 17 installed.

### It says server failed to start
Make sure that if you used an installer, that you have at least Java 8 installed.

## Support and help
Join Suwayomi's [discord server](https://discord.gg/DDZdqZWaHA) to hang out with the community and receive support and help.

## Building from source
### Prerequisite: Software dependencies
You need this software packages installed in order to build this project:
- Java Development Kit and Java Runtime Environment version 17, this can be handled by IntelliJ
### Building a jar for your OS
Run `./gradlew packageUberJarForCurrentOS`, the resulting built jar file will be `build/compose/Suwayomi-JUI-os-arch-X.Y.Z.jar`.

### Running without package
Run `./gradlew run`, JUI will build and run. Use this for debugging and development purposes.

## Translation
Feel free to translate the project on [Weblate](https://hosted.weblate.org/projects/suwayomi-jui/app/)

<details><summary>Translation Progress</summary>
<a href="https://hosted.weblate.org/engage/suwayomi-jui/">
<img src="https://hosted.weblate.org/widgets/suwayomi-jui/-/desktop/multi-auto.svg" alt="Translation status" />
</a>
</details>

## Credit
The `Suwayomi-Server` project is developed by [@AriaMoradi](https://github.com/AriaMoradi) and contributors, a link for [Suwayomi-Server is provided here][tachidesk-server] and is licensed under `Mozilla Public License v2.0`.

Parts of [Tachiyomi-1.x](https://github.com/tachiyomiorg/tachiyomi-1.x) is adopted into this codebase, also licensed under `Mozilla Public License v2.0`.

You can obtain a copy of `Mozilla Public License v2.0` from https://mozilla.org/MPL/2.0/

## License

    Copyright (C) 2020-2024 Syer and contributors

    This Source Code Form is subject to the terms of the Mozilla Public
    License, v. 2.0. If a copy of the MPL was not distributed with this
    file, You can obtain one at http://mozilla.org/MPL/2.0/.


[release]: https://github.com/Suwayomi/Suwayomi-JUI/releases
[preview]: https://github.com/Suwayomi/Suwayomi-JUI-preview/releases
[tachidesk-server]: https://github.com/Suwayomi/Suwayomi-Server
[tachidesk-server-preview]: https://github.com/Suwayomi/Suwayomi-Server-preview/releases
