# ![Shantek Banner](/.github/assets/Banner.png)

---
[![Modrinth](https://img.shields.io/badge/Modrinth-Download-green?logo=modrinth)](https://modrinth.com/plugin/durability-alert-continued)
[![Discord](https://img.shields.io/discord/628396916639793152.svg?color=%237289da&label=discord)](https://shantek.co/discord)
[![CodeFactor](https://www.codefactor.io/repository/github/shantek/durabilityalert-continued/badge)](https://www.codefactor.io/repository/github/shantek/durabilityalert-continued)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/shantek/DurabilityAlert-Continued)
![GitHub Created At](https://img.shields.io/github/created-at/shantek/DurabilityAlert-Continued)

# ![Durability Alert Banner](/.github/assets/DA_Banner.png)

# 🛡️ Durability Alert – Continued

**A lightweight alert system for item durability in Minecraft.**  
Receive notifications when your armor or tools are low on durability. Configurable on a per-player basis, with full control over thresholds and sound cues.

> 🔧 [Get the latest dev builds here →](https://www.github.com/shantek/DurabilityAlert-Continued/releases)

---

## 🔍 Features

- Alerts players when armor/tools drop below a set durability
- Works with percentage or raw durability mode
- Supports: Armor, Tools, Swords, Hoes, Fishing Rods, Shears
- Fully configurable thresholds per player
- Toggle sound alerts
- Supports per-player settings and toggles

---

## ⚙️ Commands

| Command | Description |
|---------|-------------|
| `/durabilityalert` or `/da` | Base command; opens info or help. |
| `/da toggle` | Enables/disables durability alerts for the player. |
| `/da armour <value>` | Set the alert threshold for armor (per player). |
| `/da tools <value>` | Set the alert threshold for tools (per player). |
| `/da type [percent/durability]` | Choose between % or raw durability display. |
| `/da enchant` | Toggle alerts only for enchanted gear. |
| `/da sound` | Toggle alert sounds on or off. |

---

## 🔐 Permissions

| Node | Description |
|------|-------------|
| `shantek.durabilityalert.use` | Allows a player to use and configure alerts. |
| `shantek.durabilityalert.reload` | Allows plugin config to be reloaded (default: OP only). |

---

## 🛠️ Setup Instructions

1. Drop the plugin JAR into your `/plugins` folder.
2. Restart or reload the server.
3. Players can immediately begin customizing their alerts using `/da`.

> Alerts are stored per-player and persist between sessions.

---

## 💬 External Links

- 💬 [Join the Discord](https://shantek.co/discord)
- 🛠️ [GitHub Repository](https://github.com/shantek/DurabilityAlert-Continued)
- 🐞 [Report Issues / Suggest Features](https://github.com/shantek/DurabilityAlert-Continued/issues)
- ❤️ [Support via Patreon](https://shantek.co/patreon)

---

## 📄 License

[![License: GPL](https://img.shields.io/badge/license-GPL-blue.svg)](LICENSE)

This is a continued and improved version of the original  
[Durability Alert plugin by christopherwalkerml](https://github.com/christopherwalkerml/DurabilityAlert),  
developed and maintained with permission from the original author.


Distributed under the **GNU General Public License v3.0**.  
See [`LICENSE`](LICENSE) for full terms.

--- 

![Plugin Stats](https://bstats.org/signatures/bukkit/Durability%20Alert%20Continued.svg)