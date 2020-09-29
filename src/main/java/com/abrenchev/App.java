package com.abrenchev;

public class App {
    public static void main(String... args) {
        var botools = new Botools();
        botools.runBot("your-secret-key", "your-bot-name", ExampleBot.class);
    }
}
