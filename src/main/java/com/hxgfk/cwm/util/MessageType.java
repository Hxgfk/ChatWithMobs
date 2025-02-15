package com.hxgfk.cwm.util;

public enum MessageType {
    SYSTEM,
    USER,
    ASSISTANT;

    String type() {
        return this.name().toLowerCase();
    }
}
