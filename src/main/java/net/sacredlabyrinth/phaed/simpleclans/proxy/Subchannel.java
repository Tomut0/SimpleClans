package net.sacredlabyrinth.phaed.simpleclans.proxy;

public enum Subchannel {
    UPDATE_CLAN_CHANNE("UpdateClan", false),
    UPDATE_CLANPLAYER_CHANNEL("UpdateClanPlayer", false),
    DELETE_CLAN_CHANNEL("DeleteClan", false),
    DELETE_CLANPLAYER_CHANNEL("DeleteClanPlayer", false),
    CHAT_CHANNEL("Chat", false),
    BROADCAST("Broadcast", false),
    MESSAGE("Message", false),
    GET_SERVER("GetServer", true),
    PLAYER_LIST("PlayerList", true);

    public String getClassName() {
        return className;
    }

    public boolean isBungeeChannel() {
        return isBungeeChannel;
    }

    private final String className;
    private final boolean isBungeeChannel;

    Subchannel(String displayName, boolean isBungeeChannel) {
        this.className = displayName;
        this.isBungeeChannel = isBungeeChannel;
    }
}
