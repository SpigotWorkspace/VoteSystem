package de.spigotworkspace.voteystem.datasource;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {
	private HikariDataSource hikariDataSource;
	private String ip;
	private String port;
	private String database;
	private String username;
	private String password;

	public DataSource(final String ip, final String port, final String database, final String username, final String password) {
		this.ip = ip;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public void connect(){
		hikariDataSource = new HikariDataSource();
		hikariDataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		hikariDataSource.addDataSourceProperty("serverName", ip);
		hikariDataSource.addDataSourceProperty("port", port);
		hikariDataSource.addDataSourceProperty("databaseName", database);
		hikariDataSource.addDataSourceProperty("user", username);
		hikariDataSource.addDataSourceProperty("password", password);
	}

	public HikariDataSource getHikariDataSource(){
		return hikariDataSource;
	}

	public ResultSet sendQuery(String sql){
		try(Connection connection = getHikariDataSource().getConnection();
			Statement statement = connection.prepareStatement(sql)
		){
			return statement.getResultSet();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
