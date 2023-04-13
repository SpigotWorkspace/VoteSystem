package de.spigotworkspace.votesystem.objects;


import de.spigotworkspace.votesystem.helper.ProfileFetcher;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class VotePlayer implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient UUID uniqueId;
	private int votes;
	private int streak;
	private int points;
	private int votesToday;
	private long lastVote;
	private boolean dirty;
	private boolean rewardEarned;
	private boolean broadcastActivated;
	private boolean reminderActivated;

	public VotePlayer(UUID uniqueId) {
		this.uniqueId = uniqueId;
		this.broadcastActivated = true;
		this.reminderActivated = true;
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getUniqueId());
	}

	public String getName() {
		AtomicReference<String> name = new AtomicReference<>(getOfflinePlayer().getName());
		if (name.get() == null) {
			ProfileFetcher.getFromNameOrUniqueId(getUniqueId().toString(), (jsonObject, success) -> {
				if (success) {
					name.set(jsonObject.getString("name"));
				}
			});
		}
		return name.get();
	}

	public UUID getUniqueId() {
		return uniqueId;
	}

	public VotePlayer setUniqueId(UUID uniqueId) {
		this.uniqueId = uniqueId;
		return this;
	}

	public int getVotes() {
		return votes;
	}

	public VotePlayer setVotes(int votes) {
		this.votes = votes;
		return this;
	}

	public int getStreak() {
		return streak;
	}

	public VotePlayer setStreak(int streak) {
		this.streak = streak;
		return this;
	}

	public int getPoints() {
		return points;
	}

	public VotePlayer setPoints(int points) {
		this.points = points;
		return this;
	}

	public void addPoints(int points) {
		setPoints(getPoints() + points);
	}

	public int getVotesToday() {
		return votesToday;
	}

	public VotePlayer setVotesToday(int votesToday) {
		this.votesToday = votesToday;
		return this;
	}

	public long getLastVote() {
		return lastVote;
	}

	public VotePlayer setLastVote(long lastVote) {
		this.lastVote = lastVote;
		return this;
	}

	public boolean isDirty() {
		return dirty;
	}

	public VotePlayer setDirty(boolean dirty) {
		this.dirty = dirty;
		return this;
	}

	public boolean isRewardEarned() {
		return rewardEarned;
	}

	public VotePlayer setRewardEarned(boolean rewardEarned) {
		this.rewardEarned = rewardEarned;
		return this;
	}

	public boolean isBroadcastActivated() {
		return broadcastActivated;
	}

	public VotePlayer setBroadcastActivated(boolean broadcastActivated) {
		this.broadcastActivated = broadcastActivated;
		return this;
	}

	public boolean isReminderActivated() {
		return reminderActivated;
	}

	public VotePlayer setReminderActivated(boolean reminderActivated) {
		this.reminderActivated = reminderActivated;
		return this;
	}

	public void addVote() {
		setVotes(getVotes() + 1);
	}

	public void sendMessage(String message) {
		Player player = getOfflinePlayer().getPlayer();
		if (player == null) return;
		player.sendMessage(message);
	}

	public void sendMessage(BaseComponent... baseComponents) {
		Player player = getOfflinePlayer().getPlayer();
		if (player == null) return;
		player.sendMessage(baseComponents);
	}
}
