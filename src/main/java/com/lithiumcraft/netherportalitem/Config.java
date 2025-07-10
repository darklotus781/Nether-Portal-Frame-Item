package com.lithiumcraft.netherportalitem;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = NetherPortalItem.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue DISABLE_PORTAL_IGNITION = BUILDER
            .comment("Should normal Nether portal ignition be disabled?")
            .define("disableNormalPortalIgnition", true);

    private static final ModConfigSpec.ConfigValue<String> FAILED_IGNITION_MESSAGE = BUILDER
            .comment("Message to show when portal fails to ignite.")
            .define("failedIgnitionMessage", "The Portal doesn't seem to want to light...\nYou need the \"Nether Portal Frame\" item to spawn a functional Nether Portal!");

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean disablePortalIgnition;
    public static String failedIgnitionMessage;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        disablePortalIgnition = DISABLE_PORTAL_IGNITION.get();
        failedIgnitionMessage = FAILED_IGNITION_MESSAGE.get();
    }
}
