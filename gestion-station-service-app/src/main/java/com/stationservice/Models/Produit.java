package com.stationservice.Models;
// com.stationservice.Models.Produit;


public class Produit {
	private String num_prod;
	private String design;
	private Double stock;
	private int prix_litre_prod;
        
        public Produit(){}
	
	public Produit(String num_prod, String design, Double stock, int prix_litre_prod) {
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

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

	public int getPrix_litre_prod() {
		return prix_litre_prod;
	}

	public void setPrix_litre_prod(int prix_litre_prod) {
		this.prix_litre_prod = prix_litre_prod;
	};
        
        @Override
        public String toString() {
            return design + " (" + prix_litre_prod + " €)";
        }
}


