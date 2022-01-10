package me.joao.cacador.utils;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LocAPI {
	private File f;
	private FileConfiguration config;

	public LocAPI(File f) {
		this.f = f;
		config = YamlConfiguration.loadConfiguration(f);
	}

	public void set(Location loc, String path) {
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		double yaw = loc.getYaw();
		double pitch = loc.getPitch();
		World world = loc.getWorld();

		String save = x + ";" + y + ";" + z + ";" + yaw + ";" + pitch + ";" + world.getName();

		config.set(path, save);
		try {
			config.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Location get(String path) {
		Location loc;
		try {
			String dado = config.getString(path);

			double x = Double.parseDouble(dado.split(";")[0]);
			double y = Double.parseDouble(dado.split(";")[1]);
			double z = Double.parseDouble(dado.split(";")[2]);
			double yaw = Double.parseDouble(dado.split(";")[3]);
			double pitch = Double.parseDouble(dado.split(";")[4]);
			String world = dado.split(";")[5];

			loc = new Location(Bukkit.getWorld(world), x, y, z, (short) (int) yaw, (short) (int) pitch);
		} catch (Exception e) {
			loc = null;
			System.out.println("");
			System.out.println("[!] Erro ao encontrar o path: " + path);
			System.out.println("");
		}
		return loc;
	}
}
