package com.stationservice.Models;


import javafx.beans.property.SimpleStringProperty;

public class Website{
	private final SimpleStringProperty name;
	private final SimpleStringProperty url;
	
	public Website(String name, String url) {
		this.name = new SimpleStringProperty(name);
		this.url = new SimpleStringProperty(url);
	}
	
	public String getName() {
		return this.name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public String getUrl() {
		return this.url.get();
	}
	
	public void setUrl(String url) {
		this.url.set(url); 
	}
}