package net.sacredlabyrinth.phaed.simpleclans.listeners;

import net.sacredlabyrinth.phaed.simpleclans.Helper;
import net.sacredlabyrinth.phaed.simpleclans.chest.ClanChest;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.concurrent.TimeUnit;

public class ClanChestListener extends SCListener{

    public ClanChestListener(SimpleClans plugin) {
        super(plugin);
    }

    @EventHandler
    public void close(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        ClanPlayer clanPlayer = plugin.getClanManager().getClanPlayer(player.getUniqueId());
        if (clanPlayer == null || clanPlayer.getClan() == null) {
            return;
        }

        var clan = clanPlayer.getClan();
        var storage = plugin.getStorageManager();

        if (event.getInventory().getHolder() instanceof ClanChest) {
            var throttleKey = clan.getName() + "_closing_chest";
            Helper.debounce(throttleKey, () -> {
                boolean success = storage.runWithTransaction(() -> storage.unlockChest(clan.getTag(), clanPlayer.getUniqueId()));
                if (success) {
                    storage.updateClan(clan, true);
                }
            }, 1L, TimeUnit.SECONDS);
        }
    }
}
