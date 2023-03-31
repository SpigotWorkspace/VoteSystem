package de.spigotworkspace.voteystem.objects;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;

public class VotePlayer implements Serializable {
	private UUID uuid;

	private int votes;
	private int streak;

	public Player getPlayer(){
		return getOfflinePlayer().getPlayer();
	}

	public OfflinePlayer getOfflinePlayer(){
		return Bukkit.getOfflinePlayer(getUUID());
	}

	public UUID getUUID() {
		return uuid;
	}

	public VotePlayer setUUID(final UUID uuid) {
		this.uuid = uuid;
		return this;
	}

	public int getStreak() {
		return streak;
	}

	public VotePlayer setStreak(final int streak) {
		this.streak = streak;
		return this;
	}

	public int getVotes() {
		return votes;
	}

	public VotePlayer setVotes(final int votes) {
		this.votes = votes;
		return this;
	}
}
