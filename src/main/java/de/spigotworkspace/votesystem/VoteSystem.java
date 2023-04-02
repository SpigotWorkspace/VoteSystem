package de.spigotworkspace.votesystem;

import de.spigotworkspace.votesystem.commands.VoteCommand;
import de.spigotworkspace.votesystem.commands.VoteDataCommand;
import de.spigotworkspace.votesystem.datasource.DataSource;
import de.spigotworkspace.votesystem.helper.SerializationHelper;
import de.spigotworkspace.votesystem.listeners.VoteListener;
import de.spigotworkspace.votesystem.objects.DataSourceProperties;
import de.spigotworkspace.votesystem.objects.VotePlayer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.Base64;
import java.util.UUID;


public class VoteSystem extends JavaPlugin {
	private DataSource dataSource;

	@Override
	public void onEnable() {
		loadConfig();
		if (!(this.dataSource.createDataSource())) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		prepareDatabase();
		registerListeners();
		registerCommands();
	}

	@Override
	public void onDisable() {
		if (this.dataSource.getHikariDataSource() != null) {
			this.dataSource.getHikariDataSource().close();
		}
	}

	private void registerListeners() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new VoteListener(this), this);
	}

	private void registerCommands() {
		getCommand("votedata").setExecutor(new VoteDataCommand(this));
		getCommand("vote").setExecutor(new VoteCommand(this));
	}

	private void loadConfig() {
		ConfigurationSerialization.registerClass(DataSourceProperties.class, "DataSourceProperties");

		this.saveDefaultConfig();
		FileConfiguration config = this.getConfig();

		//set default
		if (config.get("database") == null) {
			config.addDefault("database", new DataSourceProperties());
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
	}

	private void prepareDatabase() {
		this.dataSource.sendQuery("CREATE TABLE IF NOT EXISTS votedata (UUID VARCHAR(36), VotePlayer VARCHAR(100))");
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}