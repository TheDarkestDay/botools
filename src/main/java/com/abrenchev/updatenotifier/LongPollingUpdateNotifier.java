package com.abrenchev.updatenotifier;

import com.abrenchev.domain.TelegramResponse;
import com.abrenchev.domain.TelegramUpdate;
import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LongPollingUpdateNotifier extends UpdateNotifier {
    private long lastUpdateId = 0;

    public LongPollingUpdateNotifier(String authToken) {
        super(authToken);

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        Gson gson = new Gson();

        executorService.scheduleAtFixedRate(() -> {
            HttpClient httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();

            HttpRequest getUpdatesRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.telegram.org/bot" + authToken + "/getUpdates?offset=" + lastUpdateId))
                    .header("Content-Type", "application/json")
                    .build();

            httpClient.sendAsync(getUpdatesRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept((responseBody) -> {
                        TelegramResponse response = gson.fromJson(responseBody, TelegramResponse.class);

                        for (TelegramUpdate update : response.result) {
                            notifyListeners(update);
                            lastUpdateId = update.update_id + 1;
                        }
                    });

        }, 0, 30, TimeUnit.SECONDS);
    }
}
