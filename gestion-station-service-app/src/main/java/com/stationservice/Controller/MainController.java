package com.stationservice.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    public void initialize() {
        loadView("/fxml/dashboard.fxml");
    }

    @FXML
    private void handleDashboard() {
        loadView("/fxml/dashboard.fxml");
    }

    @FXML
    private void handleProduits() {
        loadView("/fxml/dossierProduit/produit.fxml");
    }

    @FXML
    private void handleAchat() {
        loadView("/fxml/dossierAchat/achat.fxml");
    }

    @FXML
    private void handleEntrees() {
        loadView("/fxml/dossierEntree/entree.fxml");
    }

    @FXML
    private void handleEntretiens() {
        loadView("/fxml/dossierEntretien/entretien.fxml");
    }

    @FXML
    private void handleServices() {
        loadView("/fxml/dossierService/entretien.fxml");
    }

    @FXML
    private void handleClient() {
        loadView("/fxml/dossierClient/client.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la vue : " + fxmlPath);
            e.printStackTrace();
        }
    }
}