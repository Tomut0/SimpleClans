package net.sacredlabyrinth.phaed.simpleclans;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static net.sacredlabyrinth.phaed.simpleclans.SimpleClans.lang;

public class ClanChest implements Serializable, InventoryHolder {

    @Serial
    private static final long serialVersionUID = 1L;

    private transient @Nullable Inventory chest;
    private transient boolean locked = false;

    private final @NotNull HashMap<Integer, Map<String, Object>> content;


    public ClanChest() {
        content = new HashMap<>();
    }

    public void loadContent() {
        if (!content.isEmpty()) {
            content.forEach((key, value) -> getInventory().setItem(key, ItemStack.deserialize(value)));
        }
    }

    public void sync() {
        ItemStack[] contents = getInventory().getContents();

        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                content.put(i, contents[i].serialize());
            } else {
                content.remove(i);
            }
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        if (chest == null) {
            chest = Bukkit.createInventory(this, 27, lang("clan.chest.title"));
        }

        return chest;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public static ClanChest deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);

        return (ClanChest) ois.readObject();
    }

    public static byte[] serialize(@NotNull ClanChest clanChest) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(clanChest);
        oos.close();

        return baos.toByteArray();
    }
}
