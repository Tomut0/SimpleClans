package net.sacredlabyrinth.phaed.simpleclans.ui;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SCAnvilFrame extends SCFrame {

    protected AnvilGUI.Builder anvilGUI = new AnvilGUI.Builder().plugin(SimpleClans.getInstance());

    public SCAnvilFrame(@Nullable SCFrame parent, @NotNull Player viewer) {
        super(parent, viewer);
    }

    public abstract AnvilGUI.Builder getAnvilGUI();
}
