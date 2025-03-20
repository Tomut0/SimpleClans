package net.sacredlabyrinth.phaed.simpleclans.messaging;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import net.sacredlabyrinth.phaed.simpleclans.managers.SettingsManager;
import net.sacredlabyrinth.phaed.simpleclans.messaging.types.PluginMessage;
import net.sacredlabyrinth.phaed.simpleclans.proxy.ProxyManager;
import org.bukkit.Bukkit;

import static net.sacredlabyrinth.phaed.simpleclans.messaging.MessageChannel.Subchannel.DELETE_CLANPLAYER_CHANNEL;

public class PluginMessageBroker extends BaseMessageBroker<byte[], PluginMessage> {

    private final ProxyManager proxy;

    public PluginMessageBroker(ClanManager cm, SettingsManager settings, ProxyManager proxy) {
        super(cm, settings);
        this.proxy = proxy;


        // let's imagine `this` is `broker` instance
        PluginMessage pluginMessage = new PluginMessage(DELETE_CLANPLAYER_CHANNEL, this.getPublisher());

        this.send(pluginMessage.asPlayerMessage(playerName, message));
        this.send(pluginMessage.asPlayerList());
        this.send(pluginMessage.asForward(this.getGson().toJson(object)));
    }

    @Override
    protected boolean canSend() {
        return settings.is(SettingsManager.ConfigField.PLUGIN_MESSAGING_ENABLE) && proxy.isChannelRegistered();
    }

    @Override
    public void send(PluginMessage message) {
        if (canSend()) {
            Bukkit.getOnlinePlayers().stream().findAny().ifPresent(player ->
                    player.sendPluginMessage(SimpleClans.getInstance(), "BungeeCord", message.getContent()));
        }
    }

    @Override
    public String getPublisher() {
        return proxy.getServerName();
    }
}
