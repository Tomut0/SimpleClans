package net.sacredlabyrinth.phaed.simpleclans.messaging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sacredlabyrinth.phaed.simpleclans.chat.SCMessage;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import net.sacredlabyrinth.phaed.simpleclans.managers.SettingsManager;
import net.sacredlabyrinth.phaed.simpleclans.messaging.types.Message;
import net.sacredlabyrinth.phaed.simpleclans.proxy.adapters.ClanPlayerListAdapter;
import net.sacredlabyrinth.phaed.simpleclans.proxy.adapters.ClanPlayerTypeAdapterFactory;
import net.sacredlabyrinth.phaed.simpleclans.proxy.adapters.ConfigurationSerializableAdapter;
import net.sacredlabyrinth.phaed.simpleclans.proxy.adapters.SCMessageAdapter;

import java.util.List;

import static net.sacredlabyrinth.phaed.simpleclans.managers.SettingsManager.ConfigField.MESSAGE_BROKER_PUBLISHER_NAME;
import static net.sacredlabyrinth.phaed.simpleclans.managers.SettingsManager.ConfigField.MESSAGE_BROKER_SUBSCRIBERS;

public abstract class BaseMessageBroker<T, R extends Message<T>> implements MessageBroker<T, R> {

    protected final SettingsManager settings;
    protected final Gson gson;

    public BaseMessageBroker(ClanManager clanManager, SettingsManager settings) {
        this.settings = settings;
        gson = new GsonBuilder().registerTypeAdapterFactory(new ClanPlayerTypeAdapterFactory(clanManager))
                .registerTypeAdapterFactory(new ConfigurationSerializableAdapter())
                .registerTypeAdapter(ClanPlayerListAdapter.getType(), new ClanPlayerListAdapter(clanManager))
                .registerTypeAdapter(SCMessage.class, new SCMessageAdapter(clanManager)).setExclusionStrategies()
                .create();
    }

    @Override
    public String getPublisher() {
        return settings.getString(MESSAGE_BROKER_PUBLISHER_NAME);
    }

    @Override
    public List<String> getSubscribers() {
        return settings.getStringList(MESSAGE_BROKER_SUBSCRIBERS);
    }

    public Gson getGson() {
        return gson;
    }

    protected abstract boolean canSend();
}
