package com.stationservice.Models;

public class Produit {
	private String num_prod;
	private String design;
	private int stock;
	private int prix_litre_prod;
	
	public Produit(String num_prod, String design, int stock, int prix_litre_prod) {
		this.num_prod = num_prod;
		this.design = design;
		this.stock = stock;
		this.prix_litre_prod = prix_litre_prod;
	}
	
	public String getNum_prod() {
		return num_prod;
	}
	
	public void setNum_prod(String num_prod) {
		this.num_prod = num_prod;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getPrix_litre_prod() {
		return prix_litre_prod;
	}

	public void setPrix_litre_prod(int prix_litre_prod) {
		this.prix_litre_prod = prix_litre_prod;
	};
}


