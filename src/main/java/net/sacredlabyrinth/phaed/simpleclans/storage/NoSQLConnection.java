package net.sacredlabyrinth.phaed.simpleclans.storage;

import net.sacredlabyrinth.phaed.simpleclans.managers.SettingsManager;

public abstract class NoSQLConnection implements SCConnection, Authenticatable {

    protected final SettingsManager settings;

    public NoSQLConnection(SettingsManager settings) {
        this.settings = settings;
    }
}
