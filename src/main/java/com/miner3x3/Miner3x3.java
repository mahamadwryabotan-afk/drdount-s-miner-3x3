package com.miner3x3;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Miner3x3.MOD_ID)
public class Miner3x3 {
    public static final String MOD_ID = "miner3x3";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Miner3x3() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(new MiningHandler());
        MinecraftForge.EVENT_BUS.register(new FastMineHandler());
        MinecraftForge.EVENT_BUS.register(new ModCommands());

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> com.miner3x3.client.ClientSetup::registerClientEvents);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Miner 3x3 mod loaded!");
    }
}