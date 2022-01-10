package me.joao.cacador;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.joao.cacador.interacoes.Comandos;
import me.joao.cacador.interacoes.Listeners;
import me.joao.cacador.utils.LocAPI;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	public static File f;
	public static FileConfiguration data;
	public static LocAPI lapi = new LocAPI(new File("plugins/BurkingCacador/Locais.yml"));
	public static Economy econ = null;

	@Override
	public void onEnable() {
		getCommand("cacador").setExecutor(new Comandos());
		getCommand("consolecc").setExecutor(new Comandos());
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		setupFiles();
		setupEconomy();
	}

	public void setupFiles() {
		saveDefaultConfig();
		Main.f = new File(Main.getInstance().getDataFolder(), "data.yml");
		Main.data = (FileConfiguration) YamlConfiguration.loadConfiguration(Main.f);
		File Locais = new File("plugins/BurkingCacador/Locais.yml");
		try {
			Main.data.save(Main.f);
			if (!Locais.exists()) {
				Locais.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Main getInstance() {
		return (Main) Bukkit.getPluginManager().getPlugin("BurkingCacador");
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
}
