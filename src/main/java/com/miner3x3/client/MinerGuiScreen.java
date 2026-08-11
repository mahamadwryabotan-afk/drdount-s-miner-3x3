package com.miner3x3.client;

import com.miner3x3.ModConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MinerGuiScreen extends Screen {
    private static final int GUI_WIDTH = 200;
    private static final int GUI_HEIGHT = 150;

    private Button miner3x3Button;
    private Button fastMineButton;

    public MinerGuiScreen() {
        super(Component.literal("Miner 3x3"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int startX = centerX - GUI_WIDTH / 2;
        int startY = centerY - GUI_HEIGHT / 2;

        // 3x3 Mining toggle button
        this.miner3x3Button = this.addRenderableWidget(new Button(
                startX + 10, startY + 35, GUI_WIDTH - 20, 20,
                getMiner3x3ButtonText(),
                (button) -> {
                    ModConfig.setMiner3x3Enabled(!ModConfig.isMiner3x3Enabled());
                    button.setMessage(getMiner3x3ButtonText());
                }));

        // Fast Mine toggle button
        this.fastMineButton = this.addRenderableWidget(new Button(
                startX + 10, startY + 60, GUI_WIDTH - 20, 20,
                getFastMineButtonText(),
                (button) -> {
                    ModConfig.setFastMineEnabled(!ModConfig.isFastMineEnabled());
                    button.setMessage(getFastMineButtonText());
                }));

        // Close button
        this.addRenderableWidget(new Button(
                startX + 10, startY + 85, GUI_WIDTH - 20, 20,
                Component.literal("Close"),
                (button) -> this.onClose()));
    }

    private Component getMiner3x3ButtonText() {
        String status = ModConfig.isMiner3x3Enabled()
                ? ChatFormatting.GREEN + "ON"
                : ChatFormatting.RED + "OFF";
        return Component.literal("3x3 Mining: " + status);
    }

    private Component getFastMineButtonText() {
        String status = ModConfig.isFastMineEnabled()
                ? ChatFormatting.GREEN + "ON"
                : ChatFormatting.RED + "OFF";
        return Component.literal("Fast Mine: " + status);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);

        int centerX = this.width / 2;
        int startY = this.height / 2 - GUI_HEIGHT / 2;

        // Title
        drawCenteredString(poseStack, this.font, "Miner 3x3", centerX, startY + 12, 0xFFFFFF);

        if (this.miner3x3Button != null) {
            drawCenteredString(poseStack, this.font,
                    Component.literal("Status: ")
                            .append(Component.literal(ModConfig.isMiner3x3Enabled() ? "3x3 ENABLED" : "3x3 DISABLED")
                                    .withStyle(ModConfig.isMiner3x3Enabled() ? ChatFormatting.GREEN : ChatFormatting.RED)),
                    centerX, startY + 112, 0xFFFFFF);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}