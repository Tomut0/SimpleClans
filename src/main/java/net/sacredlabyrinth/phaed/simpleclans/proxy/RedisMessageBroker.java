package net.sacredlabyrinth.phaed.simpleclans.proxy;

import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import net.sacredlabyrinth.phaed.simpleclans.managers.SettingsManager;
import net.sacredlabyrinth.phaed.simpleclans.storage.RedisConnection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;

public class RedisMessageBroker extends BaseMessageBroker {

    private final RedisConnection rs;

    public RedisMessageBroker(ClanManager clanManager, SettingsManager settings, RedisConnection rs, JedisPubSub listener) {
        super(clanManager, settings);
        this.rs = rs;

        try (Jedis jedis = rs.getPool().getResource()) {
            jedis.subscribe(listener, Arrays.toString(MessageChannel.values()));
        }
    }

    @Override
    public <T> void send(MessageChannel channel, T message) {
        try (Jedis jedis = rs.getPool().getResource()) {
            jedis.publish(channel.name(), gson.toJson(message));
        }
    }

    public RedisConnection getConnection() {
        return rs;
    }
}
