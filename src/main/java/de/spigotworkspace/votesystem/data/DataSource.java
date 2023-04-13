package de.spigotworkspace.votesystem.data;

import com.zaxxer.hikari.HikariDataSource;
import de.spigotworkspace.votesystem.helper.SerializationHelper;
import de.spigotworkspace.votesystem.objects.DataSourceProperties;
import de.spigotworkspace.votesystem.objects.VotePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

	protected ResultSet sendQuery(String query, Consumer<PreparedStatement> parameters) {
		try (Connection connection = getHikariDataSource().getConnection();
			 PreparedStatement prepareStatement = connection.prepareStatement(query)
		){
			parameters.accept(prepareStatement);
			return prepareStatement.executeQuery();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	protected VotePlayer getVotePlayer(UUID uuid) {
		ResultSet resultSet = sendQuery("SELECT data FROM votedata WHERE uuid = ?", (preparedStatement -> {
			try {
				preparedStatement.setString(1, uuid.toString());
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}));
		try {
			if (resultSet.next()) {
				byte[] bytes = resultSet.getBytes("data");
				return SerializationHelper.fromByteArray(bytes, VotePlayer.class).setUniqueId(uuid);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	protected void saveVotePlayers(HashMap<UUID, VotePlayer> votePlayers) {
		List<VotePlayer> votePlayersToUpdate = votePlayers.values().stream().filter(votePlayer -> votePlayer.isDirty()).collect(Collectors.toList());
		if (votePlayersToUpdate.isEmpty()) return;
		String query = buildQuery(votePlayersToUpdate);
		sendQuery(query, preparedStatement -> {
			List<byte[]> bytes = SerializationHelper.toByteArrayList(votePlayersToUpdate);
			AtomicInteger index = new AtomicInteger();
			AtomicInteger uuidIndex = new AtomicInteger(1);
			AtomicInteger bytesIndex = new AtomicInteger(2);

			votePlayersToUpdate.forEach(votePlayer -> {
				try {
					preparedStatement.setString(uuidIndex.get(), votePlayer.getUniqueId().toString());
					preparedStatement.setBytes(bytesIndex.get(), bytes.get(index.get()));
					uuidIndex.addAndGet(2);
					bytesIndex.addAndGet(2);
					index.getAndIncrement();
					votePlayer.setDirty(false);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			});
		});
	}

	private String buildQuery(List<VotePlayer> votePlayers) {
		StringBuilder stringBuilder = new StringBuilder("INSERT INTO votedata (uuid, data) VALUES");
		AtomicInteger rows = new AtomicInteger();
		votePlayers.forEach((votePlayer) -> {
			rows.getAndIncrement();
			if (rows.get() == votePlayers.size()) {
				stringBuilder.append("(?,?)");
			} else {
				stringBuilder.append("(?,?),");
			}
		});

		stringBuilder.append(" ON DUPLICATE KEY UPDATE data=VALUES(data);");
		return stringBuilder.toString();
	}
}
