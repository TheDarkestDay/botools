package com.abrenchev.updatehandler;

import com.abrenchev.domain.TelegramUpdate;
import com.abrenchev.exceptions.BotoolsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class TelegramUpdateHandler {
     public abstract Object handleUpdate(TelegramUpdate update, Object botInstance);

     protected Object runMessageHandler(Method messageHandler, Object botInstance, TelegramUpdate update) {
          var chatId = update.message != null ? update.message.chat.id : null;
          var messageContext = new MessageContext(chatId);

          try {
               return messageHandler.invoke(botInstance, messageContext);
          } catch (IllegalAccessException | InvocationTargetException exception) {
               throw new BotoolsException("An exception occurred inside message handler", exception);
          }
     }
}
