package com.stationservice.Models;
import java.time.LocalDateTime;

public class Entree {
	private String num_entr;
	private String num_prod;
	private double stock_entree;
	private LocalDateTime date_entree;
	
	public Entree(String num_entr, String num_prod, int stock_entree ) {
		this.num_entr = num_entr;
		this.num_prod = num_prod;
		this.stock_entree = stock_entree;
		this.date_entree = LocalDateTime.now();				
	}
        
        // Constructeur sans argument (nécessaire pour JDBI/Jackson/Frameworks)
        public Entree() {
        }
        

	public String getNum_entr() {
		return num_entr;
	}

	public void setNum_entr(String num_entr) {
		this.num_entr = num_entr;
	}

	public String getNum_prod() {
		return num_prod;
	}

	public void setNum_prod(String num_prod) {
		this.num_prod = num_prod;
	}

	public double getStock_entree() {
		return stock_entree;
	}

	public void setStock_entree(double stock_entree) {
		this.stock_entree = stock_entree;
	}

	public LocalDateTime getDate_entree() {
		return date_entree;
	}

	public void setDate_entree(LocalDateTime date_entree) {
		this.date_entree = date_entree;
	}
}
