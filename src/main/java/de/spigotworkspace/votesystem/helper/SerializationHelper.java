package de.spigotworkspace.votesystem.helper;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class SerializationHelper {
	public static byte[] toByteArray(Object object) {

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
		) {
			bukkitObjectOutputStream.writeObject(object);

			return byteArrayOutputStream.toByteArray();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromByteArray(byte[] bytes, Class<T> type) {
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream)
		){
			return (T) bukkitObjectInputStream.readObject();

		} catch (IOException | ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}
}
