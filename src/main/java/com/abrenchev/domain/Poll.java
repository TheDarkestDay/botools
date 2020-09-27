package com.abrenchev.domain;

import java.util.List;

public class Poll {
    private String question;

    private List<String> options;

    private boolean is_anonymous;

    public Poll(String question, List<String> options, boolean is_anonymous) {
        this.question = question;
        this.options = options;
        this.is_anonymous = is_anonymous;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public boolean isAnonymous() {
        return is_anonymous;
    }

    public void setAnonymous(boolean is_anonymous) {
        this.is_anonymous = is_anonymous;
    }
}
