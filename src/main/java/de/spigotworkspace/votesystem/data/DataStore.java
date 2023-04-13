package de.spigotworkspace.votesystem.data;

import de.spigotworkspace.votesystem.VoteSystem;
import de.spigotworkspace.votesystem.helper.SchedulerHelper;
import de.spigotworkspace.votesystem.objects.VotePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
            VotePlayer votePlayer = null;
            boolean found = false;
            if (votePlayers.containsKey(uuid)) {
                votePlayer = votePlayers.get(uuid);
                found = true;
            }

            if (!found) {
                if ((votePlayer = voteSystem.getDataSource().getVotePlayer(uuid)) == null) {
                    votePlayer = new VotePlayer(uuid);
                }
            }

            LocalDate lastVote = Instant.ofEpochMilli(votePlayer.getLastVote()).atZone(ZoneId.of("Europe/Berlin")).toLocalDate();
            LocalDate localDateNow = Instant.ofEpochMilli(Instant.now().toEpochMilli()).atZone(ZoneId.of("Europe/Berlin")).toLocalDate();
            if (localDateNow.isAfter(lastVote)) {
                votePlayer.setVotesToday(0);
            }

            votePlayers.putIfAbsent(uuid, votePlayer);
            return votePlayer;
        };

        SchedulerHelper.runTaskAsynchronously(voteSystem, runAsync, callback);
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
