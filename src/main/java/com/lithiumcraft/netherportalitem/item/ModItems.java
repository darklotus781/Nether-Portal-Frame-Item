package com.lithiumcraft.netherportalitem.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(com.lithiumcraft.netherportalitem.NetherPortalItem.MOD_ID);

    public static final DeferredItem<Item> NETHER_PORTAL_ITEM = ITEMS.register("nether_portal_item",
            () -> new NetherPortalItem(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    public static final DeferredItem<EndPortalItem> END_PORTAL_ITEM = ITEMS.register("end_portal_item",
            () -> new EndPortalItem(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}