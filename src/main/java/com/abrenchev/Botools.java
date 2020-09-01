package com.abrenchev;

import com.abrenchev.annotations.MessageHandler;
import com.abrenchev.domain.TelegramResponse;
import com.abrenchev.domain.TelegramUpdate;
import com.abrenchev.exceptions.BotoolsException;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Botools {
    private long lastUpdateId = 0;

    public void runBot(String authToken, String botName, Class<?> clazz) {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        Gson gson = new Gson();
        Object botInstance = createInstance(clazz);

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

        executorService.scheduleAtFixedRate(() -> {
            HttpRequest getUpdatesRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.telegram.org/bot" + authToken + "/getUpdates?offset=" + lastUpdateId))
                    .header("Content-Type", "application/json")
                    .build();

            httpClient.sendAsync(getUpdatesRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept((responseBody) -> {
                        TelegramResponse response = gson.fromJson(responseBody, TelegramResponse.class);

                        for (TelegramUpdate update : response.result) {
                            System.out.println(update.message.text);
                            Method messageHandler = getMessageHandler(clazz, update.message.text);
                            if (messageHandler != null) {
                                String botResponse = URLEncoder.encode(runMessageHandler(messageHandler, botInstance).toString(), StandardCharsets.UTF_8);

                                System.out.println(botResponse);

                                HttpRequest sendMessageRequest = HttpRequest.newBuilder()
                                        .uri(URI.create("https://api.telegram.org/bot" + authToken + "/sendMessage?chat_id=" + 389330901 + "&text=" + botResponse))
                                        .header("Content-Type", "application/json")
                                        .build();

                                try {
                                    var sendMessageResponse = httpClient.send(sendMessageRequest, HttpResponse.BodyHandlers.ofString());
                                    System.out.println(sendMessageResponse.body());
                                } catch (IOException | InterruptedException exception) {
                                    throw new BotoolsException("Failed to send message to Telegram", exception);
                                }
                            }

                            lastUpdateId = update.update_id + 1;
                        }
                    });

        }, 0, 1, TimeUnit.MINUTES);
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new BotoolsException("Failed to create bot instance", exception);
        }
    }

    private Object runMessageHandler(Method messageHandler, Object botInstance) {
        try {
            return messageHandler.invoke(botInstance);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new BotoolsException("An exception occurred inside message handler", exception);
        }
    }

    private Method getMessageHandler(Class<?> clazz, String messageText) {
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
