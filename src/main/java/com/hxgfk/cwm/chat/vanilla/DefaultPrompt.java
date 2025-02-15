package com.hxgfk.cwm.chat.vanilla;

import com.hxgfk.cwm.chat.PromptCreator;
import com.hxgfk.cwm.chat.PromptManager;
import com.hxgfk.cwm.util.MessageEntry;
import com.hxgfk.cwm.util.MessageType;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedList;

public class DefaultPrompt implements PromptCreator {
    @Override
    public LinkedList<MessageEntry> createPrompts(LivingEntity entity, Player player) {
        LinkedList<MessageEntry> prompts = new LinkedList<>();
        BlockPos pos = entity.blockPosition();
        String entityName = entity.getType().getDescription().getString();
        String theName = entityName;
        if (entity.hasCustomName()) {
            theName = I18n.get("prompt.entity.withName", entity.getCustomName().getString(), entityName);
        }
        long[] time = PromptManager.time(entity.getLevel());
        ItemStack item = player.getMainHandItem();
        if (!item.isEmpty()) {
            prompts.add(new MessageEntry(MessageType.SYSTEM, I18n.get(
                    "prompt.default",
                    theName,
                    player.getName().getString(),
                    player.getName().getString(),
                    player.getMainHandItem().getItem().getDescription().getString(),
                    PromptManager.toWeather(entity),
                    pos.getX(), pos.getY(), pos.getZ(),
                    entity.getLevel().dimension().location().toString(),
                    time[0], time[1], time[2]
            )));
        } else {
            prompts.add(new MessageEntry(MessageType.SYSTEM, I18n.get(
                    "prompt.default.noItem",
                    theName,
                    player.getName().getString(),
                    PromptManager.toWeather(entity),
                    pos.getX(), pos.getY(), pos.getZ(),
                    entity.getLevel().dimension().location().toString(),
                    time[0], time[1], time[2]
            )));
        }
        return prompts;
    }

    @Override
    public LinkedList<MessageEntry> createUpdatePrompts(LivingEntity entity, Player player) {
        LinkedList<MessageEntry> prompts = new LinkedList<>();
        String theName = entity.getType().getDescription().getString();
        if (entity.hasCustomName()) {
            theName = entity.getCustomName().getString();
        }
        BlockPos pos = entity.blockPosition();
        long[] time = PromptManager.time(entity.getLevel());
        ItemStack item = player.getMainHandItem();
        if (!item.isEmpty()) {
            prompts.add(new MessageEntry(MessageType.SYSTEM, I18n.get(
                    "prompt.default.update",
                    theName,
                    player.getName().getString(),
                    player.getName().getString(),
                    player.getMainHandItem().getItem().getDescription().getString(),
                    PromptManager.toWeather(entity),
                    pos.getX(), pos.getY(), pos.getZ(),
                    entity.getLevel().dimension().location().toString(),
                    time[0], time[1], time[2]
            )));
        } else {
            prompts.add(new MessageEntry(MessageType.SYSTEM, I18n.get(
                    "prompt.default.update.noItem",
                    theName,
                    player.getName().getString(),
                    PromptManager.toWeather(entity),
                    pos.getX(), pos.getY(), pos.getZ(),
                    entity.getLevel().dimension().location().toString(),
                    time[0], time[1], time[2]
            )));
        }
        return prompts;
    }
}
