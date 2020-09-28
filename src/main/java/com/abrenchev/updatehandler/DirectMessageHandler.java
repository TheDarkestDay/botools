package com.abrenchev.updatehandler;

import com.abrenchev.annotations.MessageHandler;
import com.abrenchev.domain.TelegramUpdate;

import java.lang.reflect.Method;

public class DirectMessageHandler extends TelegramUpdateHandler {
    public Object handleUpdate(TelegramUpdate update, Object botInstance) {
        if (update.message == null) {
            return null;
        }

        Method handler = getMessageHandler(botInstance, update.message.text);

        if (handler != null) {
            return runMessageHandler(handler, botInstance, update);
        }

        return null;
    }

    private Method getMessageHandler(Object botInstance, String messageText) {
        Class<?> clazz = botInstance.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method botMethod : methods) {
            if (botMethod.isAnnotationPresent(MessageHandler.class)) {
                String handlerMessage = botMethod.getAnnotation(MessageHandler.class).messageType();
                if (messageText.equals(handlerMessage)) {
                    return botMethod;
                }
            }
        }

        return null;
    }
}
