package de.spigotworkspace.votesystem.commands;

import de.spigotworkspace.votesystem.VoteSystem;
import de.spigotworkspace.votesystem.helper.SerializationHelper;
import de.spigotworkspace.votesystem.objects.VotePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mariadb.jdbc.MariaDbDataSource;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.sql.ResultSet;
import java.util.UUID;

public class VoteDataCommand implements CommandExecutor {

    VoteSystem voteSystem;

    public VoteDataCommand(VoteSystem voteSystem) {
        this.voteSystem = voteSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
        } else {

        }
        return false;
    }
}
