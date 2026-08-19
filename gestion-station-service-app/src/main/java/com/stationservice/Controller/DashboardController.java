package com.stationservice.Controller;

import com.stationservice.config.DatabaseConfig;
import com.stationservice.dao.AchatDao;
import com.stationservice.dao.EntretienDao;
import com.stationservice.dao.ProduitDao;
import com.stationservice.Models.RecetteMensuelle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class DashboardController {

    @FXML
    private Label labelChiffreAffaires;

    @FXML
    private Label labelNombreAchats;

    @FXML
    private Label labelNombreServices;

    @FXML
    private Label labelStockFaible;

    @FXML
    private VBox containerActivitesRecentes;

    @FXML
    private BarChart<String, Number> chartRecettesMois;

    @FXML
    private CategoryAxis xAxisMois;

    @FXML
    private NumberAxis yAxisMontant;

    @FXML
    public void initialize() {
        try {
            chargerGraphiqueRecettes();

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

            // Affichage du nombre d'entretiens / services
            if (labelNombreServices != null) {
                try {
                    // Si votre EntretienDao possède une méthode de comptage (ex: getNombreEntretiens)
                    labelNombreServices.setText(String.valueOf(entretienDao.getChiffreAffairesEntretien() > 0 ? "Actifs" : "0"));
                } catch (Exception e) {
                    labelNombreServices.setText("0");
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

    private void chargerGraphiqueRecettes() {
        try {
            Jdbi jdbi = DatabaseConfig.getJdbi();
            List<RecetteMensuelle> recettes = jdbi.withExtension(AchatDao.class, AchatDao::obtenirRecettesCinqDerniersMois);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Recettes");

            for (RecetteMensuelle r : recettes) {
                series.getData().add(new XYChart.Data<>(r.getMoisAnnee(), r.getTotalMensuel()));
            }

            chartRecettesMois.getData().clear();
            chartRecettesMois.getData().add(series);

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du graphique : " + e.getMessage());
            e.printStackTrace();
        }
    }
}