package com.library.notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private final List<BookReturnedListener> listeners = new ArrayList<>();

    public void addListener(BookReturnedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(BookReturnedListener listener) {
        listeners.remove(listener);
    }

    public void notifyBookReturned(BookReturnedEvent event) {
        for (BookReturnedListener listener : listeners) {
            listener.onBookReturned(event);
        }
    }
}
