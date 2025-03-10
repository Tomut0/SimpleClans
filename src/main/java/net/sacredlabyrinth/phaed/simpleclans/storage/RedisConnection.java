package net.sacredlabyrinth.phaed.simpleclans.storage;

import net.sacredlabyrinth.phaed.simpleclans.managers.SettingsManager;
import redis.clients.jedis.JedisPool;

public class RedisConnection extends NoSQLConnection {

    private JedisPool pool;

    public RedisConnection(SettingsManager settings) {
        super(settings);
    }

    @Override
    public String getUsername() {
        return settings.getString(SettingsManager.ConfigField.REDIS_USERNAME);
    }

    @Override
    public String getPassword() {
        return settings.getString(SettingsManager.ConfigField.REDIS_PASSWORD);
    }

    @Override
    public String getHost() {
        return settings.getString(SettingsManager.ConfigField.REDIS_HOST);
    }

    @Override
    public int getPort() {
        return settings.getInt(SettingsManager.ConfigField.REDIS_PORT);
    }

    public JedisPool getPool() {
        if (pool == null) {
            pool = new JedisPool(getHost(), getPort(), getUsername(), getPassword());
        }

        return pool;
    }
}
