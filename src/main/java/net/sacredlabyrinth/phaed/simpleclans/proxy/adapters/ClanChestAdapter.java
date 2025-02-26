package net.sacredlabyrinth.phaed.simpleclans.proxy.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.sacredlabyrinth.phaed.simpleclans.ClanChest;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class ClanChestAdapter extends TypeAdapter<ClanChest> {

    @Override
    public void write(JsonWriter out, ClanChest clanChest) throws IOException {
        Gson gson = SimpleClans.getInstance().getProxyManager().getGson();
        out.beginObject();

        out.name("content");
        ItemStack[] contents = clanChest.getInventory().getContents();
        out.value(gson.toJson(contents, ItemStack[].class));

        out.name("locked").value(clanChest.isLocked());

        out.endObject();
    }

    @Override
    public ClanChest read(JsonReader in) throws IOException {
        Gson gson = SimpleClans.getInstance().getProxyManager().getGson();
        in.beginObject();

        ItemStack[] items = null;
        boolean locked = false;

        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "content":
                    String json = in.nextString();
                    items = gson.fromJson(json, ItemStack[].class);
                    break;

                case "locked":
                    locked = in.nextBoolean();
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();

        ClanChest clanChest = new ClanChest();
        if (items != null) {
            clanChest.getInventory().setContents(items);
            clanChest.sync();
        }
        clanChest.setLocked(locked);

        return clanChest;
    }
}
