package de.spigotworkspace.votesystem.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemMeta itemMeta = event.getItemInHand().getItemMeta();
        if (itemMeta.hasLocalizedName()) {
            if (itemMeta.getLocalizedName().equals("vote100")) {
                event.setCancelled(true);
            }
        }
    }
}
