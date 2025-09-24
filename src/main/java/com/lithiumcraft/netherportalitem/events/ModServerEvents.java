package com.lithiumcraft.netherportalitem.events;

import com.lithiumcraft.netherportalitem.Config;
import com.lithiumcraft.netherportalitem.NetherPortalItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = NetherPortalItem.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModServerEvents {

    @SubscribeEvent
    public static void onPortalSpawn(BlockEvent.PortalSpawnEvent event) {
        if (!Config.disablePortalIgnition) return;

        LevelAccessor level = event.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) return;

        event.setCanceled(true);

        BlockPos pos = event.getPos();
        Player nearest = serverLevel.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10, false);

        if (nearest instanceof ServerPlayer player) {
            String[] messages = Config.failedIgnitionMessage.split("§n");
            player.displayClientMessage(Component.literal(messages[0]), false);

            serverLevel.getServer().tell(new TickTask(
                    serverLevel.getServer().getTickCount() + 40,
                    () -> {
                        if (messages.length > 1) {
                            player.displayClientMessage(Component.literal(messages[1]), false);
                        }
                    }
            ));
        }

    }
}
