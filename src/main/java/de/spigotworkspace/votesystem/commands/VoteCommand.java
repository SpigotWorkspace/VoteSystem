package de.spigotworkspace.votesystem.commands;

import de.spigotworkspace.votesystem.VoteSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class VoteCommand implements CommandExecutor {
    private VoteSystem voteSystem;

    public VoteCommand(VoteSystem voteSystem) {
        this.voteSystem = voteSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("§6§lDu hast heute noch nicht gevotet!\n" +
                    "§6Hier kannst du für den Server voten:\n" +
                    "§d - "+ voteSystem.getConfigProperties().getVoteSites().stream().collect(Collectors.joining("\n - ")) +
                    "\n§6Kleiner Tipp: Es lohnt sich.");
        }

        return false;
    }
}
