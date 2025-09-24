package com.lithiumcraft.netherportalitem.items;

import com.lithiumcraft.netherportalitem.NetherPortalItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NetherPortalItem.MOD_ID);

    public static final DeferredItem<Item> NETHER_PORTAL_FRAME = ITEMS.register("nether_portal_frame",
            () -> new NetherPortalFrameItem(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}