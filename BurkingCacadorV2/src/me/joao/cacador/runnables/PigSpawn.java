package me.joao.cacador.runnables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.scheduler.BukkitRunnable;

import me.joao.cacador.CacadorManager;
import me.joao.cacador.Main;

public class PigSpawn extends BukkitRunnable {
	ArrayList<Location> locs = new ArrayList<>();

	public PigSpawn() {
		Location loc;
		if (getData().getStringList("Mobs").size() > 0) {
			for (String i : getData().getStringList("Mobs")) {
				World w = Bukkit.getWorld(getData().getString(i + ".world"));
				double x = getData().getDouble(i + ".x");
				double y = getData().getDouble(i + ".y");
				double z = getData().getDouble(i + ".z");
				loc = new Location(w, x, y, z);
				locs.add(loc);
			}
		}
	}

	@Override
	public void run() {
		if (CacadorManager.evento.isOcorrendo()) {
			Random nr = new Random();
			int r = nr.nextInt(locs.size());
			Location loc = locs.get(r);

			LivingEntity mob = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.PIG);
			Pig p = (Pig) mob;

			p.setCustomName(Main.getInstance().getConfig().getString("NomePorco").replace("&", "§"));
			p.setCustomNameVisible(true);
			p.setHealth(10);
			CacadorManager.evento.addPig(p);
		} else {
			this.cancel();
		}
	}

	public static FileConfiguration getData() {
		return Main.data;
	}

	public void set(final String path, final Object value) {
		getData().set(path, value);
	}

	public void save() {
		try {
			Main.data.save(Main.f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
