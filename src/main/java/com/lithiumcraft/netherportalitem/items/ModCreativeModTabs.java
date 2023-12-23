package com.lithiumcraft.netherportalitem.items;

import com.lithiumcraft.netherportalitem.NetherPortalItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NetherPortalItem.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("netherportalitem_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.NETHER_PORTAL_FRAME.get()))
                    .title(Component.translatable("creativetab.netherportalitem_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.NETHER_PORTAL_FRAME.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}