package com.abrenchev.updatehandler;

import com.abrenchev.annotations.HandleCommand;
import com.abrenchev.domain.TelegramMessage;
import com.abrenchev.domain.TelegramMessageEntity;
import com.abrenchev.domain.TelegramMessageEntityType;
import com.abrenchev.domain.TelegramUpdate;

import java.lang.reflect.Method;
import java.util.Optional;

public class CommandHandler extends TelegramUpdateHandler {
    @Override
    public Object handleUpdate(TelegramUpdate update, Object botInstance) {
        Method handler = getMessageHandler(botInstance, update.message);

        if (handler != null) {
            return runMessageHandler(handler, botInstance, update);
        }

        return null;
    }

    protected String extractCommand(TelegramMessage message) {
        if (message == null || message.entities == null) {
            return null;
        }

        Optional<TelegramMessageEntity> enteredCommandEntity = message.entities.stream()
                .filter(entity -> entity.type == TelegramMessageEntityType.bot_command)
                .findFirst();

        if (enteredCommandEntity.isEmpty()) {
            return null;
        }

        TelegramMessageEntity commandEntity = enteredCommandEntity.get();
        int offset = commandEntity.offset;
        int length = commandEntity.length;

        return message.text.substring(offset, length);
    }

    private Method getMessageHandler(Object botInstance, TelegramMessage message) {
        Class<?> clazz = botInstance.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        String enteredCommand = extractCommand(message);
        if (enteredCommand == null) {
            return null;
        }

        for (Method botMethod : methods) {
            if (botMethod.isAnnotationPresent(HandleCommand.class)) {
                String commandToHandle = botMethod.getAnnotation(HandleCommand.class).command();

                if (enteredCommand.equals(commandToHandle)) {
                    return botMethod;
                }
            }
        }

        return null;
    }
}
