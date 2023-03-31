package de.spigotworkspace.voteystem.helper;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class SerializationHelper {
	public static String toBase64(Object object){

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
		) {
			bukkitObjectOutputStream.writeObject(object);
			byte[] bytes = byteArrayOutputStream.toByteArray();

			return Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromBase64(String base64, Class<T> type){
		byte[] bytes = Base64.getDecoder().decode(base64);
		try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream)
		){
			return (T) bukkitObjectInputStream.readObject();

		}catch (IOException | ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}
}
