# Nether Portal Item Frame

[![Modrinth](https://badges.moddingx.org/modrinth/downloads/tG0r5SA7)](https://modrinth.com/mod/portal-frame-item)
[![CurseForge](https://badges.moddingx.org/curseforge/downloads/953246)](https://www.curseforge.com/minecraft/mc-mods/nether-portal-frame-item)

**Nether Portal Item Frame** gives you full control over portal creation by turning Nether and End portals into placeable items.

---

## Features

* **Portal Items** — Instantly place pre-lit Nether or End portals by right-clicking the ground.
* **Modpack Integration** — Disable standard Nether portal ignition and use portal items as quest rewards or gated crafting recipes.
* **Configurable Behavior** — Fine-tune portal cancellation and custom messages without touching scripts.
* **Smart Placement Preview** — When holding a portal item, a full preview of the portal’s placement area appears before you build.
* **End Portal Logic** — Placing an End Portal consumes the item and gives you an Eye of Ender to light the remaining frame.

---

## Installation

1. Install **NeoForge** for Minecraft **1.21.1**.
2. Drop the mod JAR into your `mods/` folder.
3. Launch the game once to generate the configuration file.

---

## Configuration

After launching once, a config file will be created at:

```
config/netherportalitem-common.toml
```

Example options:

```toml
# If true, disables natural Nether Portal creation by players.
disablePortalIgnition = true

# Message displayed when a player attempts to light a portal normally.
portalFailMessage = "The portal doesn't seem to want to light..."
```

> 💡 **Tip:** Server owners can include a tuned config in `defaultconfigs/netherportalitemframe.toml` for modpack-wide defaults.

---

## Example KubeJS Integration (1.20.1 only)

If you prefer event scripting, here’s how you might disable normal portal creation using KubeJS:

```js
ForgeEvents.onEvent("net.minecraftforge.event.level.BlockEvent$PortalSpawnEvent", e => {
    e.setCanceled(true)
    let player = e.level.getNearestPlayer(e.pos.x, e.pos.y, e.pos.z, 10, null)
    if (player) {
        player.statusMessage = Text.of("The portal doesn't seem to want to light...")
    }
})
```

However, **Nether Portal Item Frame** already handles this automatically via config — no script required.

---

## Gameplay Notes

* Right-click with a **Nether Portal Frame Item** to instantly place a pre-lit Nether portal.
* Right-click with an **End Portal Frame Item** to build the frame and receive an **Eye of Ender** to light it manually.
* Works perfectly with quest rewards, gated progression, or scripted crafting recipes.

---

## Compatibility

* **Minecraft:** 1.21.1
* **Loader:** NeoForge
* **Worlds:** Safe for existing saves.
* **Multiplayer:** Server-compatible and sync-safe.

---

## Showcase

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/Ds_JLbj4mjQ" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>

---

## Reporting Issues

Please include:

* Game + loader versions (MC 1.21.1, NeoForge build)
* Mod version (e.g., `1.0.x`)
* Full `latest.log` (use Pastebin or Gist)
* Steps to reproduce
* Modpack name and additional mods if relevant

Open an issue here: **[GitHub Issues](https://github.com/darklotus781/Nether-Portal-Frame-Item/issues)**

---

## License

This mod is licensed under the **MIT License**.

---

## Credits

* **DarkLotus / LithiumCraft** — concept, code, & maintenance
* Community testers & pack authors providing feedback and ideas

---

## Links

* **Modrinth:** [https://modrinth.com/mod/portal-frame-item](https://modrinth.com/mod/portal-frame-item)
* **CurseForge:** [https://www.curseforge.com/minecraft/mc-mods/nether-portal-frame-item](https://www.curseforge.com/minecraft/mc-mods/nether-portal-frame-item)
* **Discord:** [Join the community](https://discord.gg/XH7zCjgUHb)

---

If this mod made your pack better, consider starring the GitHub repo or sharing your builds on Discord!
