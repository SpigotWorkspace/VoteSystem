package de.spigotworkspace.votesystem.listeners;

import de.spigotworkspace.votesystem.VoteSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
    private VoteSystem voteSystem;

    public InventoryListener(final VoteSystem voteSystem) {
        this.voteSystem = voteSystem;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("VoteData -")) {
            event.setCancelled(true);
        }
    }
}
