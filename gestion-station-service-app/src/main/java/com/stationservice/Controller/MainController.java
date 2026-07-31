package com.stationservice.Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;


public class MainController {
	@FXML
    private VBox contentArea;

    @FXML
    public void initialize() {
        // Code exécuté automatiquement au chargement de la vue
    }

    @FXML
    private void handleDashboard() {
        System.out.println("Clic sur Tableau de bord");
        // Plus tard : charger dashboard.fxml dans contentArea
    }

    @FXML
    private void handleProduits() {
        System.out.println("Clic sur Produits");
        // Plus tard : charger produits.fxml dans contentArea
    }

    @FXML
    private void handleEntretiens() {
        System.out.println("Clic sur Entretiens");
        // Plus tard : charger entretiens.fxml dans contentArea
    }
}
