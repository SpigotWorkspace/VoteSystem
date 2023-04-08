package de.spigotworkspace.votesystem.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import de.spigotworkspace.votesystem.VoteSystem;
import de.spigotworkspace.votesystem.helper.ProfileFetcher;
import de.spigotworkspace.votesystem.objects.VotePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;
import java.util.UUID;

public class VoteListener implements Listener {
	private VoteSystem voteSystem;

	public VoteListener(final VoteSystem voteSystem) {
		this.voteSystem = voteSystem;
	}

	@EventHandler
	public void onVote(VotifierEvent event) {
		Vote vote = event.getVote();
		Optional<UUID> optional = ProfileFetcher.getUniqueIdFromName(vote.getUsername());
		if (optional.isPresent()) {
			voteSystem.getDataStore().get(optional.get(), (votePlayer -> {
				if (votePlayer == null) return;
				Bukkit.broadcastMessage(String.format("§6§l%s §ehat gevotet. Danke! §4❤", votePlayer.getName()));
				handleRewards(votePlayer);
			}));
		}

	}

	private void handleRewards(VotePlayer votePlayer) {
		votePlayer.addPoints(1);
		votePlayer.addVote();
		votePlayer.setChanged(true);
	}
}
