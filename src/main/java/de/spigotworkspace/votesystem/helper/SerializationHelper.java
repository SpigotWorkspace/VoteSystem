package de.spigotworkspace.votesystem.helper;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class SerializationHelper {
	public static ArrayList<byte[]> toByteArrayList(List<?> objects) {
		AtomicReference<BukkitObjectOutputStream> bukkitObjectOutputStream = new AtomicReference<>();
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			ArrayList<byte[]> bytes = new ArrayList<>();
			objects.forEach(object -> {
				try {
					bukkitObjectOutputStream.set(new BukkitObjectOutputStream(byteArrayOutputStream));
					bukkitObjectOutputStream.get().writeObject(object);
					bukkitObjectOutputStream.get().flush();
					bytes.add(byteArrayOutputStream.toByteArray());
					byteArrayOutputStream.reset();
				} catch (IOException e) {
					throw new RuntimeException(e);
				} finally {
					try {
						bukkitObjectOutputStream.get().close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			});
			return bytes;

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
