package com.abrenchev;

import com.abrenchev.annotations.HandleCommand;
import com.abrenchev.annotations.MessageHandler;
import com.abrenchev.domain.Poll;
import com.abrenchev.domain.PollBuilder;
import com.abrenchev.updatehandler.MessageContext;

public class ExampleBot {
    private int counter = 0;

    @MessageHandler(messageType = "HELLO")
    public String sayHello(MessageContext context) {
        return "HELLO, FROM BOB";
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
}
