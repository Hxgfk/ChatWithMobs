package com.hxgfk.cwm.network;

import com.hxgfk.cwm.main.CWM;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModDispatcher {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(CWM.MODID, "network"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void registerPackets() {
        int i = 0;
        INSTANCE.registerMessage(i++, MessagePacket.class, MessagePacket::encoder, MessagePacket::decoder, MessagePacket::handler);
        INSTANCE.registerMessage(i++, OpenMsgScreenPacket.class, OpenMsgScreenPacket::encoder, OpenMsgScreenPacket::decoder, OpenMsgScreenPacket::handler);
    }
}
