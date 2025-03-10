package net.sacredlabyrinth.phaed.simpleclans.proxy;

import redis.clients.jedis.JedisPubSub;

public class RedisListener extends JedisPubSub {
    public void onMessage(String channel, String message) {

    }

    public void onSubscribe(String channel, int subscribedChannels) {

    }

    public void onUnsubscribe(String channel, int subscribedChannels) {

    }

    public void onPSubscribe(String pattern, int subscribedChannels) {

    }

    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    public void onPMessage(String pattern, String channel, String message) {

    }
}
