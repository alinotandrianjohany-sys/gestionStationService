/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Controller.dossierEntree;
//com.stationservice.Controller.dossierEntree.ModifierEntreeController;
/**
 *
 * @author DELL
 */


import org.jdbi.v3.core.Jdbi;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import com.stationservice.Models.Entree;
import com.stationservice.Models.Produit;
import com.stationservice.config.DatabaseConfig;
import com.stationservice.dao.EntreeDao;
import com.stationservice.dao.ProduitDao;
import javafx.scene.Node;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ModifierEntreeController {
    @FXML private Label lblDate;
    @FXML private TextField txtNumEntree;
    @FXML private TextField lblProduit;
    @FXML private TextField txtStockEntree;
    @FXML private Label lblError;
    @FXML private EntreeDao _entreeDao = DatabaseConfig.getDao(EntreeDao.class);
    @FXML private ProduitDao _produitDao = DatabaseConfig.getDao(ProduitDao.class);
    private Produit _produit ;
    public void Initialise(Entree entree){
        initialisationAffichage(entree);
    }
    
    @FXML
    private void handleValiderEntree(ActionEvent event){
        // 1. Nettoyage des espaces superflus (trim)
        String numEntree = txtNumEntree.getText() != null ? txtNumEntree.getText().trim() : "";
        String numProd = lblProduit.getText() != null ? lblProduit.getText().trim() : ""; // ou txtNumProd.getText().trim()
        String stockStr = txtStockEntree.getText() != null ? txtStockEntree.getText().trim() : "";

        // 2. Vérification des champs vides
        if (numEntree.isEmpty() || numProd == null || stockStr.isEmpty()) {
            afficherAlerte("Validation", "Tous les champs requis doivent être remplis.");
            return;
        }

        // 3. Regex pour num_entr (Ex: autorise lettres, chiffres, tirets, max 50 caractères)
        if (!numEntree.matches("^[a-zA-Z0-9_-]{1,50}$")) {
            afficherAlerte("Format invalide", "Le numéro d'entrée doit contenir uniquement des lettres, chiffres, '-' ou '_', sans dépasser 50 caractères.");
            return;
        }

        // 4. Validation et conversion de la quantité (stock_entree)
        int stockEntree;
        try {
            stockEntree = Integer.parseInt(stockStr);

            // Respect de la contrainte CHECK (stock_entree > 0) de PostgreSQL
            if (stockEntree <= 0) {
                afficherAlerte("Valeur incorrecte", "La quantité ajoutée doit être strictement supérieure à 0.");
                return;
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Format invalide", "La quantité entrée doit être un nombre entier valide.");
            return;
        }

        // 5. Sauvegarde sécurisée (JDBI protège contre les injections SQL grâce aux requêtes préparées)
        try {
            Entree nouvelleEntree = new Entree(numEntree, numProd, stockEntree);
            boolean resultat = _entreeDao.modificationEntre(nouvelleEntree);
            if (resultat){
                afficherAlerte("Modification", "Modification effectue avec succees");    
            } else {
                afficherAlerte("Modification", "Modification non effectue");
            }

            fermerFenetre(event);
        } catch (Exception e) {
            // Capture des violations de contraintes BDD (ex: Clé primaire déjà existante ou clé étrangère introuvable)
            afficherAlerte("Erreur Base de Données", "Impossible d'enregistrer l'entrée. Le numéro d'entrée existe déjà ou le produit sélectionné est invalide.");
            e.printStackTrace();
        }
    }
    
    private void initialisationAffichage(Entree entree){
        
        lblDate.setText(entree.getDate_entree().toString().replace("T", " "));
        txtNumEntree.setText(entree.getNum_entr());
        lblProduit.setText(entree.getNum_prod());
        txtStockEntree.setText(String.valueOf(entree.getStock_entree()));
   
    }
    
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Ferme la fenêtre associée à l'événement déclenché
     */
    private void fermerFenetre(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
}
