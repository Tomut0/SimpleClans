package net.sacredlabyrinth.phaed.simpleclans.messaging.types;

import net.sacredlabyrinth.phaed.simpleclans.messaging.MessageChannel;

public interface Message<R> {

    MessageChannel getChannel();

    R getContent();
}
