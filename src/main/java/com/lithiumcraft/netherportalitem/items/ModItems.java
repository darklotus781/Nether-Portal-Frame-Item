package com.lithiumcraft.netherportalitem.items;

import com.lithiumcraft.netherportalitem.NetherPortalItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NetherPortalItem.MOD_ID);

    public static final RegistryObject<Item> NETHER_PORTAL_FRAME = ITEMS.register("nether_portal_frame",
            () -> new NetherPortalFrameItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}