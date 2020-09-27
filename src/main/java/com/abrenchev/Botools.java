package com.abrenchev;

import com.abrenchev.domain.TelegramUpdate;
import com.abrenchev.exceptions.BotoolsException;
import com.abrenchev.updatehandler.CommandHandler;
import com.abrenchev.updatehandler.DirectMessageHandler;
import com.abrenchev.updatehandler.TelegramUpdateHandler;
import com.abrenchev.updatenotifier.LongPollingUpdateNotifier;
import com.abrenchev.updatenotifier.UpdateNotifier;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Botools {
    public void runBot(String authToken, String botName, Class<?> clazz) {
        Object botInstance = createInstance(clazz);
        UpdateNotifier updateNotifier = new LongPollingUpdateNotifier(authToken);

        List<TelegramUpdateHandler> handlers = new ArrayList<>();
        handlers.add(new CommandHandler());
        handlers.add(new DirectMessageHandler());

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        updateNotifier.onUpdate((update) -> {
            Object botResponse = runHandlers(handlers, update, botInstance);
            if (botResponse != null) {
                String responseMessage = URLEncoder.encode(botResponse.toString(), StandardCharsets.UTF_8);
                HttpRequest sendMessageRequest = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.telegram.org/bot" + authToken + "/sendMessage?chat_id=" + 389330901 + "&text=" + responseMessage))
                        .header("Content-Type", "application/json")
                        .build();

                try {
                    var sendMessageResponse = httpClient.send(sendMessageRequest, HttpResponse.BodyHandlers.ofString());
                    System.out.println(sendMessageResponse.body());
                } catch (IOException | InterruptedException exception) {
                    throw new BotoolsException("Failed to send message to Telegram", exception);
                }
            }
        });
    }

    private Object runHandlers(List<TelegramUpdateHandler> handlers, TelegramUpdate update, Object botInstance) {
        for (TelegramUpdateHandler handler : handlers) {
            var result = handler.handleUpdate(update, botInstance);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new BotoolsException("Failed to create bot instance", exception);
        }
    }
}
