package com.abrenchev.updatehandler;

import com.abrenchev.annotations.MessageHandler;
import com.abrenchev.domain.TelegramUpdate;
import com.abrenchev.exceptions.BotoolsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DirectMessageHandler implements TelegramUpdateHandler {
    public Object handleUpdate(TelegramUpdate update, Object botInstance) {
        Method handler = getMessageHandler(botInstance, update.message.text);

        if (handler != null) {
            return runMessageHandler(handler, botInstance);
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

    private Object runMessageHandler(Method messageHandler, Object botInstance) {
        try {
            return messageHandler.invoke(botInstance);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new BotoolsException("An exception occurred inside message handler", exception);
        }
    }
}
