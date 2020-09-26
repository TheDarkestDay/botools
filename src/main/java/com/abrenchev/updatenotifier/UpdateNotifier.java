package com.abrenchev.updatenotifier;

import com.abrenchev.domain.TelegramUpdate;

import java.util.ArrayList;
import java.util.List;

public abstract class UpdateNotifier {
    private List<UpdateListener> listeners = new ArrayList<>();

    protected String authToken;

    public UpdateNotifier(String authToken) {
        this.authToken = authToken;
    }

    public void onUpdate(UpdateListener listener) {
        listeners.add(listener);
    }

    protected void notifyListeners(TelegramUpdate update) {
        listeners.forEach((listener) -> listener.handleUpdate(update));
    }
}
