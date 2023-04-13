package de.spigotworkspace.votesystem.commands;

import de.spigotworkspace.votesystem.VoteSystem;
import de.spigotworkspace.votesystem.helper.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GetRewardCommand implements CommandExecutor {
    private VoteSystem voteSystem;

    public GetRewardCommand(VoteSystem voteSystem) {
        this.voteSystem = voteSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            voteSystem.getDataStore().get(player.getUniqueId(), votePlayer -> {
                if (votePlayer.isRewardEarned()) {
                    Inventory inventory = player.getInventory();
                    if (inventory.firstEmpty() == -1) {
                        player.sendMessage("§cBitte mache zuerst einen Platz in deinem Inventar frei!\n" +
                                           "§cGebe dann den Befehl \"§e/getreward§c\" erneut ein.");
                    } else {
                        votePlayer.setRewardEarned(false);
                        inventory.addItem(new ItemBuilder(Material.STAINED_GLASS, 2).setDisplayName("§dDas wertvolle Glas").setLore("Belohnung für 100 Punkte durch das Voten", "Besitzer: §e"+ player.getName()).setLocalizedName("vote100").build());
                    }
                } else {
                    player.sendMessage("§cDu hast keine offene Belohnung.");
                }
            });
        }

        return false;
    }
}
