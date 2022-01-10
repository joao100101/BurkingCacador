package me.joao.cacador.interacoes;

import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.joao.cacador.CacadorManager;
import me.joao.cacador.Main;

public class Comandos implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (cmd.getName().contentEquals("consolecc")) {
			if (sender.hasPermission("cacador.iniciar")) {
				if (args[0].equalsIgnoreCase("iniciar")) {
					if (!CacadorManager.evento.isOcorrendo() && !CacadorManager.evento.isAberto()) {
						CacadorManager.abrirEvento();
					} else {
						sender.sendMessage("§4[CACADOR] §cEsse evento ja esta ocorrendo.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("cancelar")) {
					if (CacadorManager.evento.isOcorrendo() || CacadorManager.evento.isAberto()) {
						CacadorManager.cancelarEvento("Cancelado por administrador.");
					} else {
						sender.sendMessage("§4[CACADOR] §cEvento nao esta ocorrendo.");
						return true;
					}
				} else {
					sender.sendMessage("§c/consolecc [iniciar/cancelar]");
					return true;
				}
			} else {
				sender.sendMessage("§4[CACADOR] §cSem permissao.");
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("cacador")) {
			Player p = (Player) sender;

			if (sender instanceof Player) {
				// argumentos só de players
				/*
				 * 
				 * 
				 * SETANDO LOCAIS
				 * 
				 * 
				 */
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("setspawn")) {
						if (p.hasPermission("cacador.setar")) {
							Main.lapi.set(p.getLocation(), "Locais.Entrada");
							p.sendMessage("§2[Cacador] §aEntrada do evento foi setada com sucesso.");
						} else {
							send(p, "SemPermissao");
							return true;
						}
					} else if (args[0].equalsIgnoreCase("setlobby")) {
						if (p.hasPermission("cacador.setar")) {
							Main.lapi.set(p.getLocation(), "Locais.Lobby");
							p.sendMessage("§2[Cacador] §aLobby do evento foi setado com sucesso.");
						} else {
							send(p, "SemPermissao");
							return true;
						}
					} else if (args[0].equalsIgnoreCase("setsaida")) {
						if (p.hasPermission("cacador.setar")) {
							Main.lapi.set(p.getLocation(), "Locais.Saida");
							p.sendMessage("§2[Cacador] §aSaida do evento foi setada com sucesso.");
						} else {
							send(p, "SemPermissao");
							return true;
						}
					} else if (args[0].equalsIgnoreCase("mobspawn")) {
						int index = getData().getStringList("Mobs").size() + 1;
						String arg = String.valueOf(index);
						if (args[1].equalsIgnoreCase("add")) {
							List<String> list = getData().getStringList("Mobs");
							if (!list.contains(arg)) {
								list.add(arg);
								this.set(String.valueOf(arg) + ".world", p.getWorld().getName());
								this.set(String.valueOf(arg) + ".x", p.getLocation().getBlockX());
								this.set(String.valueOf(arg) + ".y", p.getLocation().getBlockY());
								this.set(String.valueOf(arg) + ".z", p.getLocation().getBlockZ());
								this.set("Mobs", list);
								this.save();
								p.sendMessage("§2[Cacador] §aLocal de Mob adicionado com sucesso: §f" + arg);
							} else {
								p.sendMessage(
										"§4[Cacador] §cEste local ja esta definido. Escolha outro nome ou delete o mesmo.");
							}

						} else if (args[1].equalsIgnoreCase("del")) {
							String a_retirar = String.valueOf(args[2]);
							if (getData().getConfigurationSection(a_retirar) == null) {
								p.sendMessage("§4[Cacador] §cEste local de Mob nao existe.");
								return true;
							}
							List<String> list = getData().getStringList("Mobs");
							if (list.contains(a_retirar)) {
								list.remove(a_retirar);
								this.set(a_retirar, null);
								this.set("Mobs", list);
								this.save();
								p.sendMessage("§2[Cacador] §aLocal de Mob deletado com sucesso: §f" + a_retirar);

							} else {
								p.sendMessage("§4[Cacador] §cEste local de Mob nao existe.");
								return true;
							}
						} else if (args[1].equalsIgnoreCase("list")) {
							List<String> list = getData().getStringList("Mobs");
							p.sendMessage("§3[Cacador] §eLista de Locais de Mob");
							for (String local : list) {
								Location loc = getLoc(local);
								p.sendMessage(
										"§6" + local + ": §e" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
							}
						} else if (args[1].equalsIgnoreCase("limpar")) {
							List<String> list = getData().getStringList("Mobs");
							if (list.size() > 0) {
								for (int i = 1; i < list.size() + 1; i++) {
									set(String.valueOf(i), null);
								}
								set("Mobs", null);
								save();
								p.sendMessage("§2[CACADOR] §aLista de spawners limpa com sucesso.");
							} else {
								p.sendMessage("§4[CACADOR] §cA lista de spawners esta vazia.");
								return true;
							}
						} else {
							p.sendMessage("§4[Cacador] §cArgumento invalido.");
							p.sendMessage("§4[Cacador] §cArgs validos: [add/del/list/limpar]");
							return true;
						}
						/*
						 * 
						 * 
						 * INICIAR/CANCELAR
						 * 
						 * 
						 */
					} else if (args[0].equalsIgnoreCase("iniciar")) {
						if (p.hasPermission("cacador.iniciar")) {
							if (!(CacadorManager.evento.isOcorrendo()) && !(CacadorManager.evento.isAberto())) {
								// Iniciar
								CacadorManager.abrirEvento();
								send(p, "Iniciando");
							} else {
								p.sendMessage("§4[CACADOR] §cO evento ja foi aberto.");
								return true;
							}
						} else {
							send(p, "SemPermissao");
							return true;
						}
					} else if (args[0].equalsIgnoreCase("forcestart")) {
						if (p.hasPermission("cacador.iniciar")) {
							if (CacadorManager.evento.isAberto()) {
								CacadorManager.iniciarEvento();
							} else {
								p.sendMessage("§4[CACADOR] §cO evento nao esta ocorrendo.");
								return true;
							}
						}
					} else if (args[0].equalsIgnoreCase("cancelar")) {
						if (p.hasPermission("cacador.iniciar")) {
							if (CacadorManager.evento.isOcorrendo() || CacadorManager.evento.isAberto()) {
								CacadorManager.cancelarEvento("Cancelado por administrador.");
							} else {
								p.sendMessage("§4[CACADOR] §cO evento nao esta ocorrendo.");
								return true;
							}
						} else {
							send(p, "SemPermissao");
							return true;
						}
						/*
						 * 
						 * 
						 * ENTRAR/SAIR
						 * 
						 * 
						 */
					} else if (args[0].equalsIgnoreCase("entrar")) {
						if (!CacadorManager.evento.isOcorrendo()) {
							if (CacadorManager.evento.isAberto()) {
								if (!(isParticipante(p))) {
									if (isEmpty(p)) {
										if (isEmptyArmor(p)) {
											CacadorManager.evento.addParticipante(p);
											p.teleport(Main.lapi.get("Locais.Entrada"));
											send(p, "Entrou");
											for (PotionEffect ef : p.getActivePotionEffects()) {
												p.removePotionEffect(ef.getType());
											}
											p.setFoodLevel(20);
											p.setHealth(p.getMaxHealth());
										} else {
											send(p, "Inventario");
											return true;
										}
									} else {
										send(p, "Inventario");
										return true;
									}
								} else {
									send(p, "Dentro");
									return true;
								}
							} else {
								send(p, "SemEvento");
								return true;
							}
						} else {
							send(p, "JaIniciado");
							return true;
						}
					} else if (args[0].equalsIgnoreCase("sair")) {
						if (isParticipante(p)) {
							CacadorManager.evento.removeParticipante(p);
							p.getInventory().clear();
							p.teleport(Main.lapi.get("Locais.Saida"));
							send(p, "Saiu");
							for (String nick : CacadorManager.evento.getParticipantes().keySet()) {
								Player par = Bukkit.getPlayer(nick);
								if (par != null) {
									par.sendMessage(Main.getInstance().getConfig().getString("Abandonou")
											.replaceAll("@player", p.getName()).replaceAll("&", "§"));
								}
							}
							// checar se evento nao esta vazio
							CacadorManager.checkSaida();
						} else {
							send(p, "NaoEsta");
							return true;
						}
					} else {
						if (p.hasPermission("cacador.admin")) {
							p.sendMessage("§d§m---------------§d§l[§5§lC§d§lACADOR§d§l]§d§m---------------");
							p.sendMessage("§5* §d/cacador entrar §5- §dEntra no evento.");
							p.sendMessage("§5* §d/cacador sair §5- §dSai do evento.");
							p.sendMessage("§5* §d/cacador iniciar §5- §dInicia o evento.");
							p.sendMessage("§5* §d/cacador forcestart §5- §dForca abertura do evento.");
							p.sendMessage("§5* §d/cacador cancelar §5- §dCancela o evento.");
							p.sendMessage("§5* §d/cacador setspawn §5- §dSeta entrada do evento.");
							p.sendMessage("§5* §d/cacador setlobby §5- §dSeta lobby do evento.");
							p.sendMessage("§5* §d/cacador setsaida §5- §dSeta saida do evento.");
							p.sendMessage("§5* §d/cacador mobspawn add §5- §dAdd spawner no evento.");
							p.sendMessage("§5* §d/cacador mobspawn del §5- §dRemove spawner no evento.");
							p.sendMessage("§5* §d/cacador mobspawn list §5- §dLista spawners do evento.");
							p.sendMessage("§5* §d/cacador mobspawn limpar §5- §dLimpa spawners do evento.");
							p.sendMessage("§5* §d/consolecc iniciar §5- §dInicia o evento.");
							p.sendMessage("§5* §d/consolecc cancelar §5- §dCancela o evento.");
							p.sendMessage("§d§m---------------------------------------");
						} else {
							p.sendMessage("§d§m---------------§d§l[§5§lC§d§lACADOR§d§l]§d§m---------------");
							p.sendMessage("§5* §d/cacador entrar §5- §dEntra no evento.");
							p.sendMessage("§5* §d/cacador sair §5- §dSai do evento.");
							p.sendMessage("§d§m---------------------------------------");
						}
					}
				} else {
					if (p.hasPermission("cacador.admin")) {
						p.sendMessage("§d§m---------------§d§l[§5§lC§d§lACADOR§d§l]§d§m---------------");
						p.sendMessage("§5* §d/cacador entrar §5- §dEntra no evento.");
						p.sendMessage("§5* §d/cacador sair §5- §dSai do evento.");
						p.sendMessage("§5* §d/cacador iniciar §5- §dInicia o evento.");
						p.sendMessage("§5* §d/cacador forcestart §5- §dForca abertura do evento.");
						p.sendMessage("§5* §d/cacador cancelar §5- §dCancela o evento.");
						p.sendMessage("§5* §d/cacador setspawn §5- §dSeta entrada do evento.");
						p.sendMessage("§5* §d/cacador setlobby §5- §dSeta lobby do evento.");
						p.sendMessage("§5* §d/cacador setsaida §5- §dSeta saida do evento.");
						p.sendMessage("§5* §d/cacador mobspawn add §5- §dAdd spawner no evento.");
						p.sendMessage("§5* §d/cacador mobspawn del §5- §dRemove spawner no evento.");
						p.sendMessage("§5* §d/cacador mobspawn list §5- §dLista spawners do evento.");
						p.sendMessage("§5* §d/cacador mobspawn limpar §5- §dLimpa spawners do evento.");
						p.sendMessage("§5* §d/consolecc iniciar §5- §dInicia o evento.");
						p.sendMessage("§5* §d/consolecc cancelar §5- §dCancela o evento.");
						p.sendMessage("§d§m---------------------------------------");
					} else {
						p.sendMessage("§d§m---------------§d§l[§5§lC§d§lACADOR§d§l]§d§m---------------");
						p.sendMessage("§5* §d/cacador entrar §5- §dEntra no evento.");
						p.sendMessage("§5* §d/cacador sair §5- §dSai do evento.");
						p.sendMessage("§d§m---------------------------------------");
					}
				}
			} else {
				sender.sendMessage("§cComando correto: /consolecc");
				return true;
			}

		}
		return false;

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

	public boolean isEmpty(final Player p) {
		for (ItemStack item : p.getInventory().getContents()) {
			if (item != null) {
				return false;
			}
		}
		return true;
	}

	public boolean isParticipante(Player p) {
		return CacadorManager.evento.isParticipante(p);
	}

	public boolean isEmptyArmor(final Player p) {
		return p.getEquipment().getHelmet() == null && p.getEquipment().getChestplate() == null
				&& p.getEquipment().getLeggings() == null && p.getEquipment().getBoots() == null;
	}

	public static void send(final Player p, final String path) {
		p.sendMessage(Main.getInstance().getConfig().getString(path).replaceAll("&", "§"));
	}

	public void console(final String path) {
		Bukkit.getConsoleSender().sendMessage(Main.getInstance().getConfig().getString(path).replaceAll("&", "§"));
	}

	@Deprecated
	public static Location getLoc(final String path) {
		return new Location(Bukkit.getWorld(getData().getString(String.valueOf(path) + ".world")),
				getData().getDouble(String.valueOf(path) + ".x"), getData().getDouble(String.valueOf(path) + ".y"),
				getData().getDouble(String.valueOf(path) + ".z"));
	}

	public static void broadcast(final String path) {
		Bukkit.broadcastMessage(Main.getInstance().getConfig().getString(path).replaceAll("&", "§"));
	}
}
