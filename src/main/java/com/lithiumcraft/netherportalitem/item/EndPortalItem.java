package com.lithiumcraft.netherportalitem.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class EndPortalItem extends Item {
    public EndPortalItem(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.netherportalitem.end_portal_item"));
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        if (level.isClientSide || player == null) return InteractionResult.SUCCESS;

        BlockPos clicked = ctx.getClickedPos();
        if (!level.getBlockState(clicked).canBeReplaced(new BlockPlaceContext(ctx))) {
            clicked = clicked.relative(ctx.getClickedFace());
        }

        ServerLevel server = (ServerLevel) level;
        BlockPos center = clicked;

        if (!placeRing(server, center)) return InteractionResult.FAIL;

        // Only consume item and give Eye if player is NOT in creative
        if (!player.getAbilities().instabuild) {
            ctx.getItemInHand().shrink(1);
            player.addItem(new ItemStack(Items.ENDER_EYE));
        }

        level.playSound(null, center, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        return InteractionResult.CONSUME;
    }

    /** Places 11 filled and 1 empty End Portal frame, randomizing which is empty. */
    private boolean placeRing(ServerLevel level, BlockPos c) {
        if (!areaReplaceable(level, c)) return false;

        // pick one of the 12 positions to be empty
        int emptyIndex = level.random.nextInt(12);
        int placed = 0;

        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                boolean framePos = (Math.abs(x) == 2 && Math.abs(z) <= 1)
                        || (Math.abs(z) == 2 && Math.abs(x) <= 1);
                if (framePos) {
                    boolean empty = placed == emptyIndex;
                    placed++;
                    Direction inward = inwardFacingFor(c.offset(x, 0, z), c);
                    BlockState s = Blocks.END_PORTAL_FRAME.defaultBlockState()
                            .setValue(BlockStateProperties.HORIZONTAL_FACING, inward)
                            .setValue(EndPortalFrameBlock.HAS_EYE, !empty);
                    level.setBlockAndUpdate(c.offset(x, 0, z), s);
                }
            }
        }

        return true;
    }

    /** Finds which direction this frame should face toward the portal center. */
    private Direction inwardFacingFor(BlockPos pos, BlockPos center) {
        int dx = pos.getX() - center.getX();
        int dz = pos.getZ() - center.getZ();
        if (Math.abs(dx) > Math.abs(dz))
            return dx > 0 ? Direction.WEST : Direction.EAST;
        return dz > 0 ? Direction.NORTH : Direction.SOUTH;
    }

    /** Checks that all 12 frame positions are placeable. */
    private boolean areaReplaceable(Level level, BlockPos c) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                boolean framePos = (Math.abs(x) == 2 && Math.abs(z) <= 1)
                        || (Math.abs(z) == 2 && Math.abs(x) <= 1);
                if (framePos && !level.getBlockState(c.offset(x, 0, z)).canBeReplaced()) {
                    return false;
                }
            }
        }
        return true;
    }
}
