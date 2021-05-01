package net.sacredlabyrinth.phaed.simpleclans.ui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface, which connects the whole frames
 */
public interface IFrame {
    @NotNull
    Player getViewer();

    @Nullable
    IFrame getParent();
}
