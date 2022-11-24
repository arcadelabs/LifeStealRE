<img src="https://github.com/arcadelabs/LifeSteal/blob/master/branding/LifeStealLogo.png" align="left" height="240px" alt="ArcadeLabs">
<!-- <h1 align="right"><strong>Lifesteal Reimagined</strong><br>Not your regular LifeSteal core.</h1> -->
<div align="center">

# LifeSteal Reimagined

### Not your regular LifeSteal core.

[![Status](https://img.shields.io/badge/STATUS-BETA-3a0ca3?style=for-the-badge)](https://github.com/arcadelabs/LifeStealRE/tags)
[![GitHub release](https://img.shields.io/github/v/release/arcadelabs/LifeStealRE?include_prereleases&style=for-the-badge)](https://github.com/arcadelabs/LifeStealRE/releases/latest) <br>
[![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/arcadelabs/LifeStealRE?style=for-the-badge)](https://www.codefactor.io/repository/github/arcadelabs/lifestealre)
[![GitHub downloads](https://img.shields.io/github/downloads/arcadelabs/LifeStealRE/total?style=for-the-badge)](https://github.com/arcadelabs/LifeStealRE/releases/latest)
[![GitHub issues](https://img.shields.io/github/issues/arcadelabs/LifeStealRE?style=for-the-badge)](https://github.com/arcadelabs/LifeStealRE/issues) <br>
[![Modrinth](https://cdn.jsdelivr.net/gh/intergrav/devins-badges/assets/compact/available/modrinth_vector.svg)](https://modrinth.com/plugin/lifestealre)
[![Discord Plural](https://cdn.jsdelivr.net/gh/intergrav/devins-badges/assets/compact/social/discord-plural_vector.svg)](https://dsc.gg/arcadelabs)
[![Twitter Plural](https://cdn.jsdelivr.net/gh/intergrav/devins-badges/assets/compact/social/twitter-plural_vector.svg)](https://twitter.com/AskArcadeLabs)

</div>
<br>

<h2 align="center">About LifeSteal</h2>
Lifesteal is a survival gamemode concept made by some MCYT content creators, when you kill someone, you get to keep
their 1x max health attribute or as we like to call it, a life, and vice versa. Players can also craft consumable heart
items with a custom recipe or withdraw it. In addition to the traditional Lifesteal concept, our plugin adds tons of
other features, such as 3 different types of hearts, blessed, normal and cursed. It also adds the option to drop random
or fixed types of hearts on withdraw, death and craft. You can create unlimited amounts of hearts in all 3 types, each
of them is 100% customizable, and each heart can give unique effects as well.

Original LifeSteal SMP : https://twitter.com/TheLifeStealSMP

## Features,

![Progress](https://progress-bar.dev/85/?title=done&width=220&color=f72585&suffix=%%20almost%20there...)

- [x] HEX support! gradients etc, it all works! use [Web UI](https://webui.adventure.kyori.net) to edit the messages,
  and yes, this is the most important feature, I don't care your players spent diamonds and wither stars to craft an
  heart that doesn't work as long as it shows the errors in gradients.
- [x] Customizable heart recipe.
- [x] Blessed, normal and cursed heart types.
- [x] Unlimited amount of 100% customizable hearts.
- [x] Rarity based heart drops.
- [x] Heart item can be edible (consumable), a player head (on right click) or any item from supported server version (
  on right click).
- [x] Heart item can have unlimited amount of potion effects which will be given when player consumes a heart.
- [x] Totems can be disabled.
- [x] Milk cures negetive effects can be disabled.
- [x] Hearts can have custom texture model ID which resource packs will use to replace the textures.
- [x] 100% Customizable messages and sounds.
- [x] Very configurable per world feature toggle.
- [x] MySQL and SQLite database support.
- [x] Player elimination, ban/spectator/spectate
  body. ![GitHub labels](https://img.shields.io/github/labels/arcadelabs/LifeSteal/WIP)
- [ ] Heart consume animations and particle effects (work in progress, don't expect proper
  functionality.) ![GitHub labels](https://img.shields.io/github/labels/arcadelabs/LifeSteal/WIP)
- [ ] Profile system (statistics etc.) ![GitHub labels](https://img.shields.io/github/labels/arcadelabs/LifeSteal/WIP)
- [ ] And tons of other stuff that I'm gonna send screenshot of because me lazy...
  I blured some stuff out because they're upcoming more exciting features 😈

> ![image](https://user-images.githubusercontent.com/69498033/177811484-f9ef5fbc-3881-4d1e-b988-dd414502fb0d.png)

## Installation.

1. Download and install [LabAide](https://github.com/arcadelabs/LabAide/releases) library plugin build.
2. Download and install [latest](https://github.com/arcadelabs/LifeSteal/releases/tag/latest) LifeStealRE plugin build.
3. Expect it to work, if it doesn't I hearby authorize you to yell at me.

## For developers,

[![LabAide version](https://repo.zorino.in/api/badge/latest/releases/in/arcadelabs/lifesteal/lifesteal-api?color=40c14a&name=LifeStealRE%20version)](https://github.com/arcadelabs/LifeStealRE/releases/latest)
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/arcadelabs/LifeStealRE/Publish%20LifeStealRE%20to%20Maven%20repository/master?color=45b94e)](https://repo.zorino.in/#/releases/in/arcadelabs/lifesteal/lifesteal-api)


### Maven

> ```xml
> <repository>
>     <id>arcadelabs-mvn-releases</id>
>     <name>ArcadeLabs Repository</name>
>     <url>https://repo.zorino.in/releases</url>
> </repository>
> ```

> ```xml
> <dependency>
>     <groupId>in.arcadelabs.lifesteal</groupId>
>     <artifactId>lifesteal-api</artifactId>
>     <version>[VERSION]</version>
>     <scope>provided</scope>
> </dependency>
> ```

### Gradle

> ```groovy
> maven {
>     url 'https://repo.zorino.in/releases'
> }
> ```

> ```groovy
> dependencies {
>     compileOnly 'in.arcadelabs.lifesteal:lifesteal-api:[VERSION]'
> }
> ```

> Get instance of LifeStealAPI
> ```java
> LifeStealAPI api = Bukkit.getServicesManager().getRegistration(LifeStealAPI .class).getProvider();
> ```

> **Note**
> Javadocs & examples coming soon...

## Servers using LifeStealRE ,

![bStats Players](https://img.shields.io/bstats/players/15272?style=for-the-badge)
![bStats Servers](https://img.shields.io/bstats/servers/15272?style=for-the-badge)
<br>
> **Note**
> _Contact me to include your server name and IP here._

## Final thoughts,

This is my ([@RealRONiN](https://github.com/RealRONiN)'s) 2nd public project (first one is Greetings and it's private
for god knows what reasons.),
and it's still in development, I'd say it's about 40% done for now, please report any bugs or issues you face in issues
section.
For developers who're reviewing the code right now (on my humble request I'm guessing), please don't yell at me for
forgetting to add null checks and other important stuff in almost everywhere, also please don't judge my obsession with
gradients in configs. 
