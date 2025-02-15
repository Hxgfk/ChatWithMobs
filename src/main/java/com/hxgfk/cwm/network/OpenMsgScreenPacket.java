package com.hxgfk.cwm.network;

import com.hxgfk.cwm.gui.SendMessage;
import com.hxgfk.cwm.main.CWM;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Server to Client
 * @param dimensionId
 * @param entityId
 */
public record OpenMsgScreenPacket(ResourceKey<Level> dimensionId, int entityId) {
    public void encoder(FriendlyByteBuf buffer) {
        buffer.writeUtf(dimensionId.location().toString());
        buffer.writeInt(entityId);
    }

    public static OpenMsgScreenPacket decoder(FriendlyByteBuf buffer) {
        return new OpenMsgScreenPacket(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(buffer.readUtf())), buffer.readInt());
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            if (entity != null && entity instanceof Mob mob) {
                Minecraft.getInstance().setScreen(new SendMessage(mob));
            } else {
                CWM.LOGGER.error("Invalid packet {}.", ctx.get().hashCode());
            }
        }));
        ctx.get().setPacketHandled(true);
    }
}
