package com.hxgfk.cwm.network;

import com.hxgfk.cwm.handler.ChatHandler;
import com.hxgfk.cwm.main.CWM;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Client to Server
 * @param world
 * @param message
 * @param entityId
 */
public record MessagePacket(ResourceKey<Level> world, String message, int entityId) {

    public void encoder(FriendlyByteBuf buffer) {
        buffer.writeUtf(world.location().toString());
        buffer.writeUtf(message);
        buffer.writeInt(entityId);
    }

    public static MessagePacket decoder(FriendlyByteBuf buffer) {
        ResourceKey<Level> k = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(buffer.readUtf()));
        return new MessagePacket(
                k,
                buffer.readUtf(),
                buffer.readInt()
        );
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerLevel level = ctx.get().getSender().getServer().getLevel(world);
            Entity entity = level.getEntity(entityId);
            if (entity instanceof Mob mob) {
                ChatHandler.handle(mob, ctx.get().getSender(), message);
            } else {
                CWM.LOGGER.error("Invalid packet {}", ctx.get().hashCode());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
