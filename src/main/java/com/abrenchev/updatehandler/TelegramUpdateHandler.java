package com.abrenchev.updatehandler;

import com.abrenchev.domain.TelegramUpdate;

public interface TelegramUpdateHandler {
     Object handleUpdate(TelegramUpdate update, Object botInstance);
}
