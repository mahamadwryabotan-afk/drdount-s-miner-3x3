package com.miner3x3;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

public class MiningHandler {

    private static final String PROCESSING_TAG = "miner3x3_processing";

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!ModConfig.isMiner3x3Enabled()) return;

        Player player = event.getPlayer();
        if (player == null || player.level.isClientSide) return;
        if (!(player.level instanceof ServerLevel serverLevel)) return;

        // Avoid recursion when we break the surrounding blocks
        if (player.getPersistentData().getBoolean(PROCESSING_TAG)) return;

        BlockPos origin = event.getPos();
        BlockState originState = event.getState();
        ItemStack tool = player.getMainHandItem();

        // Check if the center block is breakable / mineable with the current tool
        if (!canBreakBlock(originState, serverLevel, origin, tool)) return;

        // Compute the 3x3 area offsets based on player's mining direction
        Set<BlockPos> positions = get3x3Positions(serverLevel, player, origin);

        // Mark processing to prevent recursion
        player.getPersistentData().putBoolean(PROCESSING_TAG, true);

        try {
            boolean dropItems = !player.isCreative();
            for (BlockPos pos : positions) {
                breakBlock(serverLevel, pos, tool, player, dropItems);
            }
        } finally {
            player.getPersistentData().putBoolean(PROCESSING_TAG, false);
        }
    }

    /**
     * Computes the 3x3 area centered on {@code origin}, oriented based on the
     * direction the player is mining toward.
     * <p>
     * - If mining a wall (looking mostly horizontal), the 3x3 lies on the
     *   vertical plane: extends left/right of the player's facing and up/down.
     * - If mining a floor or ceiling (looking mostly up/down), the 3x3 lies on
     *   the horizontal plane: extends in X and Z.
     * <p>
     * The origin/center block is excluded because it is already being broken by
     * the event that triggered this handler.
     */
    private Set<BlockPos> get3x3Positions(ServerLevel level, Player player, BlockPos origin) {
        Set<BlockPos> positions = new HashSet<>();

        double pitch = player.getXRot();

        if (Math.abs(pitch) > 45.0D) {
            // Mining a floor or ceiling -> 3x3 on the X-Z plane
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    positions.add(origin.offset(dx, 0, dz));
                }
            }
        } else {
            // Mining a wall -> 3x3 on the vertical plane perpendicular to facing
            Direction facing = player.getDirection();
            Direction right = getRight(facing);

            for (int side = -1; side <= 1; side++) {
                int dx = right.getStepX() * side;
                int dz = right.getStepZ() * side;
                for (int dy = -1; dy <= 1; dy++) {
                    positions.add(origin.offset(dx, dy, dz));
                }
            }
        }

        // The center block is already being broken by the triggering event
        positions.remove(origin);

        return positions;
    }

    private Direction getRight(Direction facing) {
        // Clockwise rotation of the facing direction gives the "right" axis
        // Facing N/S -> right is E/W. Facing E/W -> right is N/S.
        if (facing == Direction.NORTH) return Direction.EAST;
        if (facing == Direction.SOUTH) return Direction.WEST;
        if (facing == Direction.EAST) return Direction.SOUTH;
        if (facing == Direction.WEST) return Direction.NORTH;
        return Direction.EAST;
    }

    private void breakBlock(ServerLevel level, BlockPos pos, ItemStack tool, Player player, boolean dropItems) {
        BlockState state = level.getBlockState(pos);

        // Respect normal tool requirements and block hardness
        if (!canBreakBlock(state, level, pos, tool)) return;

        // Break the block, giving normal drops (unless creative)
        level.destroyBlock(pos, dropItems, player);
    }

    /**
     * Checks whether this block can be broken as part of the 3x3 area:
     * - The block is not air
     * - The block has finite hardness (destroy speed >= 0)
     * - If the block requires a correct tool, the player's tool must be a valid
     *   tool type and capable of harvesting the block
     */
    private boolean canBreakBlock(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool) {
        if (state.isAir()) return false;

        // Skip unbreakable blocks (e.g. bedrock, barrier)
        if (state.getDestroySpeed(level, pos) < 0.0F) return false;

        // If the block requires a correct tool for drops, the tool must be valid and correct
        if (state.requiresCorrectToolForDrops()) {
            if (tool.isEmpty()) return false;
            Item item = tool.getItem();

            boolean isToolType = item instanceof PickaxeItem ||
                    item instanceof AxeItem ||
                    item instanceof ShovelItem ||
                    item instanceof HoeItem ||
                    item instanceof SwordItem ||
                    item instanceof ShearsItem;

            if (!isToolType) return false;

            return tool.isCorrectToolForDrops(state);
        }

        // No correct tool required - can be broken with anything
        return true;
    }
}