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
        // Code exécuté automatiquement au chargement de la vue
        loadView("/fxml/dashboard.fxml");
    }

    @FXML
    private void handleDashboard() {
        loadView("/fxml/dashboard.fxml");
        // Plus tard : charger dashboard.fxml dans contentArea
    }

    @FXML
    private void handleProduits() {
        loadView("/fxml/dossierProduit/produit.fxml");
        // Plus tard : charger produits.fxml dans contentArea
    }

    @FXML
    private void handleEntretiens() {
        loadView("/fxml/dossierAchat/achat.fxml");
    }
    
    private void loadView(String fxmlPath){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            //remplacement du contenue centrale par une ue
            contentArea.getChildren().setAll(view);
        } catch (IOException e){
            
               System.err.println("Erreur lors du charegement de vue");
               e.printStackTrace();
        }
    }
}
