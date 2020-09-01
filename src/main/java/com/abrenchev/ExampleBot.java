package com.abrenchev;

import com.abrenchev.annotations.MessageHandler;

public class ExampleBot {
    @MessageHandler(messageType = "HELLO")
    public String sayHello() {
        return "HELLO, FROM BOB";
    }
}
