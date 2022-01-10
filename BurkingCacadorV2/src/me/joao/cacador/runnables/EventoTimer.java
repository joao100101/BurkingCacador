package me.joao.cacador.runnables;

import org.bukkit.scheduler.BukkitRunnable;

import me.joao.cacador.CacadorManager;

public class EventoTimer extends BukkitRunnable {

	@Override
	public void run() {
		if (CacadorManager.evento.isOcorrendo()) {
			CacadorManager.encerrarEvento(CacadorManager.getVencedor());
		} else {
			this.cancel();
		}
	}

}
