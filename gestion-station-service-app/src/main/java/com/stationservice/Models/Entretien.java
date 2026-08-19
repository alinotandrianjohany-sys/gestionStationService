package com.stationservice.Models;

import java.time.LocalDateTime;

public class Entretien {
    private String numEntr;
    private String immatriculationVoiture;
    private String nomClient;
    private LocalDateTime dateEntretien;
    private int prixEntretien;

    // Constructeur vide (nécessaire pour Jdbi)
    public Entretien() {}

    // Constructeur avec tous les arguments (nécessaire pour la création dans le contrôleur)
    public Entretien(String numEntr, String immatriculationVoiture, String nomClient, LocalDateTime dateEntretien, int prixEntretien) {
        this.numEntr = numEntr;
        this.immatriculationVoiture = immatriculationVoiture;
        this.nomClient = nomClient;
        this.dateEntretien = dateEntretien;
        this.prixEntretien = prixEntretien;
    }

    // Getters et Setters
    public String getNumEntr() { return numEntr; }
    public void setNumEntr(String numEntr) { this.numEntr = numEntr; }

    public String getImmatriculationVoiture() { return immatriculationVoiture; }
    public void setImmatriculationVoiture(String immatriculationVoiture) { this.immatriculationVoiture = immatriculationVoiture; }

    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }

    public LocalDateTime getDateEntretien() { return dateEntretien; }
    public void setDateEntretien(LocalDateTime dateEntretien) { this.dateEntretien = dateEntretien; }

    public int getPrixEntretien() { return prixEntretien; }
    public void setPrixEntretien(int prixEntretien) { this.prixEntretien = prixEntretien; }
}