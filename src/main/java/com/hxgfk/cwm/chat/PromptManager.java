package com.hxgfk.cwm.chat;

import com.hxgfk.cwm.chat.vanilla.DefaultPrompt;
import com.hxgfk.cwm.chat.vanilla.VillagerPrompt;
import com.hxgfk.cwm.main.CWM;
import com.hxgfk.cwm.util.MessageEntry;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import java.util.LinkedList;
import java.util.function.Supplier;

public class PromptManager {
    private static final DeferredRegister<PromptCreator<?>> REGISTER = DeferredRegister.create(new ResourceLocation(CWM.MODID, "prompt_creators"), "minecraft");
    public static final Supplier<IForgeRegistry<PromptCreator<?>>> REGISTRY = REGISTER.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<PromptCreator<?>> DEFAULT = REGISTER.register("default", DefaultPrompt::new);
    public static final RegistryObject<PromptCreator<?>> VILLAGER = REGISTER.register("villager", VillagerPrompt::new);

    public static void init(IEventBus bus) {
        REGISTER.register(bus);
    }

    public static LinkedList<MessageEntry> of(LivingEntity entity, Player player) {
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        PromptCreator creator = DEFAULT.get();
        PromptCreator get = REGISTRY.get().getValue(id);
        if (get != null) {
            creator = get;
        }
        return ((IMobChat)entity).isFirstChat() ? creator.createPrompts(entity, player) : creator.createUpdatePrompts(entity, player);
    }

    public static final int TICKS_PER_DAY = 24000;

    public static long[] time(Level level) {
        long daytime = level.getDayTime();
        long days = daytime / TICKS_PER_DAY;
        long ticksOfDay = daytime % TICKS_PER_DAY;
        long totalMinutesInDay = 24 * 60;
        long minecraftMinutesInDay = TICKS_PER_DAY / 20;
        long realMinutes = (ticksOfDay * totalMinutesInDay) / minecraftMinutesInDay;
        long hours = (realMinutes / 60) % 24;
        long minutes = realMinutes % 60;
        return new long[]{days, hours, minutes};
    }

    public static String toWeather(LivingEntity livingEntity) {
        boolean isRain = livingEntity.getLevel().getLevelData().isRaining();
        boolean isThunderng = livingEntity.getLevel().getLevelData().isThundering();
        boolean isSnowBiome = livingEntity.getLevel().getBiome(livingEntity.blockPosition()).get().coldEnoughToSnow(livingEntity.blockPosition());

        if (isRain && isThunderng) {
            return I18n.get("prompt.weather.thundering");
        } else if (isRain) {
            if (isSnowBiome) {
                return I18n.get("prompt.weather.snow");
            } else {
                return I18n.get("prompt.weather.rain");
            }
        } else {
            return I18n.get("prompt.weather.clear");
        }
    }

    public static String item(ItemStack item) {
        if (item.hasCustomHoverName()) {
            return I18n.get("prompt.name", item.getDisplayName().getString(), item.getItem().getDescription().getString());
        } else {
            return item.getItem().getDescription().getString();
        }
    }

    public static String entity(LivingEntity entity) {
        if (!entity.hasCustomName()) return entity.getType().getDescription().getString();
        return I18n.get("prompt.name", entity.getDisplayName().getString(), entity.getType().getDescription().getString());
    }
}
