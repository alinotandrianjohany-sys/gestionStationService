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
import com.stationservice.Controller.dossierEntree.ModifierEntreeController;
//import com.stationservice.utils.DatabaseConfig; // Ajuste le package selon ta configuration JDBI

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import com.stationservice.config.DatabaseConfig;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.io.IOException;
import java.util.Optional;
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

    @FXML private TableColumn<Entree, Void> colActions;

    private final ObservableList<Entree> listeEntrees = FXCollections.observableArrayList();
    private FilteredList<Entree> listeFiltree;
    public EntreeDao _entreeDao = DatabaseConfig.getDao(EntreeDao.class);


    @FXML
    public void initialize() {
        // IMPORTANT : Ces noms doivent correspondre EXACTEMENT aux champs/getters de la classe Entree.java
        // Exemple : "num_entr" fera appel à getNum_entr() dans Entree.java
        colNumEntree.setCellValueFactory(new PropertyValueFactory<>("num_entr"));
        colNumProd.setCellValueFactory(new PropertyValueFactory<>("num_prod"));
        colStockEntree.setCellValueFactory(new PropertyValueFactory<>("stock_entree"));
        colDateEntree.setCellValueFactory(new PropertyValueFactory<>("date_entree"));

        // Lier la liste au TableView
        tableEntree.setItems(listeEntrees);

        ajouterBoutonsActions();

        // Charger les données
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
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();

            // Rafraîchir la liste après la fermeture de la fenêtre
            chargerDonnees();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'ouverture de nouveauEntree.fxml : " + e.getMessage());
        }
    }

    private void ajouterBoutonsActions(){
        Callback<TableColumn<Entree, Void>, TableCell<Entree, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Entree, Void> call(final TableColumn<Entree, Void> param) {
                return new TableCell<>() {

                    private final Button btnEditer = new Button("Éditer");
                    private final Button btnSupprimer = new Button("Supprimer");
                    private final HBox container = new HBox(8, btnEditer, btnSupprimer);

                    {
                        // Alignement et styles des boutons
                        container.setAlignment(Pos.CENTER);
                        btnEditer.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand;");
                        btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");

                        // Action Modifier
                        btnEditer.setOnAction(event -> {
                            Entree entree = getTableView().getItems().get(getIndex());
                            handleModifierEntree(entree);
                        });

                        // Action Supprimer
                        btnSupprimer.setOnAction(event -> {
                            Entree entree = getTableView().getItems().get(getIndex());
                            handleSupprimerEntree(entree);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(container);
                        }
                    }
                };
            }
        };

        colActions.setCellFactory(cellFactory);
    }


    @FXML
    private void handleModifierEntree(Entree entree){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dossierEntree/modifierEntree.fxml"));
            Parent root = loader.load();

            ModifierEntreeController controlleur = loader.getController();
            controlleur.Initialise(entree);

            Stage stage = new Stage();
            stage.setTitle("Modification Entrée de Stock");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();

            // Rafraîchir la liste après la fermeture de la fenêtre
            chargerDonnees();

        } catch (IOException e){
            e.printStackTrace();
            System.err.println("Erreur lors de l'ouverture de nouveauEntree.fxml : " + e.getMessage());
        }
    }

    /**
     * Charge toutes les entrées depuis la base de données
     */
    private void chargerDonnees() {
        try {
            List<Entree> entreesBDD = _entreeDao.findAll();

            // Mise à jour propre de l'ObservableList
            listeEntrees.setAll(entreesBDD);

            // Rafraîchir l'affichage du tableau
            tableEntree.refresh();

            mettreAJourCompteur();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des entrées : " + e.getMessage());
        }
    }

    private void handleSupprimerEntree(Entree entree) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'entrée N° " + entree.getNum_entr() + " ?");
        alert.setContentText("Cette action réajustera également le stock du produit associé.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Suppression via le DAO
                _entreeDao.delete(entree.getNum_entr());

                // Supprimer directement de la liste observable (évite un rechargement complet)
                listeEntrees.remove(entree);

                //mettre a jour le compteur
                mettreAJourCompteur();
            } catch (Exception e) {
                e.printStackTrace();
                Alert errAlert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression : " + e.getMessage());
                errAlert.show();
            }
        }
    }

    private void mettreAJourCompteur() {
        if (lblTotalEntrees != null) {
            lblTotalEntrees.setText("Total : " + listeEntrees.size() + " entrée(s)");
        }
    }


}