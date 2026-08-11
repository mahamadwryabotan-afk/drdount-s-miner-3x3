package com.miner3x3;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FastMineHandler {

    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!ModConfig.isFastMineEnabled()) return;

        Player player = event.getEntity();
        if (player == null) return;
        if (player.level.isClientSide) return;

        // Apply the speed multiplier
        float speed = event.getOriginalSpeed();
        float boosted = speed * (float) ModConfig.getFastMineSpeed();
        event.setNewSpeed(boosted);
    }
}