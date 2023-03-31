package de.spigotworkspace.voteystem.listeners;

import com.vexsoftware.votifier.model.VotifierEvent;
import de.spigotworkspace.voteystem.VoteSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VoteListener implements Listener {
	VoteSystem voteSystem;
	public VoteListener(final VoteSystem voteSystem) {
		this.voteSystem = voteSystem;
	}

	@EventHandler
	public void onVote(VotifierEvent votifierEvent){
		
	}
}
