package com.abrenchev.updatehandler;

public class MessageContext {
    private final int chatId;

    public MessageContext(int chatId) {
        this.chatId = chatId;
    }

    public int getChatId() {
        return chatId;
    }
}
