package com.stationservice.Models;

public class Service {

	private int idService;
	private String nom;
	private int prix;

	// Constructeur par défaut
	public Service() {
	}

	// Constructeur principal (Nom + Prix)
	public Service(String nom, int prix) {
		this.nom = nom;
		this.prix = prix;
	}

	// Constructeur complet avec ID
	public Service(int idService, String nom, int prix) {
		this.idService = idService;
		this.nom = nom;
		this.prix = prix;
	}

	// Getters et Setters
	public int getIdService() {
		return idService;
	}

	public void setIdService(int idService) {
		this.idService = idService;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getPrix() {
		return prix;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}

	@Override
	public String toString() {
		return nom + " (" + prix + " AR)";
	}
}