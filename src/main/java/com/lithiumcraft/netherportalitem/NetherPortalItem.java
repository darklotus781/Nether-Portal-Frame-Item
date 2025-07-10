package com.lithiumcraft.netherportalitem;

import com.lithiumcraft.netherportalitem.items.ModCreativeModeTabs;
import com.lithiumcraft.netherportalitem.items.ModItems;
import com.mojang.logging.LogUtils;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NetherPortalItem.MOD_ID)
public class NetherPortalItem {

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "netherportalitem";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "netherportalitem" namespace
    public NetherPortalItem(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
