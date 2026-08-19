package com.stationservice.Models;

public class Service {
	private String numServ;
	private String nomService;
	private int prixService;

	public Service() {}

	public Service(String numServ, String nomService, int prixService) {
		this.numServ = numServ;
		this.nomService = nomService;
		this.prixService = prixService;
	}

	// Identifiant (numServ ou id)
	public String getNumServ() { return numServ; }
	public void setNumServ(String numServ) { this.numServ = numServ; }
	public String getId() { return numServ; }
	public void setId(String id) { this.numServ = id; }

	// Nom du service (nomService ou nom) -> Résout l'erreur getNom()
	public String getNomService() { return nomService; }
	public void setNomService(String nomService) { this.nomService = nomService; }
	public String getNom() { return nomService; }
	public void setNom(String nom) { this.nomService = nom; }

	// Prix du service (prixService ou prix)
	public int getPrixService() { return prixService; }
	public void setPrixService(int prixService) { this.prixService = prixService; }
	public int getPrix() { return prixService; }
	public void setPrix(int prix) { this.prixService = prix; }
}