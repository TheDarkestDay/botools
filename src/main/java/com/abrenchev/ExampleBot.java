package com.abrenchev;

import com.abrenchev.annotations.HandleCommand;
import com.abrenchev.annotations.MessageHandler;

public class ExampleBot {
    private int counter = 0;

    @MessageHandler(messageType = "HELLO")
    public String sayHello() {
        return "HELLO, FROM BOB";
    }

    @HandleCommand(
            command = "/count",
            description = "Outputs current counter value"
    )
    public String count() {
        int newValue = counter++;

        return String.valueOf(newValue);
    }
}
