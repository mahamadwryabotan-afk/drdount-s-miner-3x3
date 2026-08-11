package com.miner3x3;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Miner3x3.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue MINER_3X3_ENABLED = BUILDER
            .comment("Enable 3x3 Mining mode")
            .define("miner3x3Enabled", false);

    public static final ForgeConfigSpec.BooleanValue FAST_MINE_ENABLED = BUILDER
            .comment("Enable Fast Mine speed boost")
            .define("fastMineEnabled", false);

    public static final ForgeConfigSpec.DoubleValue FAST_MINE_SPEED = BUILDER
            .comment("Fast Mine speed multiplier (higher = faster)")
            .defineInRange("fastMineSpeed", 3.0, 1.0, 10.0);

    public static final ForgeConfigSpec.IntValue GUI_KEY = BUILDER
            .comment("Keybind for opening the GUI (default: G = 34)")
            .defineInRange("guiKey", 34, 0, 255);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    private static boolean miner3x3Enabled = false;
    private static boolean fastMineEnabled = false;
    private static double fastMineSpeed = 3.0;
    private static int guiKey = 34;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        miner3x3Enabled = MINER_3X3_ENABLED.get();
        fastMineEnabled = FAST_MINE_ENABLED.get();
        fastMineSpeed = FAST_MINE_SPEED.get();
        guiKey = GUI_KEY.get();
    }

    public static boolean isMiner3x3Enabled() {
        return miner3x3Enabled;
    }

    public static void setMiner3x3Enabled(boolean enabled) {
        miner3x3Enabled = enabled;
        MINER_3X3_ENABLED.set(enabled);
        MINER_3X3_ENABLED.save();
    }

    public static boolean isFastMineEnabled() {
        return fastMineEnabled;
    }

    public static void setFastMineEnabled(boolean enabled) {
        fastMineEnabled = enabled;
        FAST_MINE_ENABLED.set(enabled);
        FAST_MINE_ENABLED.save();
    }

    public static double getFastMineSpeed() {
        return fastMineSpeed;
    }

    public static int getGuiKey() {
        return guiKey;
    }
}