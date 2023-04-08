package de.spigotworkspace.votesystem.commands;

import de.spigotworkspace.votesystem.VoteSystem;
import de.spigotworkspace.votesystem.helper.ItemBuilder;
import de.spigotworkspace.votesystem.helper.ProfileFetcher;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class VoteDataCommand implements CommandExecutor {
    private VoteSystem voteSystem;

    public VoteDataCommand(VoteSystem voteSystem) {
        this.voteSystem = voteSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("voteystem.votedata")) {
                player.sendMessage("§cDu hast dafür keine Berechtigung");
                return false;
            }

            if (args.length != 1) {
                command.setUsage("§cUsage: /votedata <Name/UUID>");
                return false;
            }

            String value = args[0];
            ProfileFetcher.getFromNameOrUniqueId(value, ((jsonObject, success) -> {
                if (success) {
                    voteSystem.getDataStore().get((UUID) jsonObject.get("id"), votePlayer -> {
                        Inventory inventory = Bukkit.createInventory(null, 9, "VoteData - " + votePlayer.getName());
                        for (int i = 0; i < inventory.getSize(); i++) {
                            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, 7).setDisplayName("§8").build());
                        }
                        inventory.setItem(4, new ItemBuilder(Material.PAPER).setDisplayName("§c"+votePlayer.getName())
                                .setLore("Votes: "+ votePlayer.getVotes(),
                                        "Points: " + votePlayer.getPoints(),
                                        "Streak: " + votePlayer.getStreak())
                                .build());

                        player.openInventory(inventory);
                    });
                }
            }));
        }
        return false;
    }
}
