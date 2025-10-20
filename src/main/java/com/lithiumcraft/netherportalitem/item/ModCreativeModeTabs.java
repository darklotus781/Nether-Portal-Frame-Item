package com.lithiumcraft.netherportalitem.item;

import com.lithiumcraft.netherportalitem.NetherPortalItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NetherPortalItem.MOD_ID);

    public static final Supplier<CreativeModeTab> ITEMS_TAB = CREATIVE_MODE_TABS.register("netherportalitem_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.NETHER_PORTAL_ITEM.get()))
                    .title(Component.translatable("creativetab.netherportalitem_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.END_PORTAL_ITEM.get());
                        output.accept(ModItems.NETHER_PORTAL_ITEM.get());
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}