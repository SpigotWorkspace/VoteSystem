package de.spigotworkspace.votesystem.datasource;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.util.PropertyElf;
import de.spigotworkspace.votesystem.objects.DataSourceProperties;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.*;

public class DataSource {
	private HikariDataSource hikariDataSource;

	private DataSourceProperties dataSourceProperties;

	public DataSource(DataSourceProperties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}

	public boolean createDataSource() {
		if (dataSourceProperties != null) {
			hikariDataSource = new HikariDataSource();
			hikariDataSource.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
			hikariDataSource.addDataSourceProperty("url", String.format("jdbc:mariadb://%s:%s/%s", dataSourceProperties.getIp(), dataSourceProperties.getPort(),dataSourceProperties.getDatabase()));
			hikariDataSource.addDataSourceProperty("user", dataSourceProperties.getUsername());
			hikariDataSource.addDataSourceProperty("password", dataSourceProperties.getPassword());
		} else {
			System.out.println("Bitte konfiguriere die VoteSystem Config, damit eine Datenbankverbindung hergestellt werden kann.");
		}
		return dataSourceProperties != null;
	}

	public HikariDataSource getHikariDataSource() {
		return hikariDataSource;
	}

	public ResultSet sendQuery(String query) {
		try (Connection connection = getHikariDataSource().getConnection();
			PreparedStatement prepareStatement = connection.prepareStatement(query)
		){
			return prepareStatement.executeQuery();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
