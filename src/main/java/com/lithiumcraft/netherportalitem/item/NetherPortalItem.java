package com.lithiumcraft.netherportalitem.item;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;

import java.util.ArrayList;
import java.util.List;

public class NetherPortalItem extends Item {
    public NetherPortalItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.netherportalitem.nether_portal_item"));
    }

    /**
     * Creates a Nether Portal with {@link NetherPortalItem#createPortalFrame(UseOnContext)} and swing and consumes the item if the portal is successfully placed.
     * @param context The {@link UseOnContext} of the usage interaction.
     * @return A "consume" {@link InteractionResult} if successful, fail if otherwise.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            if (this.createPortalFrame(context)) {
                player.swing(context.getHand());
                if (!player.getAbilities().instabuild) {
                    context.getItemInHand().shrink(1);
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.FAIL;
    }

    public static List<BlockPos> getFrameArea(Level level, UseOnContext context) {
        List<BlockPos> area = new ArrayList<>();

        BlockPos pos = context.getClickedPos();
        if (!level.getBlockState(pos).canBeReplaced(new BlockPlaceContext(context))) {
            pos = pos.relative(context.getClickedFace());
        }

        Direction.Axis axis = context.getHorizontalDirection().getAxis();

        // 4 wide × 5 tall rectangle
        for (int h = -1; h < 3; h++) {
            for (int v = pos.getY(); v < pos.getY() + 5; v++) {
                BlockPos truePos = (axis == Direction.Axis.X)
                        ? new BlockPos(pos.getX(), v, pos.getZ() + h)
                        : new BlockPos(pos.getX() + h, v, pos.getZ());
                area.add(truePos);
            }
        }
        return area;
    }

    /** Returns only the 6 inner Nether Portal blocks. */
    public static List<BlockPos> getInteriorArea(Level level, UseOnContext context) {
        List<BlockPos> interior = new ArrayList<>();

        BlockPos pos = context.getClickedPos();
        if (!level.getBlockState(pos).canBeReplaced(new BlockPlaceContext(context))) {
            pos = pos.relative(context.getClickedFace());
        }

        Direction.Axis axis = context.getHorizontalDirection().getAxis();

        for (int h = 0; h < 2; h++) {
            for (int v = pos.getY() + 1; v < pos.getY() + 4; v++) {
                BlockPos truePos = (axis == Direction.Axis.X)
                        ? new BlockPos(pos.getX(), v, pos.getZ() + h)
                        : new BlockPos(pos.getX() + h, v, pos.getZ());
                interior.add(truePos);
            }
        }
        return interior;
    }

    /**
     * Creates a Nether Portal.
     * @param context The {@link UseOnContext} of the usage interaction.
     * @return Whether the portal frame can be placed, as a {@link Boolean}.
     */
    private boolean createPortalFrame(UseOnContext context) {
        Level level = context.getLevel();
        List<BlockPos> framePositions = getFrameArea(level, context);
        List<BlockPos> interiorPositions = getInteriorArea(level, context);

        // verify space
        for (BlockPos pos : framePositions) {
            if (!level.getBlockState(pos).canBeReplaced(new BlockPlaceContext(context))) {
                return false;
            }
        }

        // place obsidian frame
        for (BlockPos pos : framePositions) {
            level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
        }

        // place portal blocks
        Direction.Axis axis = context.getHorizontalDirection().getAxis();
        Direction.Axis trueAxis = (axis == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;

        for (BlockPos pos : interiorPositions) {
            level.setBlock(pos,
                    Blocks.NETHER_PORTAL.defaultBlockState().setValue(NetherPortalBlock.AXIS, trueAxis),
                    2 | 16);
        }
        return true;
    }
}