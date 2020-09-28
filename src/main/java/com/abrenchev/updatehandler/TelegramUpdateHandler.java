package com.abrenchev.updatehandler;

import com.abrenchev.domain.TelegramUpdate;
import com.abrenchev.exceptions.BotoolsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class TelegramUpdateHandler {
     public abstract Object handleUpdate(TelegramUpdate update, Object botInstance);

     protected Object runMessageHandler(Method messageHandler, Object botInstance, TelegramUpdate update) {
          var sourceMessage = update.message != null ? update.message : update.channel_post;

          var chatId = sourceMessage != null ? sourceMessage.chat.id : null;
          var messageText = sourceMessage != null ? sourceMessage.text : null;
          var messageContext = new MessageContext(chatId, messageText);

          try {
               return messageHandler.invoke(botInstance, messageContext);
          } catch (IllegalAccessException | InvocationTargetException exception) {
               throw new BotoolsException("An exception occurred inside message handler", exception);
          }
     }
}
