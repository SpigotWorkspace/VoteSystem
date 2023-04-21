package de.spigotworkspace.votesystem.listeners;

import de.spigotworkspace.votesystem.VotePlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryListener implements Listener {
    private VotePlugin votePlugin;

    public InventoryListener(final VotePlugin votePlugin) {
        this.votePlugin = votePlugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (event.getView().getTitle().equals("§b§lVote-Daten")) {
            event.setCancelled(true);
        } else if (event.getView().getTitle().equals("§b§lBenachrichtigungen")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (!clickedItem.getType().equals(Material.BOOK_AND_QUILL)) return;
            List<String> lore = clickedItem.getLore();
            Player player = (Player) event.getWhoClicked();
            votePlugin.getDataStore().get(player.getUniqueId(), votePlayer -> {
                if (clickedItem.getItemMeta().getDisplayName().equals("§9Broadcasts")) {
                    votePlayer.setBroadcastActivated(!votePlayer.isBroadcastActivated());
                    lore.set(2, "§7Aktuell: "+ (votePlayer.isBroadcastActivated() ? "§aaktiv" : "§cinaktiv"));
                } else if (clickedItem.getItemMeta().getDisplayName().equals("§9Vote-Reminder")) {
                    votePlayer.setReminderActivated(!votePlayer.isReminderActivated());
                    lore.set(2, "§7Aktuell: "+ (votePlayer.isReminderActivated() ? "§aaktiv" : "§cinaktiv"));
                }
                clickedItem.setLore(lore);
                player.updateInventory();
            });
        }
    }
}
