package de.spigotworkspace.votesystem.objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("ConfigProperties")
public class ConfigProperties implements ConfigurationSerializable {

    private int autoSaveIntervalInMinutes;
    private int autoReminderIntervalInMinutes;
    private List<String> voteSites;

    public ConfigProperties() {
        this.autoSaveIntervalInMinutes = 15;
        this.autoReminderIntervalInMinutes = 30;
        this.voteSites = Arrays.asList("example.com");
    }

    public ConfigProperties(Map<String, Object> map) {
        this.autoSaveIntervalInMinutes = (int) map.get("autoSaveIntervalInMinutes");
        this.autoReminderIntervalInMinutes = (int) map.get("autoReminderIntervalInMinutes");
        this.voteSites = (List<String>) map.get("voteSites");
    }

    public int getAutoSaveIntervalInMinutes() {
        return autoSaveIntervalInMinutes;
    }

    public int getAutoReminderIntervalInMinutes() {
        return autoReminderIntervalInMinutes;
    }

    public List<String> getVoteSites() {
        return voteSites;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("autoSaveIntervalInMinutes", getAutoSaveIntervalInMinutes());
        map.put("autoReminderIntervalInMinutes", getAutoReminderIntervalInMinutes());
        map.put("voteSites", getVoteSites());
        return map;
    }
}
