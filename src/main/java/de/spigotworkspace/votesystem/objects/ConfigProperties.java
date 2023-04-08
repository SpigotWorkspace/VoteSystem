package de.spigotworkspace.votesystem.objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;
@SerializableAs("ConfigProperties")
public class ConfigProperties implements ConfigurationSerializable {

    private int autoSaveIntervalInMinutes;

    public ConfigProperties() {
        this.autoSaveIntervalInMinutes = 15;
    }

    public ConfigProperties(Map<String, Object> map) {
        this.autoSaveIntervalInMinutes = (int) map.get("autoSaveIntervalInMinutes");
    }

    public int getAutoSaveIntervalInMinutes() {
        return autoSaveIntervalInMinutes;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("autoSaveIntervalInMinutes", getAutoSaveIntervalInMinutes());
        return map;
    }
}
