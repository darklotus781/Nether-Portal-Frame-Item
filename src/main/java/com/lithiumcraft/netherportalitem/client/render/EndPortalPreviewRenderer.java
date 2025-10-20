package com.lithiumcraft.netherportalitem.client.render;

import com.lithiumcraft.netherportalitem.item.EndPortalItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class EndPortalPreviewRenderer {

    @SubscribeEvent
    public static void renderPreview(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        if (!(mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof EndPortalItem)) return;

        HitResult hit = mc.hitResult;
        if (!(hit instanceof BlockHitResult bhr) || hit.getType() != HitResult.Type.BLOCK) return;

        Level level = mc.level;
        BlockPos base = bhr.getBlockPos().relative(bhr.getDirection());
        List<BlockPos> frames = calculateRingPositions(base);

        // Renderer setup
        PoseStack poseStack = event.getPoseStack();
        var cam = mc.gameRenderer.getMainCamera();
        poseStack.pushPose();
        poseStack.translate(-cam.getPosition().x, -cam.getPosition().y, -cam.getPosition().z);

        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();

        // We'll draw the frames in the translucent layer with high opacity
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1F, 1F, 1F, 0.50F); // 75% opacity

        RenderType translucent = RenderType.translucent();

        for (BlockPos pos : frames) {
            Direction inward = inwardFacingFor(pos, base);
            BlockState previewState = Blocks.END_PORTAL_FRAME.defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, inward)
                    .setValue(EndPortalFrameBlock.HAS_EYE, true);

            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());

            var model = blockRenderer.getBlockModel(previewState);
            var vertexConsumer = buffer.getBuffer(RenderType.translucent());

            // lower light for ghosty appearance
            int light = 0x808080;

            // semi-transparent white tint (50%)
            float alpha = 0.5F;
            float r = 1.0F, g = 1.0F, b = 1.0F;

            // Draw model manually with alpha-tinted vertices
            blockRenderer.getModelRenderer().renderModel(
                    poseStack.last(),
                    vertexConsumer,
                    previewState,
                    model,
                    r * alpha, g * alpha, b * alpha,  // apply alpha via tint
                    light,
                    0
            );

            poseStack.popPose();
        }

        buffer.endBatch(translucent);
    }

    private static void drawOverlayBox(PoseStack poseStack, MultiBufferSource buffer, BlockPos pos,
                                       float r, float g, float b, float a) {
        // simple colored outline overlay (optional visual hint)
        net.minecraft.client.renderer.LevelRenderer.renderLineBox(
                poseStack,
                buffer.getBuffer(RenderType.lines()),
                pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1,
                r, g, b, a
        );
    }

    /** Same ring math as your placement logic. */
    private static List<BlockPos> calculateRingPositions(BlockPos center) {
        List<BlockPos> list = new ArrayList<>();
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                boolean framePos = (Math.abs(x) == 2 && Math.abs(z) <= 1)
                        || (Math.abs(z) == 2 && Math.abs(x) <= 1);
                if (framePos) list.add(center.offset(x, 0, z));
            }
        }
        return list;
    }

    /** Determines how a frame should face inward toward the portal center. */
    private static Direction inwardFacingFor(BlockPos pos, BlockPos center) {
        int dx = pos.getX() - center.getX();
        int dz = pos.getZ() - center.getZ();
        if (Math.abs(dx) > Math.abs(dz))
            return dx > 0 ? Direction.WEST : Direction.EAST;
        return dz > 0 ? Direction.NORTH : Direction.SOUTH;
    }
}
