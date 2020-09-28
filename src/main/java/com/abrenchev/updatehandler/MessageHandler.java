package com.abrenchev.updatehandler;

import com.abrenchev.annotations.HandleMessage;
import com.abrenchev.domain.TelegramUpdate;

import java.lang.reflect.Method;

public class MessageHandler extends TelegramUpdateHandler {
    public Object handleUpdate(TelegramUpdate update, Object botInstance) {
        if (update.message == null) {
            return null;
        }

        Method handler = getMessageHandler(botInstance);

        if (handler != null) {
            return runMessageHandler(handler, botInstance, update);
        }

        return null;
    }

    private Method getMessageHandler(Object botInstance) {
        Class<?> clazz = botInstance.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method botMethod : methods) {
            if (botMethod.isAnnotationPresent(HandleMessage.class)) {
                return botMethod;
            }
        }

        return null;
    }
}
