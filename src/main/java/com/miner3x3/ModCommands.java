package com.miner3x3;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModCommands {

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("mine~2")
                .executes(this::toggleMiner3x3));
    }

    private int toggleMiner3x3(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (source.getEntity() instanceof ServerPlayer player) {
            boolean newState = !ModConfig.isMiner3x3Enabled();
            ModConfig.setMiner3x3Enabled(newState);

            if (newState) {
                player.sendSystemMessage(Component.literal("[Miner 3x3] ")
                        .withStyle(ChatFormatting.GOLD)
                        .append(Component.literal("3x3 Mining is now ")
                                .withStyle(ChatFormatting.WHITE))
                        .append(Component.literal("ENABLED")
                                .withStyle(ChatFormatting.GREEN)));
            } else {
                player.sendSystemMessage(Component.literal("[Miner 3x3] ")
                        .withStyle(ChatFormatting.GOLD)
                        .append(Component.literal("3x3 Mining is now ")
                                .withStyle(ChatFormatting.WHITE))
                        .append(Component.literal("DISABLED")
                                .withStyle(ChatFormatting.RED)));
            }
            return 1;
        }

        source.sendFailure(Component.literal("This command can only be used by a player."));
        return 0;
    }
}