package com.abrenchev.command;

import com.abrenchev.exceptions.BotoolsException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class SendMessageCommand extends BotoolsCommand {
    @Override
    public void execute(String authToken, Object arguments) {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        String responseMessage = URLEncoder.encode(arguments.toString(), StandardCharsets.UTF_8);
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
}
