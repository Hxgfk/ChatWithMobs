package com.hxgfk.cwm.chat;

import com.hxgfk.cwm.util.MessageEntry;

import java.util.LinkedList;

public interface IMobChat {
    LinkedList<MessageEntry> getMessages();
    void addMessage(MessageEntry msg);
    void clearMsgList();
    void setAll(LinkedList<MessageEntry> list);
    boolean isFirstChat();
    void setChatting(boolean chatting);
    boolean isChatting();
    float getTemperature();
    void setTemperature(float temperature);
    float randomTemperature();
    void stopAI();
}
