package com.miner3x3.client;

import com.miner3x3.ModConfig;
import com.miner3x3.Miner3x3;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Miner3x3.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static final String KEY_CATEGORY = "key.categories.miner3x3";
    public static final String KEY_OPEN_GUI = "key.miner3x3.open_gui";

    public static KeyMapping openGuiKey = new KeyMapping(
            KEY_OPEN_GUI,
            GLFW.GLFW_KEY_G,
            KEY_CATEGORY
    );

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(openGuiKey);
    }

    public static void registerClientEvents() {
        MinecraftForge.EVENT_BUS.addListener(ClientSetup::onKeyInput);
    }

    public static void onKeyInput(InputEvent.Key event) {
        if (openGuiKey.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.setScreen(new MinerGuiScreen());
            }
        }
    }
}