package com.hxgfk.cwm.chat.vanilla;

import com.hxgfk.cwm.chat.PromptCreator;
import com.hxgfk.cwm.chat.PromptManager;
import com.hxgfk.cwm.util.MessageEntry;
import com.hxgfk.cwm.util.MessageType;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class VillagerPrompt implements PromptCreator<Villager> {
    @Override
    public boolean canChat(Villager entity) {
        return !entity.isTrading();
    }

    public static String toTradings(Villager villager) {
        List<String> offerStrings = new ArrayList<>();
        for (MerchantOffer offer : villager.getOffers()) {
            ItemStack a = offer.getCostA();
            ItemStack b = offer.getCostB();
            ItemStack res = offer.getResult();
            int max = offer.getMaxUses();
            if (b.isEmpty()) {
                offerStrings.add(I18n.get("prompt.villager.tradeA",
                        a.getCount(),
                        PromptManager.item(a),
                        res.getCount(),
                        PromptManager.item(res),
                        max
                ));
            } else {
                offerStrings.add(I18n.get("prompt.villager.tradeB",
                        a.getCount(),
                        PromptManager.item(a),
                        b.getCount(),
                        PromptManager.item(b),
                        res.getCount(),
                        PromptManager.item(res),
                        max
                ));
            }
        }
        return I18n.get("prompt.villager.trades", Arrays.toString(offerStrings.toArray()));
    }

    @Override
    public LinkedList<MessageEntry> createPrompts(Villager entity, Player player) {
        LinkedList<MessageEntry> prompts = new LinkedList<>();
        BlockPos pos = entity.blockPosition();
        long[] time = PromptManager.time(entity.getLevel());
        String level = entity.getLevel().dimension().location().toString();
        String weather = PromptManager.toWeather(entity);
        ItemStack item = player.getMainHandItem();
        String profession = I18n.get("entity.minecraft.villager."+entity.getVillagerData().getProfession().name());
        boolean isReputable = entity.getPlayerReputation(player) > 5;
        boolean isHorrible = entity.getPlayerReputation(player) < 5;
        boolean isSkilledProfession = entity.getVillagerData().getLevel() > 3;
        String village = I18n.get("prompt.village", I18n.get("biome.minecraft."+entity.getVillagerData().getType()));
        ItemStack villagerItem = entity.getMainHandItem();
        if (!item.isEmpty()) {
            prompts.add(new MessageEntry(MessageType.SYSTEM, I18n.get("prompt.villager",
                    village,
                    entity.isBaby() ? I18n.get("prompt.villager.kid") : I18n.get("prompt.villager.adult"),
                    player.getName().getString(),
                    player.getName().getString(),
                    item.getItem().getDescription().getString(),
                    weather,
                    pos.getX(), pos.getY(), pos.getZ(),
                    level,
                    time[0], time[1], time[2]
            )));
        } else {
            prompts.add(new MessageEntry(MessageType.SYSTEM, I18n.get("prompt.villager.noItem",
                    village,
                    entity.isBaby() ? I18n.get("prompt.villager.kid") : I18n.get("prompt.villager.adult"),
                    player.getName().getString(),
                    weather,
                    pos.getX(), pos.getY(), pos.getZ(),
                    level,
                    time[0], time[1], time[2]
            )));
        }
        if (!villagerItem.isEmpty()) {
            prompts.add(new MessageEntry(MessageType.SYSTEM, I18n.get("prompt.villager.puttingItem", villagerItem.getItem().getDescription().getString())));
        }
        if (isHorrible) {
            prompts.add(new MessageEntry(MessageType.SYSTEM, I18n.get("prompt.villager.horrible", player.getName().getString())));
        }
        if (isReputable) {
            prompts.add(new MessageEntry(MessageType.SYSTEM, I18n.get("prompt.villager.reputable", player.getName().getString())));
        }
        if (entity.getVillagerData().getProfession() != VillagerProfession.NONE) {
            String p = I18n.get("prompt.villager.profession", profession);
            if (isSkilledProfession) {
                p += I18n.get("prompt.villager.profession.skilled");
            } else {
                p += I18n.get("prompt.end");
            }
            prompts.add(new MessageEntry(MessageType.SYSTEM, p));
        }
        if (!entity.getOffers().isEmpty()) {
            prompts.add(new MessageEntry(MessageType.SYSTEM, toTradings(entity)));
        }
        return prompts;
    }

    @Override
    public LinkedList<MessageEntry> createUpdatePrompts(Villager entity, Player player) {
        return createPrompts(entity, player);
    }
}
