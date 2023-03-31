package de.spigotworkspace.votesystem.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import de.spigotworkspace.votesystem.VoteSystem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VoteListener implements Listener {
	VoteSystem voteSystem;
	public VoteListener(final VoteSystem voteSystem) {
		this.voteSystem = voteSystem;
	}

	@EventHandler
	public void onVote(VotifierEvent votifierEvent) {
		Vote vote = votifierEvent.getVote();
		Bukkit.broadcastMessage(String.format("§6§l%s §ehat gevotet. Danke! §4❤", vote.getUsername()));
	}
}
