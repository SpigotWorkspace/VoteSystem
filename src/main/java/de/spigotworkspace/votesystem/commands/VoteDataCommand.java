package de.spigotworkspace.votesystem.commands;

import de.spigotworkspace.votesystem.VotePlugin;
import de.spigotworkspace.votesystem.helper.ItemBuilder;
import de.spigotworkspace.votesystem.helper.ProfileFetcher;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class VoteDataCommand implements CommandExecutor {
    private VotePlugin votePlugin;

    public VoteDataCommand(VotePlugin votePlugin) {
        this.votePlugin = votePlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (args.length) {
                case 0:
                    openDataInventory(player, player.getUniqueId());
                    break;
                case 1:
                    if (!player.hasPermission("voteystem.votedata")) {
                        player.sendMessage("§cDu hast dafür keine Berechtigung");
                        return false;
                    }
                    String value = args[0];
                    ProfileFetcher.getFromNameOrUniqueId(value, ((jsonObject, success) -> {
                        if (success) {
                            openDataInventory(player, (UUID) jsonObject.get("id"));
                        }
                    }));
                    break;
                default:
                    if (player.hasPermission("voteystem.votedata")) {
                        player.sendMessage("§cUsage: /votedata <Name/UUID>");
                        return false;
                    }
                    break;
            }
        }
        return false;
    }

    private void openDataInventory(Player player, UUID uuid) {
        votePlugin.getDataStore().get(uuid, votePlayer -> {
            Inventory inventory = Bukkit.createInventory(null, 9, "§b§lVote-Daten");
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, 7).setDisplayName("§8").build());
            }
            ArrayList<String> lore = new ArrayList<>();
            lore.add("Votes: "+ votePlayer.getVotes());
            lore.add("Points: " + votePlayer.getPoints());
            lore.add("Streak: " + votePlayer.getStreak());
            lore.add("Letzter Vote: N/A");

            if (votePlayer.getLastVote() != 0) {
                LocalDate localDate = Instant.ofEpochMilli(votePlayer.getLastVote()).atZone(ZoneId.of("Europe/Berlin")).toLocalDate();
                String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                lore.set(3, "Letzter Vote: " + formattedDate);
            }

            inventory.setItem(4, new ItemBuilder(Material.PAPER).setDisplayName("§c"+votePlayer.getName()).setLore(lore).build());

            player.openInventory(inventory);
        });
    }
}
