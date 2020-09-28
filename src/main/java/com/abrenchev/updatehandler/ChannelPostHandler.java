package com.abrenchev.updatehandler;

import com.abrenchev.annotations.HandleChannelPost;
import com.abrenchev.domain.TelegramUpdate;

import java.lang.reflect.Method;

public class ChannelPostHandler extends TelegramUpdateHandler {
    @Override
    public Object handleUpdate(TelegramUpdate update, Object botInstance) {
        if (update.channel_post == null) {
            return null;
        }

        var channelPostHandler = getChannelPostHandler(botInstance);

        return runMessageHandler(channelPostHandler, botInstance, update);
    }

    private Method getChannelPostHandler(Object botInstance) {
        Class<?> clazz = botInstance.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method botMethod : methods) {
            if (botMethod.isAnnotationPresent(HandleChannelPost.class)) {
                return botMethod;
            }
        }

        return null;
    }
}
