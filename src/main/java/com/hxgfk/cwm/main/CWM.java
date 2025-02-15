package com.hxgfk.cwm.main;

import com.hxgfk.cwm.chat.PromptManager;
import com.hxgfk.cwm.network.ModDispatcher;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CWM.MODID)
public class CWM {
    public static final String MODID = "cwm";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public CWM(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        bus.addListener(this::setup);
        PromptManager.init(bus);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
        ModDispatcher.registerPackets();
    }
}
