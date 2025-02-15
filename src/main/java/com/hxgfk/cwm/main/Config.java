package com.hxgfk.cwm.main;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<String> OBJ_BASE_URL = builder.comment("The base url of api.").define("base_url", "");
    private static final ForgeConfigSpec.ConfigValue<String> OBJ_KEY = builder.comment("Your api key.").define("key", "");
    private static final ForgeConfigSpec.ConfigValue<String> OBj_MODEL = builder.comment("The target model.").define("model", "");
    private static final ForgeConfigSpec.IntValue OBJ_MAX_INPUT = builder.comment("Maximum number of characters to enter.").defineInRange("maximum_inputs", 350, 0, Integer.MAX_VALUE);
    public static String base_url = "";
    public static String key = "";
    public static String model = "";
    public static int maximum_inputs = 350;

    public static final ForgeConfigSpec SPEC = builder.build();

    @SubscribeEvent
    public static void load(ModConfigEvent event) {
        base_url = OBJ_BASE_URL.get();
        key = OBJ_KEY.get();
        model = OBj_MODEL.get();
        maximum_inputs = OBJ_MAX_INPUT.get();
    }
}
