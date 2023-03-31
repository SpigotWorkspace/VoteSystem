package de.spigotworkspace.voteystem;

import de.spigotworkspace.voteystem.listeners.VoteListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class VoteSystem extends JavaPlugin {
	@Override
	public void onEnable() {
		registerListeners();
	}

	@Override
	public void onDisable() {

	}

	private void registerListeners(){
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new VoteListener(this), this);
	}
}