package net.sacredlabyrinth.phaed.simpleclans.proxy;

public enum MessageChannel {

    UPDATE_CLAN_CHANNEL("UpdateClan"),
    UPDATE_CLANPLAYER_CHANNEL("UpdateClanPlayer"),
    DELETE_CLAN_CHANNEL("DeleteClan"),
    DELETE_CLANPLAYER_CHANNEL("DeleteClanPlayer"),
    CHAT_CHANNEL("Chat");

    public String getDisplayName() {
        return displayName;
    }

    private final String displayName;

    MessageChannel(String displayName) {
        this.displayName = displayName;
    }
}
