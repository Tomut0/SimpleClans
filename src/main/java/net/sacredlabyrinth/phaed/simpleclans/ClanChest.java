package net.sacredlabyrinth.phaed.simpleclans;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ClanChest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private transient Clan clan;
    private transient Inventory chest;

    private final HashMap<Integer, Map<String, Object>> content;

    public ClanChest(@NotNull Clan clan) {
        this.clan = clan;
        content = new HashMap<>();
    }

    public void open(Player player) {
        player.openInventory(getInventory());
    }

    public void loadContent(@NotNull Clan clan) {
        this.clan = clan;
        chest = Bukkit.createInventory(null, 27, clan.getColorTag() + " Chest");
        content.forEach((key, value) -> {
            chest.setItem(key, ItemStack.deserialize(value));
        });
    }

    public static ClanChest deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);

        return (ClanChest) ois.readObject();
    }

    public Clan getClan() {
        return clan;
    }

    public static byte[] serialize(@NotNull ClanChest clanChest) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(clanChest);
        oos.close();

        return baos.toByteArray();
    }

    public void save() {
        ItemStack[] contents = chest.getContents();

        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                content.put(i, contents[i].serialize());
            } else {
                content.remove(i);
            }
        }
    }

    public Inventory getInventory() {
        if (chest == null) {
            chest = Bukkit.createInventory(null, 27, clan.getColorTag() + " chest");
            SimpleClans.getInstance().getStorageManager().insertClanChest(this);
            return chest;
        }

        return chest;
    }
}
