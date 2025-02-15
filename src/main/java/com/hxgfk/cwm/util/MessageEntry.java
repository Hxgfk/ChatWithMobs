package com.hxgfk.cwm.util;

import net.minecraft.nbt.CompoundTag;

public record MessageEntry(MessageType type, String content) {
    public CompoundTag toNbt() {
        CompoundTag t = new CompoundTag();
        t.putString("content", content);
        t.putString("type", type.name());
        return t;
    }

    @Override
    public String toString() {
        return "MessageEntry{" +
                "type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
