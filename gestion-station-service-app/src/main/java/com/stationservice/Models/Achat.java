package com.stationservice.Models;
//com.stationservice.Models.Achat;

import java.time.LocalDateTime;
public class Achat {
	private String num_achat;
	private String num_prod;
	private String nom_client;
	private Double nbr_litre;
	private int montant_paye_achat;
	private LocalDateTime date_achat;
	
	public Achat(String num_achat, String num_prod, String nom_client,Double nbr_litre, int montant_paye_achat) {
		this.num_achat = num_achat;
		this.num_prod = num_prod;
		this.nom_client = nom_client;
		this.nbr_litre = nbr_litre;
		this.montant_paye_achat = montant_paye_achat;
		this.date_achat = LocalDateTime.now();
	}

	public String getNum_achat() {
		return num_achat;
	}

	public void setNum_achat(String num_achat) {
		this.num_achat = num_achat;
	}

	public String getNum_prod() {
		return num_prod;
	}

	public void setNum_prod(String num_prod) {
		this.num_prod = num_prod;
	}

	public String getNom_client() {
		return nom_client;
	}

	public void setNom_client(String nom_client) {
		this.nom_client = nom_client;
	}

	public Double getNbr_litre() {
		return nbr_litre;
	}

	public void setNbr_litre(Double nbr_litre) {
		this.nbr_litre = nbr_litre;
	}

	public int getMontant_paye_achat() {
		return montant_paye_achat;
	}

	public void setMontant_paye_achat(int montant_paye_achat) {
		this.montant_paye_achat = montant_paye_achat;
	}

	public LocalDateTime getDate_achat() {
		return date_achat;
	}

	public void setDate_achat(LocalDateTime date_achat) {
		this.date_achat = date_achat;
	}
}
 


