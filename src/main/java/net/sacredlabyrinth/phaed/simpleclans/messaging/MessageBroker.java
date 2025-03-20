package net.sacredlabyrinth.phaed.simpleclans.messaging;

import net.sacredlabyrinth.phaed.simpleclans.messaging.types.Message;

import java.util.List;

public interface MessageBroker<T, R extends Message<T>> {

    List<String> getSubscribers();

    String getPublisher();

    void send(R message);
}
