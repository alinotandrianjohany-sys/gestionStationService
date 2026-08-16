/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Controller.dossierEntree;

/**
 *
 * @author DELL
 */

import com.stationservice.Models.Entree;
import com.stationservice.dao.EntreeDao;
//import com.stationservice.utils.DatabaseConfig; // Ajuste le package selon ta configuration JDBI

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import com.stationservice.config.DatabaseConfig;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
public class EntreeController {
    @FXML
    private Button btnNouvelleEntree;

    @FXML
    private TextField txtRecherche;

    @FXML
    private Label lblTotalEntrees;

    @FXML
    private TableView<Entree> tableEntree;

    @FXML
    private TableColumn<Entree, String> colNumEntree;

    @FXML
    private TableColumn<Entree, String> colNumProd;

    @FXML
    private TableColumn<Entree, Integer> colStockEntree;

    @FXML
    private TableColumn<Entree, LocalDateTime> colDateEntree;

    private final ObservableList<Entree> listeEntrees = FXCollections.observableArrayList();
    private FilteredList<Entree> listeFiltree;
    public EntreeDao _entreeDao = DatabaseConfig.getDao(EntreeDao.class);


    @FXML
    public void initialize() {
    // JavaFX va appeler getNumEntree(), getNumProd(), etc.
        colNumEntree.setCellValueFactory(new PropertyValueFactory<>("numEntree"));
        colNumProd.setCellValueFactory(new PropertyValueFactory<>("numProd"));
        colStockEntree.setCellValueFactory(new PropertyValueFactory<>("stockEntree"));
    colDateEntree.setCellValueFactory(new PropertyValueFactory<>("dateEntree"));

    chargerDonnees();
}

    /**
     * Méthode appelée par le bouton "+ Nouvelle Entrée" dans entree.fxml
     */
    @FXML
    private void handleNouvelleEntree(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dossierEntree/nouveauEntree.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nouvelle Entrée de Stock");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Rend la fenêtre modale
            stage.setResizable(false);
            
            // On affiche le dialogue et on attend sa fermeture
            stage.showAndWait();

            // Une fois la fenêtre fermée, on rafraîchit la liste des entrées
            chargerDonnees();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'ouverture de la fenêtre nouveau_entree.fxml : " + e.getMessage());
        }
    }

    /**
     * Charge toutes les entrées depuis la base de données via EntreeDao
     */
    private void chargerDonnees() {
        try {
            List<Entree> entreesBDD = _entreeDao.findAll();
            listeEntrees.setAll(entreesBDD);
            tableEntree.setItems(listeEntrees);
            mettreAJourCompteur();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des entrées : " + e.getMessage());
        }
    }

    private void mettreAJourCompteur() {
        if (lblTotalEntrees != null) {
            lblTotalEntrees.setText("Total : " + tableEntree.getItems().size() + " entrée(s)");
        }
    }
}
