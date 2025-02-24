package net.sacredlabyrinth.phaed.simpleclans.proxy;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.chat.SCMessage;
import net.sacredlabyrinth.phaed.simpleclans.managers.SettingsManager.ConfigField;
import net.sacredlabyrinth.phaed.simpleclans.proxy.adapters.ClanPlayerListAdapter;
import net.sacredlabyrinth.phaed.simpleclans.proxy.adapters.ClanPlayerTypeAdapterFactory;
import net.sacredlabyrinth.phaed.simpleclans.proxy.adapters.ConfigurationSerializableAdapter;
import net.sacredlabyrinth.phaed.simpleclans.proxy.adapters.SCMessageAdapter;
import net.sacredlabyrinth.phaed.simpleclans.proxy.listeners.MessageListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;

public final class BungeeManager implements ProxyManager, PluginMessageListener {

    private final SimpleClans plugin;
    private final Gson gson;
    private final List<String> onlinePlayers = new ArrayList<>();
    private String serverName = "";

    public BungeeManager(SimpleClans plugin) {
        this.plugin = plugin;
        gson = new GsonBuilder().registerTypeAdapterFactory(new ClanPlayerTypeAdapterFactory(plugin))
                .registerTypeAdapterFactory(new ConfigurationSerializableAdapter())
                .registerTypeAdapter(ClanPlayerListAdapter.getType(), new ClanPlayerListAdapter(plugin))
                .registerTypeAdapter(SCMessage.class, new SCMessageAdapter(plugin)).setExclusionStrategies()
                .create();
        if (!plugin.getSettingsManager().is(ConfigField.PERFORMANCE_USE_BUNGEECORD)) {
            return;
        }
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
        Bukkit.getScheduler().runTaskTimer(plugin, this::requestPlayerList, 0, 60 * 20);
        requestServerName();
    }

