package de.spigotworkspace.votesystem.data;

import de.spigotworkspace.votesystem.VoteSystem;
import de.spigotworkspace.votesystem.helper.SchedulerHelper;
import de.spigotworkspace.votesystem.objects.VotePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DataStore {
    private HashMap<UUID, VotePlayer> votePlayers = new HashMap<>();

    private VoteSystem voteSystem;

    public DataStore(VoteSystem voteSystem) {
        this.voteSystem = voteSystem;
    }

    public void get(UUID uuid, Consumer<VotePlayer> callback) {
        Supplier<VotePlayer> runAsync = () -> {
            if (votePlayers.containsKey(uuid)) {
                return votePlayers.get(uuid);
            }

            VotePlayer votePlayer;
            if ((votePlayer = voteSystem.getDataSource().getVotePlayer(uuid)) == null) {
                votePlayer = new VotePlayer(uuid);
            }

            votePlayers.put(uuid, votePlayer);
            return votePlayer;
        };

        Consumer<VotePlayer> runSync = (votePlayer) -> {
            callback.accept(votePlayer);
        };

        SchedulerHelper.runTaskAsynchronously(voteSystem, runAsync, runSync);
    }

    public void saveVotePlayers() {
        voteSystem.getDataSource().saveVotePlayers(votePlayers);
    }

    public void startAutoSave() {
        int interval = voteSystem.getConfigProperties().getAutoSaveIntervalInMinutes();
        new BukkitRunnable() {
            @Override
            public void run() {
                saveVotePlayers();
            }
        }.runTaskTimerAsynchronously(voteSystem, 20L * 60 * interval, 20L * 60 * interval);
    }


}
