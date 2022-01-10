package me.joao.cacador.interacoes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import me.joao.cacador.CacadorManager;
import me.joao.cacador.Main;

public class Listeners implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (CacadorManager.evento.isParticipante(e.getPlayer())) {
			Player p = e.getPlayer();
			CacadorManager.evento.removeParticipante(p);
			p.getInventory().clear();
			p.teleport(Main.lapi.get("Locais.Saida"));
			for (String nick : CacadorManager.evento.getParticipantes().keySet()) {
				Player par = Bukkit.getPlayer(nick);
				if (par != null) {
					par.sendMessage(Main.getInstance().getConfig().getString("Abandonou")
							.replaceAll("@player", p.getName()).replaceAll("&", "§"));
				}
			}
			CacadorManager.checkSaida();
		}
	}

	@EventHandler
	public void onChat(ChatMessageEvent e) {
		if (e.getSender().getName().equalsIgnoreCase(CacadorManager.vencedorAtual)) {
			if (e.getTags().contains("cacador")) {
				e.setTagValue("cacador", Main.getInstance().getConfig().getString("Tag").replace("&", "§"));
			}
		}
	}

	@EventHandler
	public void pontuar(EntityDeathEvent e) {
		if (CacadorManager.evento.isOcorrendo()) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (CacadorManager.evento.isParticipante(p)) {
					CacadorManager.evento.removeParticipante(p);
					p.getInventory().clear();
					p.teleport(Main.lapi.get("Locais.Saida"));
					for (String nick : CacadorManager.evento.getParticipantes().keySet()) {
						Player par = Bukkit.getPlayer(nick);
						if (par != null) {
							par.sendMessage(Main.getInstance().getConfig().getString("Abandonou")
									.replaceAll("@player", p.getName()).replaceAll("&", "§"));
						}
					}
					CacadorManager.checkSaida();
				}
			}
			if (e.getEntity() instanceof Pig) {
				Pig porco = (Pig) e.getEntity();
				if (porco.getKiller() instanceof Player) {
					Player killer = porco.getKiller();
					if (CacadorManager.evento.isParticipante(killer)) {
						if (CacadorManager.evento.getPorcos().contains(porco)) {
							CacadorManager.evento.removePig(porco);
							CacadorManager.evento.pontuar(killer);
							killer.sendMessage(
									"§aVoce matou um porco, seus pontos: §f" + CacadorManager.evento.getPontos(killer));
						}
					}
				}
			}
		}
	}
}
