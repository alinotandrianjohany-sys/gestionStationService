/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Controller.dossierAchat;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import com.stationservice.dao.ProduitDao;
import com.stationservice.dao.AchatDao;
import com.stationservice.Models.Achat;
import com.stationservice.Models.Produit;
import com.stationservice.config.DatabaseConfig;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.ComboBox;

import org.postgresql.util.PSQLException;
import org.jdbi.v3.core.JdbiException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 *
 * @author DELL
 */
public class NouveauAchat {
    @FXML private TextField txtNumProduit;
    @FXML private TextField txtNomClient;
    @FXML private TextField txtChoix;
    @FXML private Button BtnEnregistrer;
    @FXML private ComboBox<Produit> comboProduit;
    @FXML private ComboBox ComboTypeAchat;
    @FXML private Label txtMessage;

    private ProduitDao produitDao = DatabaseConfig.getDao(ProduitDao.class);
    private AchatDao achatDao = DatabaseConfig.getDao(AchatDao.class);

    public void initialize() {
        ComboTypeAchat.getItems().addAll("Ariary", "Litre");
        ComboTypeAchat.setValue("Litre");
        chargerProduits();
    }

    private void chargerProduits() {
        List<Produit> listeProduits = produitDao.findAll();
        comboProduit.getItems().clear();
        comboProduit.getItems().addAll(listeProduits);
    }

    @FXML
    private void Btn_effectuerNouveauAchat() {
        String regexNom = "^[\\p{L}\\s\\-]+$";

        if (txtNomClient.getText().trim().isEmpty()) {
            afficherMessage("Le nom du client est vide");
            return;
        }

        if (!txtNomClient.getText().trim().matches(regexNom)) {
            afficherMessage("Nom du client invalide");
            return;
        }

        boolean estAjoute = false;

        Produit produitSelectionne = comboProduit.getValue();
        if (produitSelectionne == null) {
            txtMessage.setText("Veuillez sélectionner un produit.");
            return;
        }

        if (ComboTypeAchat.getValue() == null || ComboTypeAchat.getValue().toString().trim().isEmpty()) {
            afficherMessage("Type Achat : Litre ou Montant.");
            return;
        }

        String typeChoisie = ComboTypeAchat.getValue().toString().trim();
        String regexEntier = "^[0-9]+$";
        String regexDecimal = "^[0-9]+([.,][0-9]+)?$";

        if (typeChoisie.equals("Ariary")) {
            String val = txtChoix.getText().trim();

            if (val.isEmpty()) {
                afficherMessage("Le montant à payer est vide");
                return;
            }

            if (!val.matches(regexEntier)) {
                afficherMessage("Veuillez saisir un montant valide en chiffres.");
                return;
            }

            int prix = Integer.parseInt(val);
            if (prix <= 0) {
                afficherMessage("Le prix doit être supérieur à 0");
                return;
            }

            double quantite = (double) prix / produitSelectionne.getPrix_litre_prod();

            if (quantite > produitSelectionne.getStock()) {
                afficherMessage("Désolé, le stock est insuffisant...");
                return;
            }

            try {
                Achat newAchat = new Achat(genererNumEntree(), produitSelectionne.getNum_prod(), txtNomClient.getText().trim(), quantite, prix);
                estAjoute = achatDao.enregistrerVenteEtMettreAJourStock(newAchat);

                if (estAjoute) {
                    txtMessage.setText("Merci de votre confiance, à bientôt !");
                    txtMessage.setStyle("-fx-text-fill: green;");
                    supprimerChamps();
                    fermetureFenetre();
                } else {
                    afficherMessage("Erreur lors de l'ajout de l'achat");
                }
            } catch (JdbiException e) {
                if (e.getCause() instanceof PSQLException psqlException && "23505".equals(psqlException.getSQLState())) {
                    afficherMessage("Erreur de doublon : L'achat existe déjà !");
                }
            }

        } else if (typeChoisie.equals("Litre")) {
            String saisie = txtChoix.getText().trim();

            if (saisie.isEmpty()) {
                afficherMessage("La quantité est vide");
                return;
            }

            if (!saisie.matches(regexDecimal)) {
                afficherMessage("Veuillez saisir une quantité valide (ex: 2.5 ou 10)");
                return;
            }

            double quantite = Double.parseDouble(saisie.replace(",", "."));
            if (quantite <= 0) {
                afficherMessage("La quantité doit être supérieure à 0");
                return;
            }

            if (quantite > produitSelectionne.getStock()) {
                afficherMessage("Le stock de produit est insuffisant");
                return;
            }

            double calcule = quantite * produitSelectionne.getPrix_litre_prod();
            int montantAPayer = (int) Math.round(calcule);

            try {
                Achat newAchat = new Achat(genererNumEntree(), produitSelectionne.getNum_prod(), txtNomClient.getText().trim(), quantite, montantAPayer);
                estAjoute = achatDao.enregistrerVenteEtMettreAJourStock(newAchat);

                if (estAjoute) {
                    txtMessage.setText("Merci de votre confiance, à bientôt !");
                    txtMessage.setStyle("-fx-text-fill: green;");
                    supprimerChamps();
                    fermetureFenetre();
                } else {
                    afficherMessage("Erreur lors de l'ajout de l'achat");
                }
            } catch (JdbiException e) {
                if (e.getCause() instanceof PSQLException psqlException && "23505".equals(psqlException.getSQLState())) {
                    afficherMessage("Erreur de doublon : L'achat existe déjà !");
                }
            }
        }
    }

    private void supprimerChamps() {
        if (txtNumProduit != null) txtNumProduit.setText("");
        txtNomClient.setText("");
        txtChoix.setText("");
    }

    private void afficherMessage(String message) {
        txtMessage.setText(message);
        txtMessage.setStyle("-fx-text-fill: red;");
    }

    private void fermetureFenetre() {
        Stage stage = (Stage) BtnEnregistrer.getScene().getWindow();
        stage.close();
    }

    private String genererNumEntree() {
        int totalEntrees = achatDao.getNombreAchats();
        int nouveauNumero = totalEntrees + 2;

        return "achat-" + nouveauNumero;
    }
}