# Nether Portal Frame Item
This is a Minecraft Mod for Forge 1.20.1
This mod adds the Nether Portal as an Item.  This item can be placed in the world to instantly place a standard size Nether Portal.
The portal is pre-lit and ready to go.

### Why?
This mod was built to address the issue present in some modpacks where you want to cancel the typical Portal creation and only allow players to travel to the Nether via other means.
This allows for the built-in Minecraft portal linking and creation but gates it behind progression.  Now in your quests you can reward the player with the portal frame item.

### Example Portal Cancel Event:
File: kubejs/startup_scripts/netherPortalCancel.js
```javascript
ForgeEvents.onEvent("net.minecraftforge.event.level.BlockEvent$PortalSpawnEvent", e => {
    let {level, pos} = e
    e.setCanceled(true)
    let server = level.server
    let player = level.getNearestPlayer(pos.x, pos.y, pos.z, 10, null)
    if (!player) return
    player.statusMessage = Text.of("The portal doesn't seem to want to light...")
    server.schedule(2*1000, ()=> player.statusMessage = Text.of("You need the \"Nether Portal Frame\" Item to spawn a Nether Portal!"))
})
```

Need Help?
======
When reporting an issue put the version number before the issue title! Such as [FULL][0.0.1] My game is broken! Also include any added mods you may have put in, into the description of the issue.

|                                                         You can also find us on Discord for help<br>or just to chat!                                                          |
|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| <a href="https://discord.gg/XH7zCjgUHb"><img src="https://discordapp.com/assets/fc0b01fe10a0b8c602fb0106d8189d9b.png" alt="Join us on Discord!"  width="200" height="68"></a> |
