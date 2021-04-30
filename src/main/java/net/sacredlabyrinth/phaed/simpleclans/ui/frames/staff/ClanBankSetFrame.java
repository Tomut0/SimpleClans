package net.sacredlabyrinth.phaed.simpleclans.ui.frames.staff;

import com.cryptomorin.xseries.XMaterial;
import net.sacredlabyrinth.phaed.simpleclans.ChatBlock;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ui.InventoryController;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCAnvilFrame;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCComponentImpl;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCFrame;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClanBankSetFrame extends SCAnvilFrame {

    private final Clan clan;

    public ClanBankSetFrame(@Nullable SCFrame parent, @NotNull Player viewer, @NotNull Clan clan) {
        super(parent, viewer);
        this.clan = clan;
    }

    @Override
    public @NotNull String getTitle() {
        return clan.getName();
    }

    @Override
    public AnvilGUI.Builder getAnvilGUI() {
        anvilGUI.title(clan.getName()).
                itemLeft(new SCComponentImpl(String.valueOf(clan.getBalance()), null, XMaterial.PAPER, AnvilGUI.Slot.INPUT_LEFT).getItem()).
                onComplete((player, amount) -> {
                    try {
                        double amountDouble = Double.parseDouble(amount);
                        InventoryController.runSubcommand(player, "admin bank set", false, amount);
                        clan.setBalance(amountDouble);
                        ChatBlock.sendMessage(player, "You set the clan's amount: " + clan.getBalance());
                        getViewer().closeInventory();
                    } catch (NumberFormatException ex) {
                        ChatBlock.sendMessage(player, "Amount should be a number!");
                    }
                    return AnvilGUI.Response.close();
                });
        return anvilGUI;
    }
}
