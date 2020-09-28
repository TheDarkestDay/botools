package com.abrenchev.service;

import com.abrenchev.domain.Poll;
import com.google.gson.Gson;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class TelegramApiService {
    private final String authToken;

    public TelegramApiService(String authToken) {
        this.authToken = authToken;
    }

    public CompletableFuture<?> sendObject(long chatId, Object data) {
        if (data instanceof Poll) {
            return sendPoll(chatId, data);
        }

        return this.sendMessage(chatId, data);
    }

    private CompletableFuture<?> sendPoll(long chatId, Object data) {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        Poll pollToSend = (Poll) data;
        Gson gson = new Gson();

        String question = URLEncoder.encode(pollToSend.getQuestion(), StandardCharsets.UTF_8);
        String options = URLEncoder.encode(gson.toJson(pollToSend.getOptions()), StandardCharsets.UTF_8);
        HttpRequest sendMessageRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.telegram.org/bot" + authToken + "/sendPoll?chat_id=" + chatId + "&question=" + question + "&options=" + options + "&is_anonymous=" + pollToSend.isAnonymous()))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.sendAsync(sendMessageRequest, HttpResponse.BodyHandlers.ofString());
    }

    private CompletableFuture<?> sendMessage(long chatId, Object data) {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        String responseMessage = URLEncoder.encode(data.toString(), StandardCharsets.UTF_8);
        HttpRequest sendMessageRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.telegram.org/bot" + authToken + "/sendMessage?chat_id=" + chatId + "&text=" + responseMessage))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.sendAsync(sendMessageRequest, HttpResponse.BodyHandlers.ofString());
    }
}
