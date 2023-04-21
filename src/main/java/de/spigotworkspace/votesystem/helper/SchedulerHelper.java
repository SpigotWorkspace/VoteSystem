package de.spigotworkspace.votesystem.helper;

import de.spigotworkspace.votesystem.VotePlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class SchedulerHelper {
    public static <T> void runTaskAsynchronously(VotePlugin votePlugin, Supplier<T> runAsync, Consumer<T> runSync) {
        new BukkitRunnable() {
            @Override
            public void run() {
                T object = runAsync.get();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        runSync.accept(object);
                    }
                }.runTask(votePlugin);
            }
        }.runTaskAsynchronously(votePlugin);
    }
}
