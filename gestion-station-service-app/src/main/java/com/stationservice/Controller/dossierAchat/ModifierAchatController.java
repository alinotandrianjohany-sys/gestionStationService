/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Controller.dossierAchat;

/**
 * com.stationservice.Controller.dossierAchat.ModifierAchatController
 * @author DELL
 */

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import com.stationservice.dao.ProduitDao;
import com.stationservice.dao.AchatDao;
import com.stationservice.Models.Achat;
import com.stationservice.Models.Produit;
import com.stationservice.config.DatabaseConfig;

import org.postgresql.util.PSQLException;
import org.jdbi.v3.core.JdbiException;

import java.util.Optional;

public class ModifierAchatController {

    @FXML private TextField txtNumProduit;
    @FXML private TextField txtNomClient;
    @FXML private TextField txtChoix;
    @FXML private Button BtnEnregistrer;
    @FXML private ComboBox<String> ComboTypeAchat;
    @FXML private Label txtMessage;

    private ProduitDao produitDao = DatabaseConfig.getDao(ProduitDao.class);
    private AchatDao achatDao = DatabaseConfig.getDao(AchatDao.class);

    // Stocke l'achat initialement transmis pour l'édition
    private Achat achatActuel;

    /**
     * Initialise la vue avec les données de l'achat sélectionné.
     */
    public void Initialise(Achat achat) {
        this.achatActuel = achat;

        // Informations non modifiables
        txtNumProduit.setText(achat.getNum_prod());
        txtNumProduit.setEditable(false);

        // Informations modifiables
        txtNomClient.setText(achat.getNom_client());
        txtChoix.setText(String.valueOf(achat.getNbr_litre()));

        // Configuration de la ComboBox
        ComboTypeAchat.getItems().clear();
        ComboTypeAchat.getItems().addAll("Litre", "Ariary");
        ComboTypeAchat.setValue("Litre");
    }

    @FXML
    public void Btn_effectuerModificationAchat() {
        if (achatActuel == null) {
            afficherMessage("Aucun achat chargé pour la modification.");
            return;
        }

        String regexNom = "^[\\p{L}\\s\\-]+$";

        if (txtNomClient.getText().trim().isEmpty()) {
            afficherMessage("Le nom du client est vide");
            return;
        }

        if (!txtNomClient.getText().trim().matches(regexNom)) {
            afficherMessage("Nom du client invalide");
            return;
        }

        // Récupération du produit en BDD pour recalculer avec le prix et le stock disponible
        Optional<Produit> produitOpt = produitDao.findById(achatActuel.getNum_prod());
        if (produitOpt.isEmpty()) {
            afficherMessage("Produit associé introuvable dans la base.");
            return;
        }
        Produit produit = produitOpt.get();

        if (ComboTypeAchat.getValue() == null || ComboTypeAchat.getValue().trim().isEmpty()) {
            afficherMessage("Type Achat : Litre ou Ariary.");
            return;
        }

        String typeChoisi = ComboTypeAchat.getValue().trim();
        String regexEntier = "^[0-9]+$";
        String regexDecimal = "^[0-9]+([.,][0-9]+)?$";

        double nouvelleQuantite = 0.0;
        int nouveauMontantPaye = 0;

        if ("Ariary".equals(typeChoisi)) {
            String val = txtChoix.getText().trim();

            if (val.isEmpty()) {
                afficherMessage("Le montant à payer est vide");
                return;
            }

            if (!val.matches(regexEntier)) {
                afficherMessage("Veuillez saisir un montant valide en chiffres.");
                return;
            }

            nouveauMontantPaye = Integer.parseInt(val);
            if (nouveauMontantPaye <= 0) {
                afficherMessage("Le prix doit être supérieur à 0");
                return;
            }

            nouvelleQuantite = (double) nouveauMontantPaye / produit.getPrix_litre_prod();

        } else if ("Litre".equals(typeChoisi)) {
            String saisie = txtChoix.getText().trim();

            if (saisie.isEmpty()) {
                afficherMessage("La quantité est vide");
                return;
            }

            if (!saisie.matches(regexDecimal)) {
                afficherMessage("Veuillez saisir une quantité valide (ex: 2.5 ou 10)");
                return;
            }

            nouvelleQuantite = Double.parseDouble(saisie.replace(",", "."));
            if (nouvelleQuantite <= 0) {
                afficherMessage("La quantité doit être supérieure à 0");
                return;
            }

            double calcule = nouvelleQuantite * produit.getPrix_litre_prod();
            nouveauMontantPaye = (int) Math.round(calcule);
        }

        // Vérification de la capacité du stock :
        // Le stock réel utilisable est (stock actuel + ancienne quantité de cet achat)
        double stockDisponibleTotal = produit.getStock() + achatActuel.getNbr_litre();
        if (nouvelleQuantite > stockDisponibleTotal) {
            afficherMessage("Le stock de produit est insuffisant");
            return;
        }

        // Mise à jour de l'objet Achat local
        achatActuel.setNom_client(txtNomClient.getText().trim());
        achatActuel.setNbr_litre(nouvelleQuantite);
        achatActuel.setMontant_paye_achat(nouveauMontantPaye);

        try {
            // Transaction DAO pour maj d'achat et réajustement du stock produit
            boolean estModifie = achatDao.modificationAchat(achatActuel);

            if (estModifie) {
                txtMessage.setText("Modification enregistrée avec succès !");
                txtMessage.setStyle("-fx-text-fill: green;");
                fermetureFenetre();
            } else {
                afficherMessage("Erreur lors de la modification de l'achat.");
            }
        } catch (JdbiException e) {
            if (e.getCause() instanceof PSQLException psqlException && "23505".equals(psqlException.getSQLState())) {
                afficherMessage("Erreur de doublon : Transaction en conflit.");
            } else {
                afficherMessage("Erreur de base de données : " + e.getMessage());
            }
        }
    }

    private void afficherMessage(String message) {
        txtMessage.setText(message);
        txtMessage.setStyle("-fx-text-fill: red;");
    }

    private void fermetureFenetre() {
        Stage stage = (Stage) BtnEnregistrer.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}