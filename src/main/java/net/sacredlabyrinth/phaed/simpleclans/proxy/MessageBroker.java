package net.sacredlabyrinth.phaed.simpleclans.proxy;

import java.util.List;

public interface MessageBroker {

    List<String> getSubscribers();

    String getPublisher();

    <T> void send(MessageChannel channel, T message);
}
