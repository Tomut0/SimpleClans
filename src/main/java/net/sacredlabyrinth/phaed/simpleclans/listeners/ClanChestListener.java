package net.sacredlabyrinth.phaed.simpleclans.listeners;

import net.sacredlabyrinth.phaed.simpleclans.ClanChest;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;

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

        if (event.getInventory().getHolder() instanceof ClanChest cc) {
            cc.setLocked(false);
            plugin.getStorageManager().updateClan(clanPlayer.getClan(), true);
        }
    }
}
