package de.spigotworkspace.votesystem.helper;


import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Utility class to get information about a mojang profile.
 */
public abstract class ProfileFetcher {
    /**
     * JSONObject keys: <br>
     *    - id: UUID<br>
     *    - name: Username
     *
     * @param value UUID oder Username
     * @param callback BiConsumer with a boolean which indicates whether the operation was successful or not. If the operation was successful the JSONObject contains the username and the UUID.
     */
    public static void getFromNameOrUniqueId(String value, BiConsumer<JSONObject, Boolean> callback) {
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/";

            if (value.length() > 16) { //is the value a UUID?
                url = "https://api.mojang.com/user/profile/";
            }

            Connection connection = Jsoup.connect(url + value);
            connection.method(Connection.Method.GET);
            connection.ignoreContentType(true);

            Connection.Response response = connection.execute();
            if (response.statusCode() == 200) {
                JSONObject jsonObject = new JSONObject(response.body());
                jsonObject.put("id", fromTrimmed(jsonObject.getString("id")));
                callback.accept(jsonObject, true);
            }
        } catch (IOException e) {
            callback.accept(null, false);
        }
    }

    /**
     *
     * @param username
     * @return an optional with a UUID if the operation was successful
     */
    public static Optional<UUID> getUniqueIdFromName(String username) {
        try {
            Connection connection = Jsoup.connect("https://api.mojang.com/users/profiles/minecraft/" + username);
            connection.method(Connection.Method.GET);
            connection.ignoreContentType(true);

            Connection.Response response = connection.execute();
            if (response.statusCode() == 200) {
                JSONObject jsonObject = new JSONObject(response.body());
                return Optional.of(fromTrimmed(jsonObject.getString("id")));
            }

        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    private static UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException{
        if (trimmedUUID.length() != 32) return null;
        StringBuilder builder = new StringBuilder(trimmedUUID);
        builder.insert(20, "-");
        builder.insert(16, "-");
        builder.insert(12, "-");
        builder.insert(8, "-");
        return UUID.fromString(builder.toString());
    }
}
