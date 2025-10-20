package com.lithiumcraft.netherportalitem.client.render;

import com.lithiumcraft.netherportalitem.item.NetherPortalItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
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
public class NetherPortalPreviewRenderer {

    @SubscribeEvent
    public static void renderPreview(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        if (!(mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof NetherPortalItem)) return;

        HitResult hit = mc.hitResult;
        if (!(hit instanceof BlockHitResult bhr) || hit.getType() != HitResult.Type.BLOCK) return;

        Level level = mc.level;
        UseOnContext fakeContext = new UseOnContext(mc.player, InteractionHand.MAIN_HAND, bhr);

        // 🔹 Get the full 4×5 placement area (20 total positions)
        List<BlockPos> fullArea = NetherPortalItem.getFrameArea(level, fakeContext);

        // Split into frame + interior lists like createPortalFrame()
        List<BlockPos> frameBlocks = new ArrayList<>();
        List<BlockPos> portalBlocks = new ArrayList<>();

        BlockPos base = fakeContext.getClickedPos();
        if (!level.getBlockState(base).canBeReplaced(new net.minecraft.world.item.context.BlockPlaceContext(fakeContext))) {
            base = base.relative(fakeContext.getClickedFace());
        }
        Direction.Axis axis = fakeContext.getHorizontalDirection().getAxis();

        for (int h = -1; h < 3; h++) {
            for (int v = base.getY(); v < base.getY() + 5; v++) {
                boolean isFrameEdge = (h == -1 || h == 2) || (v == base.getY() || v == base.getY() + 4);
                BlockPos pos = (axis == Direction.Axis.X)
                        ? new BlockPos(base.getX(), v, base.getZ() + h)
                        : new BlockPos(base.getX() + h, v, base.getZ());

                if (isFrameEdge) {
                    frameBlocks.add(pos);
                } else {
                    portalBlocks.add(pos);
                }
            }
        }

        // Renderer setup (identical to EndPortalPreviewRenderer)
        PoseStack poseStack = event.getPoseStack();
        var cam = mc.gameRenderer.getMainCamera();
        poseStack.pushPose();
        poseStack.translate(-cam.getPosition().x, -cam.getPosition().y, -cam.getPosition().z);

        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1F, 1F, 1F, 0.65F); // more opaque for obsidian

        RenderType translucent = RenderType.translucent();

        // --- Render obsidian frame ---
        for (BlockPos pos : frameBlocks) {
            drawGhostBlock(poseStack, buffer, blockRenderer, Blocks.OBSIDIAN.defaultBlockState(), pos);
        }

        // --- Render purple Nether Portal interior ---
        Direction.Axis portalAxis = (axis == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
        for (BlockPos pos : portalBlocks) {
            BlockState portalState = Blocks.NETHER_PORTAL.defaultBlockState()
                    .setValue(BlockStateProperties.HORIZONTAL_AXIS, portalAxis);
            drawGhostBlock(poseStack, buffer, blockRenderer, portalState, pos);
        }

        buffer.endBatch(translucent);
        poseStack.popPose();
    }

    /** Shared ghost-block render identical to EndPortalPreviewRenderer, with 0.65F alpha. */
    private static void drawGhostBlock(PoseStack poseStack,
                                       MultiBufferSource.BufferSource buffer,
                                       BlockRenderDispatcher blockRenderer,
                                       BlockState state,
                                       BlockPos pos) {
        poseStack.pushPose();
        poseStack.translate(pos.getX(), pos.getY(), pos.getZ());

        var model = blockRenderer.getBlockModel(state);
        var vertexConsumer = buffer.getBuffer(RenderType.translucent());

        int light = 0x808080;
        float alpha = 0.65F; // more solid look
        float r = 1.0F, g = 1.0F, b = 1.0F;

        blockRenderer.getModelRenderer().renderModel(
                poseStack.last(),
                vertexConsumer,
                state,
                model,
                r * alpha, g * alpha, b * alpha,
                light,
                0
        );

        poseStack.popPose();
    }
}
