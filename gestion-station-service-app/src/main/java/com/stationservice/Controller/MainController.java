package com.stationservice.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML private AnchorPane contentArea;
    @FXML private Label lblHeaderTitle;
    @FXML private Label lblHeaderSubtitle;

    @FXML private Button btnDashboard;
    @FXML private Button btnProduits;
    @FXML private Button btnAchats;
    @FXML private Button btnEntrees;
    @FXML private Button btnEntretiens;

    @FXML
    public void initialize() {
        handleDashboard();
    }

    @FXML
    private void handleDashboard() {
        updateHeader("Tableau de Bord", "Vue d'ensemble et statistiques de la station");
        setActiveButton(btnDashboard);
        loadView("/fxml/dashboard.fxml");
    }

    @FXML
    private void handleProduits() {
        updateHeader("Produits & Stocks", "Gestion des carburants et des prix unitaires");
        setActiveButton(btnProduits);
        loadView("/fxml/dossierProduit/produit.fxml");
    }

    @FXML
    private void handleAchats() {
        updateHeader("Gestion des Achats", "Enregistrement des ventes clients et reçus");
        setActiveButton(btnAchats);
        loadView("/fxml/dossierAchat/achat.fxml");
    }

    @FXML
    private void handleEntrees() {
        updateHeader("Entrées en Stock", "Historique des réapprovisionnements en carburant");
        setActiveButton(btnEntrees);
        loadView("/fxml/dossierEntree/entree.fxml");
    }

    @FXML
    private void handleEntretiens() {
        updateHeader("Services & Entretiens", "Suivi des interventions techniques et facturation");
        setActiveButton(btnEntretiens);
        loadView("/fxml/dossierEntretien/entretien.fxml");
    }
    
    @FXML
    private void handleClient(){
        loadView("/fxml/dossierClient/client.fxml");
    }

    private void updateHeader(String title, String subtitle) {
        lblHeaderTitle.setText(title);
        lblHeaderSubtitle.setText(subtitle);
    }

    private void setActiveButton(Button activeButton) {
        Button[] buttons = {btnDashboard, btnProduits, btnAchats, btnEntrees, btnEntretiens};
        for (Button btn : buttons) {
            btn.getStyleClass().remove("menu-button-active");
            if (!btn.getStyleClass().contains("menu-button")) {
                btn.getStyleClass().add("menu-button");
            }
        }
        activeButton.getStyleClass().add("menu-button-active");
    }

    private void loadView(String fxmlPath) {
        try {
            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                System.err.println("Fichier FXML introuvable : " + fxmlPath);
                return;
            }
            Node view = FXMLLoader.load(resource);

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}