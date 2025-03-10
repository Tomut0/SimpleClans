package net.sacredlabyrinth.phaed.simpleclans.proxy;

public enum BrokerType {
    REDIS("redis"),
    PLUGIN_MESSAGING("plugin-messaging");

    private final String displayName;

    BrokerType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static BrokerType fromName(String name) {
        return valueOf(name.toUpperCase().replace("-", "_"));
    }
}