package net.sacredlabyrinth.phaed.simpleclans.events;

import net.sacredlabyrinth.phaed.simpleclans.ui.IFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author RoinujNosde
 */
public class FrameOpenEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;
    private final @NotNull IFrame frame;

    public FrameOpenEvent(@NotNull Player viewer, @NotNull IFrame frame) {
        super(viewer);
        this.frame = frame;
    }

    public @NotNull IFrame getFrame() {
        return frame;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
