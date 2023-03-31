package de.spigotworkspace.votesystem.objects;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;

public class VotePlayer implements Serializable {
	private static final long serialVersionUID = 1L;
	private UUID uuid;

	private int votes;
	private int streak;
	private int points;

	public Player getPlayer(){
		return getOfflinePlayer().getPlayer();
	}

	public OfflinePlayer getOfflinePlayer(){
		return Bukkit.getOfflinePlayer(getUUID());
	}

	public UUID getUUID() {
		return uuid;
	}

	public VotePlayer setUUID(UUID uuid) {
		this.uuid = uuid;
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
}
