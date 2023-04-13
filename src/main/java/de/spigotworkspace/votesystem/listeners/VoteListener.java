package de.spigotworkspace.votesystem.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import de.spigotworkspace.votesystem.VoteSystem;
import de.spigotworkspace.votesystem.helper.ProfileFetcher;
import de.spigotworkspace.votesystem.objects.VotePlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.UUID;

public class VoteListener implements Listener {
	private VoteSystem voteSystem;

	public VoteListener(final VoteSystem voteSystem) {
		this.voteSystem = voteSystem;
	}

	@EventHandler
	public void onVote(VotifierEvent event) {
		Vote vote = event.getVote();
		ProfileFetcher.getFromNameOrUniqueId(vote.getUsername(), (jsonObject, success) -> {
			if (success) {
				Bukkit.getOnlinePlayers().forEach(player -> {
					voteSystem.getDataStore().get(player.getUniqueId(), allVotePlayer -> {
						if (allVotePlayer.isBroadcastActivated()) {
							player.sendMessage(String.format("§6§l%s §ehat gevotet. Danke! §4❤", jsonObject.getString("name")));
						}
					});
				});
				voteSystem.getDataStore().get((UUID) jsonObject.get("id"), votePlayer -> {
					if (votePlayer == null) return;
					handleRewards(votePlayer);
				});
			}
		});
	}

	private void handleRewards(VotePlayer votePlayer) {
		if (votePlayer.getVotesToday() >= 2) {
			votePlayer.sendMessage("§6§lVielen Dank für deinen Vote! §eDa du heute bereits zweimal gevotet hast, erhältst du keine Belohnung.");
		} else {
			votePlayer.setVotesToday(votePlayer.getVotesToday() + 1);
			votePlayer.addPoints(1);
			votePlayer.sendMessage("§6§lVielen Dank für deinen Vote! §eAls Belohnung erhältst du einen Punkt.");

			LocalDate lastVote = Instant.ofEpochMilli(votePlayer.getLastVote()).atZone(ZoneId.of("Europe/Berlin")).toLocalDate();
			LocalDate localDateNow = Instant.ofEpochMilli(Instant.now().toEpochMilli()).atZone(ZoneId.of("Europe/Berlin")).toLocalDate();
			int daysBetween = Period.between(lastVote, localDateNow).getDays();
			if (daysBetween > 1) {
				votePlayer.setStreak(1);
			} else {
				if (votePlayer.getVotesToday() == 1) {
					votePlayer.setStreak(votePlayer.getStreak() + 1);

					if (votePlayer.getStreak() % 10 == 0) {
						votePlayer.sendMessage(String.format("§6§lVielen Dank für deinen Vote! §eDa du %d Tage am Stück gevotet hast, erhältst du nochmal zehn Punkte!", votePlayer.getStreak()));
						votePlayer.addPoints(10);
					}
				}
			}

			if (votePlayer.getPoints() == 100 && !votePlayer.isRewardEarned()) {
				votePlayer.setRewardEarned(true);
				sendRewardMessage(votePlayer);

			} else if (votePlayer.isRewardEarned()) {
				sendRewardMessage(votePlayer);
			}
		}

		votePlayer.addVote();
		votePlayer.setLastVote(Instant.now().toEpochMilli());
		votePlayer.setDirty(true);
	}

	private void sendRewardMessage(VotePlayer votePlayer) {
		TextComponent border = new TextComponent("✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸✸");
		border.setColor(ChatColor.YELLOW);
		TextComponent textComponent = new TextComponent("\nDa du 100 Punkte oder mehr erreicht hast, erhältst du ein besonderes Item.");
		textComponent.setColor(ChatColor.GOLD);
		TextComponent clickableTextComponent = new TextComponent("\nKlicke hier um deine Belohnung abzuholen.");
		clickableTextComponent.setColor(ChatColor.LIGHT_PURPLE);
		clickableTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/getreward"));
		votePlayer.sendMessage(border, textComponent, clickableTextComponent, border);
	}
}
