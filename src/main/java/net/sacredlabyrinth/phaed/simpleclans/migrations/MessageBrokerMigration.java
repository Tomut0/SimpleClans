package net.sacredlabyrinth.phaed.simpleclans.migrations;

import net.sacredlabyrinth.phaed.simpleclans.managers.SettingsManager;

public class MessageBrokerMigration extends ConfigMigration {
    public MessageBrokerMigration(SettingsManager settingsManager) {
        super(settingsManager);
    }

    @Override
    public void migrate() {
        String bungeePath = "performance.use-bungeecord";
        if (!settings.getConfig().contains(bungeePath)) {
            return;
        }

        settings.getConfig().set(bungeePath, null);
        settings.getConfig().set("message-broker", "plugin-messaging");
    }
}
