package com.abrenchev.updatenotifier;

import com.abrenchev.domain.TelegramUpdate;

@FunctionalInterface
public interface UpdateListener {
    void handleUpdate(TelegramUpdate update);
}
