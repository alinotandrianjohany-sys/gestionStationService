package com.stationservice.Controller;

import com.stationservice.config.DatabaseConfig;
import com.stationservice.dao.AchatDao;
import com.stationservice.dao.EntretienDao;
import com.stationservice.dao.ProduitDao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label labelChiffreAffaires;

    @FXML
    private Label labelNombreAchats;

    @FXML
    private Label labelStockFaible;

    @FXML
    public void initialize() {
        try {
            // Instanciation des DAO via Jdbi
            AchatDao achatDao = DatabaseConfig.getJdbi().onDemand(AchatDao.class);
            EntretienDao entretienDao = DatabaseConfig.getJdbi().onDemand(EntretienDao.class);
            ProduitDao produitDao = DatabaseConfig.getJdbi().onDemand(ProduitDao.class);

            // Calcul du chiffre d'affaires
            int caCarburant = 0;
            int caEntretien = 0;

            try {
                caCarburant = achatDao.getChiffreAffairesCarburant();
            } catch (Exception e) {
                System.err.println("Erreur getChiffreAffairesCarburant: " + e.getMessage());
            }

            try {
                caEntretien = entretienDao.getChiffreAffairesEntretien();
            } catch (Exception e) {
                System.err.println("Erreur getChiffreAffairesEntretien: " + e.getMessage());
            }

            int caTotal = caCarburant + caEntretien;

            if (labelChiffreAffaires != null) {
                labelChiffreAffaires.setText(String.format("%,d Ar", caTotal));
            }

            if (labelNombreAchats != null) {
                try {
                    labelNombreAchats.setText(String.valueOf(achatDao.getNombreAchats()));
                } catch (Exception e) {
                    labelNombreAchats.setText("0");
                }
            }

            if (labelStockFaible != null) {
                try {
                    int nbStockFaible = produitDao.findStockFaible().size();
                    labelStockFaible.setText(String.valueOf(nbStockFaible));
                } catch (Exception e) {
                    labelStockFaible.setText("0");
                }
            }

        } catch (Exception e) {
            System.err.println("Erreur globale lors de l'initialisation du Dashboard : ");
            e.printStackTrace();
        }
    }
}