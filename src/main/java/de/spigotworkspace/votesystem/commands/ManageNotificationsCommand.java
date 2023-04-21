package de.spigotworkspace.votesystem.commands;

import de.spigotworkspace.votesystem.VotePlugin;
import de.spigotworkspace.votesystem.helper.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ManageNotificationsCommand implements CommandExecutor {
    private VotePlugin votePlugin;

    public ManageNotificationsCommand(VotePlugin votePlugin) {
        this.votePlugin = votePlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("voteystem.managenotifications")) {
                player.sendMessage("§cDu hast dafür keine Berechtigung");
                return false;
            }
            votePlugin.getDataStore().get(player.getUniqueId(), votePlayer -> {
                Inventory inventory = Bukkit.createInventory(null, 9, "§b§lBenachrichtigungen");

                for (int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, 7).setDisplayName("§8").build());
                }

                inventory.setItem(3, new ItemBuilder(Material.BOOK_AND_QUILL).setDisplayName("§9Broadcasts")
                        .setLore("§7Wähle, ob du eine Benachrichtigung erhalten möchtest, ",
                                 "§7wenn ein Spieler gevotet hat.",
                                 "§7Aktuell: "+ (votePlayer.isBroadcastActivated() ? "§aaktiv" : "§cinaktiv"))
                        .build());
                inventory.setItem(5, new ItemBuilder(Material.BOOK_AND_QUILL).setDisplayName("§9Vote-Reminder")
                        .setLore("§7Wähle, ob du eine Benachrichtigung erhalten möchtest, ",
                                 "§7wenn du am aktuellen Tag noch nicht gevotet hast.",
                                 "§7Aktuell: "+ (votePlayer.isReminderActivated() ? "§aaktiv" : "§cinaktiv"))
                        .build());

                player.openInventory(inventory);
            });
        }

        return false;
    }
}
