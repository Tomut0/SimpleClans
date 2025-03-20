package net.sacredlabyrinth.phaed.simpleclans.messaging;

import java.util.regex.Pattern;

public class MessageChannel {

    private static final Pattern CHANNEL_PATTERN =
            Pattern.compile("SimpleClans\\|(?<subchannel>\\w+)\\|(?<version>v\\d+)\\|(?<server>.+)");

    private final Subchannel subchannel;
    private final String publisher;

    public MessageChannel(Subchannel subchannel, String publisher) {
        this.subchannel = subchannel;
        this.publisher = publisher;
    }

    public enum Subchannel {
        UPDATE_CLAN_CHANNEL("UpdateClan", 1),
        UPDATE_CLANPLAYER_CHANNEL("UpdateClanPlayer", 1),
        DELETE_CLAN_CHANNEL("DeleteClan", 1),
        DELETE_CLANPLAYER_CHANNEL("DeleteClanPlayer", 1),
        CHAT_CHANNEL("Chat", 1);

        private final String displayName;
        private final Integer version;

        public String getDisplayName() {
            return displayName;
        }

        public Integer getVersion() {
            return version;
        }

        Subchannel(String displayName, Integer version) {
            this.displayName = displayName;
            this.version = version;
        }
    }

    public String formatted() {
        return String.format("SimpleClans|%s|%s|%s", subchannel.getDisplayName(), "v" + subchannel.getVersion(), publisher);
    }

    public boolean isMessageChannel(String input) {
        return CHANNEL_PATTERN.matcher(input).matches();
    }
}
