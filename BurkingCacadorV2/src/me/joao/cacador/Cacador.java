package me.joao.cacador;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

public class Cacador {
	private HashMap<String, Integer> participantes;
	private ArrayList<Pig> porcos;
	private boolean entrada;
	private boolean ocorrendo;

	public Cacador() {
		participantes = new HashMap<String, Integer>();
		porcos = new ArrayList<>();
		entrada = false;
		ocorrendo = false;
	}

	public void addPig(Pig p) {
		porcos.add(p);
	}

	public void removePig(Pig p) {
		porcos.remove(p);
	}

	public void clearPigs() {
		porcos.clear();
	}

	public void clearParticipantes() {
		participantes.clear();
	}

	public void addParticipante(Player p) {
		participantes.put(p.getName(), 0);
	}

	public void removeParticipante(Player p) {
		participantes.remove(p.getName());
	}

	public boolean isParticipante(Player p) {

		return participantes.containsKey(p.getName());
	}

	public int getPontos(Player p) {
		return participantes.get(p.getName());
	}

	public HashMap<String, Integer> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(HashMap<String, Integer> participantes) {
		this.participantes = participantes;
	}

	public ArrayList<Pig> getPorcos() {
		return porcos;
	}

	public void setPorcos(ArrayList<Pig> porcos) {
		this.porcos = porcos;
	}

	public boolean isAberto() {
		return entrada;
	}

	public void setEntrada(boolean entrada) {
		this.entrada = entrada;
	}

	public boolean isOcorrendo() {
		return ocorrendo;
	}

	public void setOcorrendo(boolean ocorrendo) {
		this.ocorrendo = ocorrendo;
	}

	public void pontuar(Player p) {
		participantes.put(p.getName(), participantes.get(p.getName()) + 1);
	}
}
