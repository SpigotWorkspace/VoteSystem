package de.spigotworkspace.votesystem.objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("DataSourceProperties")
public class DataSourceProperties implements ConfigurationSerializable {
    private String ip;
    private String port;
    private String database;
    private String username;
    private String password;

    public DataSourceProperties() {
        this.ip = "";
        this.port = "3306";
        this.database = "";
        this.username = "";
        this.password = "";
    }

    public DataSourceProperties(Map<String, Object> map) {
        this.ip = (String) map.get("ip");
        this.port = (String) map.get("port");
        this.database = (String) map.get("database");
        this.username = (String) map.get("username");
        this.password = (String) map.get("password");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("ip", getIp());
        map.put("port", getPort());
        map.put("database", getDatabase());
        map.put("username", getUsername());
        map.put("password", getPassword());
        return map;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
