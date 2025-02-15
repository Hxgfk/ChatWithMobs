package com.hxgfk.cwm.util;

import net.minecraft.nbt.CompoundTag;

public class MessageEntry {
    private MessageType type;
    private String content;

    public MessageEntry(MessageType type, String content) {
        this.content = content;
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public String getContent() {
        return this.content;
    }

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
