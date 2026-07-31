package com.stationservice.Models;

public class Service {
	private String num_serv;
	private String service;
	private int prix_service;
	
	public Service(String num_serv, String service, int prix_service ) {
		this.num_serv = num_serv;
		this.service = service;
		this.prix_service = prix_service;
	}

	public String getNum_serv() {
		return num_serv;
	}

	public void setNum_serv(String num_serv) {
		this.num_serv = num_serv;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public int getPrix_service() {
		return prix_service;
	}

	public void setPrix_service(int prix_service) {
		this.prix_service = prix_service;
	}
	
}

