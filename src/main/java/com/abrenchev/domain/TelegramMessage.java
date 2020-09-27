package com.abrenchev.domain;

import java.util.List;

public class TelegramMessage {
    public long message_id;
    public Object from;
    public String text;
    public TelegramChat chat;
    public List<TelegramMessageEntity> entities;
}
