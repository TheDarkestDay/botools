package com.abrenchev;

import com.abrenchev.annotations.HandleChannelPost;
import com.abrenchev.annotations.HandleCommand;
import com.abrenchev.annotations.HandleMessage;
import com.abrenchev.domain.Poll;
import com.abrenchev.domain.PollBuilder;
import com.abrenchev.updatehandler.MessageContext;

import java.util.concurrent.CompletableFuture;

public class ExampleBot {
    private int counter = 0;

    @HandleMessage()
    public String doEcho(MessageContext context) {
        return "Got message: " + context.getMessageText();
    }

    @HandleCommand(
            command = "/count",
            description = "Outputs current counter value"
    )
    public String count(MessageContext context) {
        int newValue = counter++;

        return String.valueOf(newValue);
    }


    @HandleCommand(
            command = "/poll",
            description = "Shows demo poll"
    )
    public Poll runDemoPoll(MessageContext context) {
        return new PollBuilder()
                .setQuestion("Do you want to visit a cinema tonight?")
                .addOption("Yes")
                .addOption("No")
                .build();
    }

    @HandleChannelPost()
    public CompletableFuture<String> praiseJava(MessageContext context) {
        String messageText = context.getMessageText();
        if (messageText.contains("Java")) {
            return CompletableFuture.completedFuture("Java is awesome");
        }

        return null;
    }
}
