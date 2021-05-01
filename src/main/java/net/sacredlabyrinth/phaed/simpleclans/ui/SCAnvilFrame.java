package net.sacredlabyrinth.phaed.simpleclans.ui;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SCAnvilFrame implements IFrame {

    private final IFrame parent;
    private final Player viewer;

    protected AnvilGUI.Builder anvilGUI = new AnvilGUI.Builder().plugin(SimpleClans.getInstance());

    public SCAnvilFrame(@Nullable IFrame parent, @NotNull Player viewer) {
        this.parent = parent;
        this.viewer = viewer;
    }

    @Nullable
    @Override
    public IFrame getParent() {
        return parent;
    }

    @NotNull
    @Override
    public Player getViewer() {
        return viewer;
    }

    @Nullable
    public abstract String getTitle();

    public abstract AnvilGUI.Builder getAnvilGUI();
}
