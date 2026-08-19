/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Controller.dossierEntree;
//com.stationservice.Controller.dossierEntree.NouveauEntreeController
/**
 *
 * @author DELL
 */


import javafx.scene.control.Button;
import javafx.stage.Stage;

import com.stationservice.dao.ProduitDao;
import com.stationservice.dao.AchatDao;
import com.stationservice.dao.EntreeDao;
import com.stationservice.Models.Achat;
import com.stationservice.Models.Produit;
import com.stationservice.Models.Entree;
import com.stationservice.config.DatabaseConfig;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jdbi.v3.core.Jdbi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class NouveauEntreeController {
    @FXML private Label lblDate;
    @FXML private TextField txtNumEntree;
    @FXML private ComboBox<Produit> cbProduit;
    @FXML private TextField txtStockEntree;
    @FXML private Label lblError;
    @FXML private EntreeDao _entreeDao = DatabaseConfig.getDao(EntreeDao.class);
    @FXML private ProduitDao _produitDao = DatabaseConfig.getDao(ProduitDao.class);

    // REGEX : Uniquement des nombres positifs (ex: 50, 100.5, 250.75)
    private static final String QUANTITE_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";
    
    @FXML
    public void initialize() {
        // Initialiser la connexion JDBI

        // 1. Affichage de la date courante en haut du formulaire (format jour mois année)
        LocalDate dateAujourdhui = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
        lblDate.setText("Date : " + dateAujourdhui.format(formatter));

        // 2. Charger les produits dans le ComboBox
        chargerProduits();

        // 3. Générer le numéro d'entrée automatique : entree-{(nombre d'entrees BD)+1}
        genererNumEntree();
    }

    private void chargerProduits() {
        List<Produit> produits = _produitDao.findAll();
        
        cbProduit.getItems().addAll(produits);

        // Personnaliser l'affichage dans le ComboBox (ex: P001 - Essence)
        cbProduit.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Produit p) {
                return p != null ? p.getNum_prod() + " - " + p.getDesign(): "";
            }

            @Override
            public Produit fromString(String string) {
                return null;
            }
        });
    }

    private void genererNumEntree() {
        int maxNumero = _entreeDao.findMaxNumeroEntree();
        int nouveauNumero = maxNumero + 1;

        txtNumEntree.setText("entree-" + nouveauNumero);
    }

    @FXML
    private void handleValiderEntree() {
        lblError.setText("");
        txtStockEntree.getStyleClass().remove("text-field-error");

        Produit produitSelectionne = cbProduit.getValue();
        String quantiteTexte = txtStockEntree.getText().trim();

        // --- VALIDATIONS ---
        if (produitSelectionne == null) {
            lblError.setText("Veuillez sélectionner un produit dans le menu.");
            return;
        }

        // --- VALIDATION REGEX DU STOCK ---
        if (!quantiteTexte.matches(QUANTITE_REGEX)) {
            txtStockEntree.getStyleClass().add("text-field-error");
            lblError.setText("Saisie invalide ! Veuillez entrer une quantité positive (ex: 100 ou 50.5).");
            return;
        }

        double quantite = Double.parseDouble(quantiteTexte);
        if (quantite <= 0) {
            txtStockEntree.getStyleClass().add("text-field-error");
            lblError.setText("La quantité doit être supérieure à 0.");
            return;
        }

        // --- ENREGISTREMENT BD ---
        String numEntree = txtNumEntree.getText();
        
        Entree _entree = new Entree(numEntree, produitSelectionne.getNum_prod(), Integer.parseInt(quantiteTexte) );
        
        boolean succes = _entreeDao.insertEntreeEtModificationStock(_entree);
                

        if (succes) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Entrée de stock enregistrée avec succès !", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();

            // Réinitialiser les champs et régénérer le prochain ID
            txtStockEntree.clear();
            cbProduit.getSelectionModel().clearSelection();
            genererNumEntree();
        } else {
            lblError.setText("Erreur lors de l'enregistrement dans la base de données.");
        }
    }
    
}
