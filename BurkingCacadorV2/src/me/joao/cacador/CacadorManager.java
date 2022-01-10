package me.joao.cacador;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.joao.cacador.runnables.AnunciarCacador;
import me.joao.cacador.runnables.EventoTimer;
import me.joao.cacador.runnables.PigSpawn;

public class CacadorManager {
	public static final Cacador evento = new Cacador();
	public static String vencedorAtual = Main.getInstance().getConfig().getString("VencedorAtual");
	private static AnunciarCacador anunciarcaca = new AnunciarCacador();
	private static PigSpawn ps = new PigSpawn();
	private static EventoTimer timer = new EventoTimer();
	private static int tempo_spawn_porco = Main.getInstance().getConfig().getInt("TimeSpawnPorco");
	private static int duracao_evento = Main.getInstance().getConfig().getInt("DuracaoEvento");

	public static void abrirEvento() {
		if (!evento.isOcorrendo()) {
			if (!evento.isAberto()) {
				evento.setEntrada(true);
				evento.setOcorrendo(false);
				anunciarcaca.runTaskTimerAsynchronously(Main.getInstance(), 0, 20 * 10);
			}
		}
	}

	public static void iniciarEvento() {
		if (getData().getStringList("Mobs").size() > 0) {
			if (evento.getParticipantes().keySet().size() > 1) {
				evento.setEntrada(false);
				evento.setOcorrendo(true);
				darKit();
				ps.runTaskTimer(Main.getInstance(), 0, 20 * tempo_spawn_porco);
				timer.runTaskLater(Main.getInstance(), (20 * 60) * duracao_evento);
				broadcast("Iniciando");
			} else {
				cancelarEvento("Poucos participantes");
			}
		} else {
			cancelarEvento("Não há spawn de porco setado.");
		}
	}

	public static Player getVencedor() {
		Player winner = null;
		int maior = -1;
		for (String all : evento.getParticipantes().keySet()) {
			if (evento.getParticipantes().get(all) >= maior) {
				maior = evento.getParticipantes().get(all);
				winner = Bukkit.getPlayer(all);
			}
		}
		return winner;
	}

	public static void encerrarEvento(Player vencedor) {
		for (String todos : evento.getParticipantes().keySet()) {
			Player p = Bukkit.getPlayer(todos);
			p.getInventory().clear();
			p.getInventory().setHelmet(new ItemStack(Material.AIR));
			p.getInventory().setChestplate(new ItemStack(Material.AIR));
			p.getInventory().setLeggings(new ItemStack(Material.AIR));
			p.getInventory().setBoots(new ItemStack(Material.AIR));
			p.teleport(Main.lapi.get("Locais.Saida"));
		}

		double premio = Main.getInstance().getConfig().getDouble("Premio");
		Main.econ.depositPlayer(vencedor, premio);

		Main.getInstance().getConfig().set("VencedorAtual", vencedor.getName());
		Main.getInstance().saveConfig();
		vencedorAtual = vencedor.getName();
		CacadorManager.evento.setEntrada(false);
		CacadorManager.evento.setOcorrendo(false);
		ps.cancel();
		for (Pig porco : CacadorManager.evento.getPorcos()) {
			porco.damage(1000);
		}
		CacadorManager.evento.clearPigs();

		for (String anuncio : Main.getInstance().getConfig().getStringList("AnuncioVitoria")) {
			Bukkit.broadcastMessage(anuncio.replace("&", "§").replace("@player", vencedor.getName()).replace("@pontos",
					evento.getParticipantes().get(vencedor.getName()) + ""));
		}
		try {
			anunciarcaca.cancel();
			timer.cancel();
			ps.cancel();
		} catch (Exception e) {
		}
		anunciarcaca = new AnunciarCacador();
		timer = new EventoTimer();
		ps = new PigSpawn();
		CacadorManager.evento.clearParticipantes();
	}

	public static void darKit() {
		ItemStack arco = new ItemStack(Material.BOW);
		ItemStack flecha = new ItemStack(Material.ARROW);
		ItemMeta armeta = arco.getItemMeta();

		armeta.setDisplayName("§dArco Cacador");
		armeta.addEnchant(Enchantment.ARROW_DAMAGE, 4, true);
		armeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);

		arco.setItemMeta(armeta);

		for (String all : evento.getParticipantes().keySet()) {
			Player players = Bukkit.getPlayer(all);
			players.getInventory().addItem(arco);
			players.getInventory().addItem(flecha);
		}
	}

	public static void checkSaida() {
		if (evento.isOcorrendo()) {
			if (evento.getParticipantes().keySet().size() <= 0) {
				cancelarEvento("Todos os jogadores abandonaram.");
			} else if (evento.getParticipantes().keySet().size() == 1) {
				encerrarEvento(getVencedor());
			}
		}
	}

	public static void cancelarEvento(String motivo) {
		String msgcancelado = Main.getInstance().getConfig().getString("Cancelado").replace("&", "§");
		Bukkit.broadcastMessage(msgcancelado + " Motivo: " + motivo);

		if (evento.getParticipantes().keySet().size() > 0) {
			for (String todos : evento.getParticipantes().keySet()) {
				Player all = Bukkit.getPlayer(todos);
				all.teleport(Main.lapi.get("Locais.Saida"));
				all.getInventory().clear();
				all.getInventory().setHelmet(new ItemStack(Material.AIR));
				all.getInventory().setChestplate(new ItemStack(Material.AIR));
				all.getInventory().setLeggings(new ItemStack(Material.AIR));
				all.getInventory().setBoots(new ItemStack(Material.AIR));
			}
		}

		for (Pig pigs : evento.getPorcos()) {
			pigs.damage(1000);
		}

		evento.setEntrada(false);
		evento.setOcorrendo(false);
		evento.clearParticipantes();
		evento.clearPigs();

		try {
			anunciarcaca.cancel();
			timer.cancel();
			ps.cancel();
		} catch (Exception e) {
		}

		anunciarcaca = new AnunciarCacador();
		timer = new EventoTimer();
		ps = new PigSpawn();
	}

	public static void broadcast(final String path) {
		Bukkit.broadcastMessage(Main.getInstance().getConfig().getString(path).replaceAll("&", "§"));
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
