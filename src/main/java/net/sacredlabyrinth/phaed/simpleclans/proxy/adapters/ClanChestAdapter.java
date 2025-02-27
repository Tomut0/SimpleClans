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

        out.name("using-server").value(clanChest.getServerUsing());

        out.endObject();
    }

    @Override
    public ClanChest read(JsonReader in) throws IOException {
        Gson gson = SimpleClans.getInstance().getProxyManager().getGson();
        in.beginObject();

        ItemStack[] items = null;
        String locked = null;

        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "content":
                    String json = in.nextString();
                    items = gson.fromJson(json, ItemStack[].class);
                    break;
                case "using-server":
                    locked = in.nextString();
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
        }

        if (locked != null) {
            clanChest.useServer(locked);
        } else {
            clanChest.releaseServer();
        }

        return clanChest;
    }
}
