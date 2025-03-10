package net.sacredlabyrinth.phaed.simpleclans.proxy;

import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import net.sacredlabyrinth.phaed.simpleclans.managers.SettingsManager;

public class PluginMessageBroker extends BaseMessageBroker {

    public PluginMessageBroker(ClanManager clanManager, SettingsManager settings) {
        super(clanManager, settings);
    }

    @Override
    public <T> void send(MessageChannel channel, T message) {

    }
}
