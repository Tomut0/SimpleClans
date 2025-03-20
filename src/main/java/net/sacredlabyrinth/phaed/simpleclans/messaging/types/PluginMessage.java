package net.sacredlabyrinth.phaed.simpleclans.messaging.types;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.sacredlabyrinth.phaed.simpleclans.messaging.MessageChannel;

public class PluginMessage extends MessageImpl<byte[]> {

    @SuppressWarnings("UnstableApiUsage")
    private final ByteArrayDataOutput output = ByteStreams.newDataOutput();

    public PluginMessage(MessageChannel.Subchannel subchannel, String publisher) {
        super(subchannel, publisher);
    }

    public PluginMessage asForward(ByteArrayDataOutput object, boolean all) {
        output.writeUTF("Forward");
        output.writeUTF(all ? "ALL" : "ONLINE");
        output.writeUTF(channel.formatted());

        output.writeShort(object.toByteArray().length);
        output.write(object.toByteArray());

        return this;
    }

    public PluginMessage asForward(ByteArrayDataOutput object) {
        return asForward(object, false);
    }

    public PluginMessage asForward(String object) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF(object);

        return asForward(dataOutput, false);
    }

    public PluginMessage asPlayerList() {
        output.writeUTF("PlayerList");
        output.writeUTF("ALL");

        return this;
    }

    public PluginMessage asPlayerMessage(String playerName, String message) {
        output.writeUTF(playerName);
        output.writeUTF(message);

        return this;
    }

    @Override
    public byte[] getContent() {
        return output.toByteArray();
    }
}
