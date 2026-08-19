package com.stationservice.Models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ServiceItem {
    private String numServ;
    private String nomService;
    private int prixService;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    // 1. Constructeur vide obligatoire pour Jdbi
    public ServiceItem() {}

    public ServiceItem(String numServ, String nomService, int prixService) {
        this.numServ = numServ;
        this.nomService = nomService;
        this.prixService = prixService;
    }

    public String getNumServ() { return numServ; }
    public void setNumServ(String numServ) { this.numServ = numServ; }

    public String getNomService() { return nomService; }
    public void setNomService(String nomService) { this.nomService = nomService; }

    public int getPrixService() { return prixService; }
    public void setPrixService(int prixService) { this.prixService = prixService; }

    public BooleanProperty selectedProperty() { return selected; }
    public boolean isSelected() { return selected.get(); }
    public void setSelected(boolean selected) { this.selected.set(selected); }
}