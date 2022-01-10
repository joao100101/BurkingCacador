package me.joao.cacador.runnables;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.joao.cacador.CacadorManager;
import me.joao.cacador.Main;

public class AnunciarCacador extends BukkitRunnable {
	private int chamadas = Main.getInstance().getConfig().getInt("Chamadas");
	private List<String> anuncio = Main.getInstance().getConfig().getStringList("Anuncio");

	@Override
	public void run() {
		if (CacadorManager.evento.isAberto()) {
			if (chamadas > 0) {
				for (String linha : anuncio) {
					Bukkit.broadcastMessage(linha.replace("&", "§").replace("@chamadas", chamadas + ""));
				}
				chamadas--;
			} else {
				CacadorManager.iniciarEvento();
				this.cancel();
			}
		} else {
			this.cancel();
		}
	}

}
