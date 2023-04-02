package de.spigotworkspace.votesystem.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import de.spigotworkspace.votesystem.VoteSystem;
import de.spigotworkspace.votesystem.helper.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.json.JSONObject;

import java.util.function.BiConsumer;

public class VoteListener implements Listener {
	private VoteSystem voteSystem;

	public VoteListener(final VoteSystem voteSystem) {
		this.voteSystem = voteSystem;
	}

	@EventHandler
	public void onVote(VotifierEvent votifierEvent) {
		Vote vote = votifierEvent.getVote();
		UUIDFetcher.getFromName(vote.getUsername(), (jsonObject, success) -> {
			if (success) {
				Bukkit.broadcastMessage(String.format("§6§l%s §ehat gevotet. Danke! §4❤", jsonObject.get("name")));
			}
		});
	}
}
