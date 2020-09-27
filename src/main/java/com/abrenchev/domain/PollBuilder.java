package com.abrenchev.domain;

import java.util.ArrayList;
import java.util.List;

public class PollBuilder {
    private String question;

    private List<String> options = new ArrayList<>();

    private boolean is_anonymous;

    public Poll build() {
        return new Poll(
                question,
                options,
                is_anonymous
        );
    }

    public PollBuilder setQuestion(String question) {
        this.question = question;

        return this;
    }

    public PollBuilder addOption(String option) {
        options.add(option);

        return this;
    }

    public PollBuilder makeAnonymous() {
        is_anonymous = true;

        return this;
    }

    public PollBuilder makePublic() {
        is_anonymous = false;

        return this;
    }
}
