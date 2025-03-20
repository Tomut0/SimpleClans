package net.sacredlabyrinth.phaed.simpleclans.messaging.types;

import net.sacredlabyrinth.phaed.simpleclans.messaging.MessageChannel;

public abstract class MessageImpl<T> implements Message<T> {

    protected final MessageChannel channel;

    public MessageImpl(MessageChannel.Subchannel subchannel, String publisher) {
        this.channel = new MessageChannel(subchannel, publisher);
    }

    public MessageChannel getChannel() {
        return channel;
    }
}
