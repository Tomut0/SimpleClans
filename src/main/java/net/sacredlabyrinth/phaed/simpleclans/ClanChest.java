package net.sacredlabyrinth.phaed.simpleclans;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static net.sacredlabyrinth.phaed.simpleclans.SimpleClans.lang;

public class ClanChest implements Serializable, InventoryHolder {

    @Serial
    private static final long serialVersionUID = 1L;

    private transient @Nullable Inventory chest;

    private transient String lockedServer;

    @Override
    public @NotNull Inventory getInventory() {
        if (chest == null) {
            chest = Bukkit.createInventory(this, 27, lang("clan.chest.title"));
        }

        return chest;
    }

    public String getLockedServer() {
        return lockedServer;
    }

    public boolean isLockedServer() {
        return lockedServer == null || lockedServer.isEmpty() ||
                Objects.equals(lockedServer, SimpleClans.getInstance().getProxyManager().getServerName());
    }

    public void setLockedServer(@Nullable String lockedServer) {
        this.lockedServer = lockedServer;
    }

    public static class Serializer  {

        @SuppressWarnings("unchecked")
        public static ClanChest deserialize(ClanChest cc, byte[] data) throws IOException, ClassNotFoundException {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);

            List<Map<String, Object>> serializedContents = (List<Map<String, Object>>) ois.readObject();
            ois.close();

            ItemStack[] contents = serializedContents.stream()
                    .map(map -> map != null ? ItemStack.deserialize(map) : null) // Convert Map -> ItemStack
                    .toArray(ItemStack[]::new);

            cc.getInventory().setContents(contents);
            return cc;
        }

        public static byte[] serialize(@NotNull ClanChest clanChest) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            ItemStack[] contents = clanChest.getInventory().getContents();
            List<Map<String, Object>> serializedContents = Arrays.stream(contents)
                    .map(item -> item != null ? item.serialize() : null)
                    .toList();

            oos.writeObject(serializedContents);
            oos.close();

            return baos.toByteArray();
        }
    }
}
