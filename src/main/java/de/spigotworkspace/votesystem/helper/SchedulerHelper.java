package de.spigotworkspace.votesystem.helper;

import de.spigotworkspace.votesystem.VoteSystem;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class SchedulerHelper {
    public static <T> void runTaskAsynchronously(VoteSystem voteSystem, Supplier<T> runAsync, Consumer<T> runSync) {
        new BukkitRunnable() {
            @Override
            public void run() {
                T object = runAsync.get();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        runSync.accept(object);
                    }
                }.runTask(voteSystem);
            }
        }.runTaskAsynchronously(voteSystem);
    }
}
