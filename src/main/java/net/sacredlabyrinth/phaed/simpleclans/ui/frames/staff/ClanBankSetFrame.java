package net.sacredlabyrinth.phaed.simpleclans.ui.frames.staff;

import com.cryptomorin.xseries.XMaterial;
import net.sacredlabyrinth.phaed.simpleclans.ChatBlock;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.EconomyResponse;
import net.sacredlabyrinth.phaed.simpleclans.events.ClanBalanceUpdateEvent;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCAnvilFrame;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCComponentImpl;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCFrame;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.sacredlabyrinth.phaed.simpleclans.SimpleClans.lang;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.RED;

public class ClanBankSetFrame extends SCAnvilFrame {

    private final Clan clan;

    public ClanBankSetFrame(@Nullable SCFrame parent, @NotNull Player viewer, @NotNull Clan clan) {
        super(parent, viewer);
        this.clan = clan;
    }

    @Override
    public @NotNull String getTitle() {
        return lang("clan.admin.balance", getViewer(), clan.getName(), clan.getBalance());
    }

    @Override
    public AnvilGUI.Builder getAnvilGUI() {
        anvilGUI.title(getTitle()).
                itemLeft(new SCComponentImpl(String.valueOf(clan.getBalance()), null, XMaterial.PAPER, AnvilGUI.Slot.INPUT_LEFT).getItem()).
                onComplete((player, amount) -> {
                    try {
                        double parsedAmount = Double.parseDouble(amount);
                        EconomyResponse response = clan.setBalance(getViewer(), ClanBalanceUpdateEvent.Cause.COMMAND, parsedAmount);

                        if (response == EconomyResponse.SUCCESS) {
                            ChatBlock.sendMessage(player, AQUA + lang("clan.admin.set", getViewer(), clan.getTag(), amount));
                            getViewer().closeInventory();
                        }
                    } catch (NumberFormatException ex) {
                        ChatBlock.sendMessage(player, RED + lang("amount.should.be.number", getViewer()));
                    }
                    return AnvilGUI.Response.close();
                });
        return anvilGUI;
    }
}
