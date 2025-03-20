package net.sacredlabyrinth.phaed.simpleclans.proxy;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.chat.SCMessage;

public interface ProxyManager {

    String getServerName();

    boolean isChannelRegistered();

    boolean isOnline(String playerName);

    @Deprecated
    void sendMessage(SCMessage message);

    @Deprecated
    void sendMessage(String target, String message);

    @Deprecated
    void sendUpdate(Clan clan);

    @Deprecated
    void sendUpdate(ClanPlayer cp);

    @Deprecated
    void sendDelete(Clan clan);

    @Deprecated
    void sendDelete(ClanPlayer cp);
}
