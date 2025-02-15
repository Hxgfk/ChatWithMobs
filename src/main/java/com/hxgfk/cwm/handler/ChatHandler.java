package com.hxgfk.cwm.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hxgfk.cwm.chat.IMobChat;
import com.hxgfk.cwm.chat.PromptManager;
import com.hxgfk.cwm.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedList;

public class ChatHandler {
    public static void handle(LivingEntity entity, Player sender, String message) {
        ((IMobChat)entity).setChatting(true);
        sender.sendSystemMessage(Component.literal("<").append(sender.getDisplayName()).append("> ").append(message));
        LinkedList<MessageEntry> ALL_HISTORY = new LinkedList<>(((IMobChat) entity).getMessages());
        ALL_HISTORY.addAll(PromptManager.of(entity, sender));
        ALL_HISTORY.add(new MessageEntry(MessageType.USER, message));
        new Thread(() -> {
            try {
                RequestResult result = ModelAPI.sendMessage(ALL_HISTORY, ((IMobChat)entity).getTemperature());
                if (result.getStatus() == 200) {
                    JsonObject resultObj = JsonParser.parseString(result.getData()).getAsJsonObject();
                    String returnMsg = resultObj.getAsJsonArray("choices")
                            .get(0).getAsJsonObject()
                            .getAsJsonObject("message")
                            .get("content").getAsString();
                    Component cp = Component.literal("<")
                            .append(entity.getDisplayName())
                            .append("> ")
                            .append(returnMsg);
                    sender.sendSystemMessage(cp);
                    ALL_HISTORY.add(new MessageEntry(MessageType.ASSISTANT, returnMsg));
                    ((IMobChat) entity).setAll(ALL_HISTORY);
                } else {
                    sender.sendSystemMessage(Component.translatable("msg.exec.err.failReq").append(" "+result.getStatus()+" ").append(result.getData()).withStyle(ChatFormatting.RED));
                }
            }catch (Exception e) {
                sender.sendSystemMessage(Component.translatable("msg.exec.err.failReq").append(" "+e).withStyle(ChatFormatting.RED));
            }
            ((IMobChat)entity).setChatting(false);
        }).start();
    }
}
