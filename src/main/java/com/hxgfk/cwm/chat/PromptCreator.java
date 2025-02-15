package com.hxgfk.cwm.chat;

import com.hxgfk.cwm.util.MessageEntry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedList;

public interface PromptCreator <T extends LivingEntity> {
    LinkedList<MessageEntry> createPrompts(T entity, Player player);
    LinkedList<MessageEntry> createUpdatePrompts(T entity, Player player);
}
