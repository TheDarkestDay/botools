package com.abrenchev.updatehandler;

public class MessageContext {
    private final long chatId;

    private final String messageText;

    public MessageContext(long chatId, String messageText) {
        this.chatId = chatId;
        this.messageText = messageText;
    }

    public long getChatId() {
        return chatId;
    }

    public String getMessageText() {
        return messageText;
    }
}
