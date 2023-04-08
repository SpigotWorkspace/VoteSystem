package de.spigotworkspace.votesystem.objects;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.UUID;

public class VotePlayer implements Serializable {
	private static final long serialVersionUID = 1L;
	private UUID uniqueId;
	private int votes;
	private int streak;
	private int points;
	private boolean changed;

	public VotePlayer(UUID uniqueId) {
		this.uniqueId = uniqueId;
		this.votes = 0;
		this.streak = 0;
		this.points = 0;
		this.changed = false;
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getUniqueId());
	}

	public String getName() {
		return getOfflinePlayer().getName();
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

	public boolean hasChanged() {
		return changed;
	}

	public VotePlayer setChanged(boolean changed) {
		this.changed = changed;
		return this;
	}

	public void addVote() {
		setVotes(getVotes() + 1);
	}
}
