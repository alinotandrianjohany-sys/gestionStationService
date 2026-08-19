package com.stationservice.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Entretien {

    private String numEntretien;
    private String immatriculationVoiture;
    private String nomClient;
    private LocalDateTime dateEntretien;
    private int prixEntretien;
    private List<Service> services;

    // Constructeurs
    public Entretien() {
        this.services = new ArrayList<>();
        this.dateEntretien = LocalDateTime.now();
        this.prixEntretien = 0;
    }

    public Entretien(String numEntretien, String immatriculationVoiture, String nomClient) {
        this();
        this.numEntretien = numEntretien;
        this.immatriculationVoiture = immatriculationVoiture;
        this.nomClient = nomClient;
    }

    public Entretien(String numEntretien, String immatriculationVoiture, String nomClient, LocalDateTime dateEntretien, int prixEntretien) {
        this();
        this.numEntretien = numEntretien;
        this.immatriculationVoiture = immatriculationVoiture;
        this.nomClient = nomClient;
        this.dateEntretien = dateEntretien;
        this.prixEntretien = prixEntretien;
    }

    // Gestion des services
    public void ajouterService(Service service) {
        if (this.services == null) {
            this.services = new ArrayList<>();
        }
        if (service != null) {
            this.services.add(service);
            this.prixEntretien += service.getPrix(); // Correction ici: getPrix() au lieu de getPrixService()
        }
    }

    public void recalculerPrixTotal() {
        this.prixEntretien = 0;
        if (this.services != null) {
            for (Service s : this.services) {
                this.prixEntretien += s.getPrix(); // Correction ici: getPrix() au lieu de getPrixService()
            }
        }
    }

    // Getters et Setters
    public String getNumEntretien() {
        return numEntretien;
    }

    public void setNumEntretien(String numEntretien) {
        this.numEntretien = numEntretien;
    }

    public String getImmatriculationVoiture() {
        return immatriculationVoiture;
    }

    public void setImmatriculationVoiture(String immatriculationVoiture) {
        this.immatriculationVoiture = immatriculationVoiture;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public LocalDateTime getDateEntretien() {
        return dateEntretien;
    }

    public void setDateEntretien(LocalDateTime dateEntretien) {
        this.dateEntretien = dateEntretien;
    }

    public int getPrixEntretien() {
        return prixEntretien;
    }

    public void setPrixEntretien(int prixEntretien) {
        this.prixEntretien = prixEntretien;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
        recalculerPrixTotal();
    }
}