    @SuppressWarnings("UnstableApiUsage")
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] data) {
        ByteArrayDataInput input = ByteStreams.newDataInput(data);
        String subChannel = input.readUTF();

        SimpleClans.debug("Message received, sub-channel: " + subChannel);
        try {
            Subchannel subchannel = Subchannel.valueOf(subChannel);
            Class<?> clazz = Class.forName("net.sacredlabyrinth.phaed.simpleclans.proxy.listeners." + subchannel.getClassName());
            MessageListener listener = (MessageListener) clazz.getConstructor(BungeeManager.class).newInstance(this);
            if (subchannel.isBungeeChannel()) {
                listener.accept(input);
                SimpleClans.debug("Message processed");
                return;
            }

            String receivedServerName = input.readUTF();
            if (!isServerAllowed(receivedServerName)) {
                SimpleClans.debug(String.format("Server not allowed: %s", serverName));
                return;
            }

            listener.accept(input);
            SimpleClans.debug("Message processed");
        } catch (IllegalArgumentException ignored) {
        } catch (IllegalStateException ex) {
            plugin.getLogger().log(Level.WARNING,"Received server name is empty", ex);
        } catch(ClassNotFoundException e) {
            SimpleClans.debug(String.format("Unknown channel: %s", subChannel));
        } catch (ReflectiveOperationException ex) {
            plugin.getLogger().log(Level.SEVERE, String.format("Error processing channel %s", subChannel), ex);
        }
    }

    private boolean isServerAllowed(@NotNull String serverName) {
        List<String> servers = plugin.getSettingsManager().getStringList(ConfigField.BUNGEE_SERVERS);
        if (servers.isEmpty()) {
            return true;
        }

        return servers.contains(serverName);
    }

    @Override
    public boolean isOnline(String playerName) {
        if (Bukkit.getOnlinePlayers().stream().anyMatch(player -> player.getName().equals(playerName))) {
            return true;
        }
        return onlinePlayers.contains(playerName);
    }

    public void setOnlinePlayers(@NotNull List<String> onlinePlayers) {
        this.onlinePlayers.clear();
        this.onlinePlayers.addAll(onlinePlayers);
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    public void setServerName(@NotNull String name) {
        this.serverName = name;
    }

    @Override
    public void sendMessage(SCMessage message) {
        forwardPluginMessage(Subchannel.CHAT_CHANNEL, gson.toJson(message), false);
    }

    @Override
    public void sendMessage(@NotNull String target, @NotNull String message) {
        if (message.isEmpty()) {
            return;
        }

        if ("ALL".equals(target)) {
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(message));
            sendBroadcast(message);
            return;
        }

        Player player = Bukkit.getPlayerExact(target);
        if (player != null) {
            player.sendMessage(message);
            return;
        }

        sendPrivateMessage(target, message);
    }

    private void sendBroadcast(@NotNull String message) {
        forwardPluginMessage(Subchannel.BROADCAST, message, false);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void sendPrivateMessage(@NotNull String playerName, @NotNull String message) {
        if (!isChannelRegistered()) {
            return;
        }
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Forward");
        output.writeUTF("ONLINE");
        output.writeUTF(Subchannel.MESSAGE.name());
        output.writeUTF(serverName);
        output.writeUTF(playerName);
        output.writeUTF(message);

        sendOnBungeeChannel(output);
    }

    @Override
    public void sendDelete(Clan clan) {
        forwardPluginMessage(Subchannel.DELETE_CLAN_CHANNEL, clan.getTag());
    }

    @Override
    public void sendDelete(ClanPlayer cp) {
        forwardPluginMessage(Subchannel.DELETE_CLANPLAYER_CHANNEL, cp.getUniqueId().toString());
    }

    @Override
    public void sendUpdate(Clan clan) {
        forwardPluginMessage(Subchannel.UPDATE_CLAN_CHANNEL, gson.toJson(clan));
    }

    @Override
    public void sendUpdate(ClanPlayer cp) {
        forwardPluginMessage(Subchannel.UPDATE_CLANPLAYER_CHANNEL, gson.toJson(cp));
    }

    public SimpleClans getPlugin() {
        return plugin;
    }

    public Gson getGson() {
        return gson;
    }

    @SuppressWarnings("UnstableApiUsage")
    private void requestPlayerList() {
        if (!isChannelRegistered()) {
            return;
        }
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("PlayerList");
        output.writeUTF("ALL");

        sendOnBungeeChannel(output);
    }

    private void requestServerName() {
        Listener listener = new Listener() {
            @SuppressWarnings("UnstableApiUsage")
            @EventHandler
            void on(PlayerJoinEvent event) {
                ByteArrayDataOutput output = ByteStreams.newDataOutput();
                output.writeUTF("GetServer");

                Bukkit.getScheduler().runTask(plugin, () -> {
                    event.getPlayer().sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
                    PlayerJoinEvent.getHandlerList().unregister(this); //only needed once
                });
            }
        };
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    private void forwardPluginMessage(final Subchannel subChannel, final String message) {
        forwardPluginMessage(subChannel, message, true);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void forwardPluginMessage(final Subchannel subChannel, final String message, final boolean all) {
        SimpleClans.debug(String.format("Forwarding message, channel %s, message %s, all %s", subChannel, message, all));
        if (!isChannelRegistered()) {
            SimpleClans.debug("Not registered");
            return;
        }
        String target = all ? "ALL" : "ONLINE";
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Forward");
        output.writeUTF(target);
        output.writeUTF(subChannel.name());
        output.writeUTF(serverName);
        output.writeUTF(message);

        sendOnBungeeChannel(output);
    }

    private void sendOnBungeeChannel(ByteArrayDataOutput output) {
        Bukkit.getOnlinePlayers().stream().findAny().ifPresent(player ->
                player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray()));
    }

    private boolean isChannelRegistered() {
        return Bukkit.getMessenger().isOutgoingChannelRegistered(plugin, "BungeeCord");
    }

}
