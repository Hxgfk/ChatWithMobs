package com.hxgfk.cwm.handler;

import com.hxgfk.cwm.chat.IMobChat;
import com.hxgfk.cwm.chat.PromptManager;
import com.hxgfk.cwm.main.CWM;
import com.hxgfk.cwm.main.ModCommand;
import com.hxgfk.cwm.network.ModDispatcher;
import com.hxgfk.cwm.network.OpenMsgScreenPacket;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.MissingMappingsEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void openUI(AttackEntityEvent event) {
        if (event.getTarget() instanceof Player) return;
        if (event.getTarget() instanceof LivingEntity le && event.getEntity() instanceof ServerPlayer sp && sp.isCrouching()) {
            if (((IMobChat)le).isChatting()) return;
            event.setCanceled(true);
            le.lookAt(EntityAnchorArgument.Anchor.EYES, sp.position());
            ((IMobChat) le).stopAI();
            ModDispatcher.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> sp),
                    new OpenMsgScreenPacket(sp.getLevel().dimension(), le.getId())
            );
        }
    }

    @SubscribeEvent
    public static void missingMapping(MissingMappingsEvent event) {
        event.getMappings(PromptManager.REGISTRY.get().getRegistryKey(), CWM.MODID)
                .stream().filter(mapping -> mapping.getKey().getPath().contains("test"))
                .forEach(MissingMappingsEvent.Mapping::warn);
    }

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        ModCommand.register(event.getDispatcher());
    }
}
