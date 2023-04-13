package de.spigotworkspace.votesystem;

import de.spigotworkspace.votesystem.commands.*;
import de.spigotworkspace.votesystem.data.DataSource;
import de.spigotworkspace.votesystem.data.DataStore;
import de.spigotworkspace.votesystem.listeners.BlockListener;
import de.spigotworkspace.votesystem.listeners.InventoryListener;
import de.spigotworkspace.votesystem.listeners.VoteListener;
import de.spigotworkspace.votesystem.objects.ConfigProperties;
import de.spigotworkspace.votesystem.objects.DataSourceProperties;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.Collectors;


public class VoteSystem extends JavaPlugin {
	private DataSource dataSource;
	private DataStore dataStore;
	private ConfigProperties configProperties;

	@Override
	public void onEnable() {
		loadConfig();
		if (!(this.dataSource.createDataSource())) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		this.dataStore = new DataStore(this);
		this.dataStore.startAutoSave();
		prepareDatabase();
		registerListeners();
		registerCommands();
		startAutoReminder();
	}

	@Override
	public void onDisable() {
		this.dataStore.saveVotePlayers();
		if (this.dataSource.getHikariDataSource() != null) {
			this.dataSource.getHikariDataSource().close();
		}
	}

	private void registerListeners() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new VoteListener(this), this);
		pluginManager.registerEvents(new InventoryListener(this), this);
		pluginManager.registerEvents(new BlockListener(), this);
	}

	private void registerCommands() {
		getCommand("getreward").setExecutor(new GetRewardCommand(this));
		getCommand("managenotifications").setExecutor(new ManageNotificationsCommand(this));
		getCommand("votedata").setExecutor(new VoteDataCommand(this));
		getCommand("vote").setExecutor(new VoteCommand(this));
	}

	private void loadConfig() {
		ConfigurationSerialization.registerClass(DataSourceProperties.class, "DataSourceProperties");
		ConfigurationSerialization.registerClass(ConfigProperties.class, "ConfigProperties");

		this.saveDefaultConfig();
		FileConfiguration config = this.getConfig();

		//set defaults
		if (config.get("database") == null) {
			config.addDefault("database", new DataSourceProperties());
			config.options().copyDefaults(true);
			this.saveConfig();
		}

		if (config.get("properties") == null) {
			config.addDefault("properties", new ConfigProperties());
			config.options().copyDefaults(true);
			this.saveConfig();
		}

		//create DataSource
		DataSourceProperties dataSourceProperties = (DataSourceProperties) config.get("database");
		if (StringUtils.isAnyBlank(dataSourceProperties.getIp(), dataSourceProperties.getDatabase(),
				dataSourceProperties.getUsername(), dataSourceProperties.getPort())
		){
			dataSourceProperties = null;
		}
		this.dataSource = new DataSource(dataSourceProperties);

		//load properties
		this.configProperties = (ConfigProperties) config.get("properties");
	}

	private void prepareDatabase() {
		this.dataSource.sendQuery("CREATE TABLE IF NOT EXISTS votedata (uuid VARCHAR(36), data BLOB, PRIMARY KEY (uuid))");
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public ConfigProperties getConfigProperties() {
		return configProperties;
	}

	private void startAutoReminder() {
		int interval = this.getConfigProperties().getAutoReminderIntervalInMinutes();
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach(player -> {
					dataStore.get(player.getUniqueId(), votePlayer -> {
						if (votePlayer.isReminderActivated() && votePlayer.getVotesToday() == 0) {
							votePlayer.sendMessage("§6§lDu hast heute noch nicht gevotet!\n" +
									"§6Hier kannst du für den Server voten:\n" +
									"§d - "+ getConfigProperties().getVoteSites().stream().collect(Collectors.joining("\n - ")) +
									"\n§6Kleiner Tipp: Es lohnt sich.");
						}
					});
				});
			}
		}.runTaskTimer(this, 20L * 60 * interval, 20L * 60 * interval);
	}
}