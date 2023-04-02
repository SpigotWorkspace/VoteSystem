package de.spigotworkspace.votesystem.helper;


import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.UUID;
import java.util.function.BiConsumer;

public class UUIDFetcher {

    public static void getFromName(String username, BiConsumer<JSONObject, Boolean> callback) {
        try {
            Connection connection = Jsoup.connect("https://api.mojang.com/users/profiles/minecraft/" + username);

            connection.method(Connection.Method.GET);
            connection.userAgent("Mozilla/5.0");
            connection.ignoreContentType(true);

            Connection.Response response = connection.execute();
            if (response.statusCode() == 200) {
                callback.accept(new JSONObject(response.body()), true);
            }
        } catch (IOException e) {
            callback.accept(null, false);
        }
    }
}
