/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Controller;

import com.stationservice.Controller.dossierProduit.ModifierProduit;
import com.stationservice.dao.ProduitDao;
import com.stationservice.Models.Produit;
import com.stationservice.config.DatabaseConfig;
import java.util.Optional;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;


/**
 *
 * @author DELL
 */
public class ProduitController {
    //les listes des produit
    private ProduitDao produitDao = DatabaseConfig.getDao(ProduitDao.class);
    
    //affichage des deonnes
    @FXML private TableView<Produit> tableProduits;
    @FXML private TableColumn<Produit, String> col_num_prod;
    @FXML private TableColumn<Produit, String> col_design;
    @FXML private TableColumn<Produit, Integer> col_stock;
    @FXML private TableColumn<Produit, Integer> col_prix_litre_prod;
    @FXML private TableColumn<Produit, Void> colActions;
    
    private ObservableList<Produit> observableList = FXCollections.observableArrayList(); 
    
    @FXML
    public void initialize(){
        LierLesColonnesAuxAtributProduit();
        ajouterBoutonsAction();
        chargerDonnees();
    }
    
    @FXML
    private void AfficherFenetreAjoutProduit(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DossierProduit/AjoutProduit.fxml"));
            Parent root = loader.load();

            Stage popUpAjoutProduit = new Stage();
            popUpAjoutProduit.setTitle("Ajouter un Produit");
            popUpAjoutProduit.setScene(new Scene(root));
            
            // Empêche de cliquer sur la fenêtre principale tant que la pop-up est ouverte
            popUpAjoutProduit.initModality(Modality.APPLICATION_MODAL);
            
            popUpAjoutProduit.showAndWait();
            
            chargerDonnees();

        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture de la fenêtre d'ajout : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    
    //Lier les colonnes aux attributs de la classe Produit
    private void LierLesColonnesAuxAtributProduit(){
        col_num_prod.setCellValueFactory(new PropertyValueFactory<>("num_prod"));
        col_design.setCellValueFactory(new PropertyValueFactory<>("design"));
        col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        col_prix_litre_prod.setCellValueFactory(new PropertyValueFactory<>("prix_litre_prod"));
    }
    
    private void chargerDonnees(){
        //appel de la base de donne
        List<Produit> listBdd = rechargerProduitsDepuisBD();
        
        //conversion en obserable liste pour javaFX
        observableList = FXCollections.observableArrayList(listBdd);
        
        //injection des donnees dans la table
        tableProduits.setItems(observableList);
    }
    
    private List<Produit> rechargerProduitsDepuisBD(){
        return produitDao.findAll();
    }
    
    
    private void ajouterBoutonsAction() {
        Callback<TableColumn<Produit, Void>, TableCell<Produit, Void>> cellFactory = param -> new TableCell<>() {
            
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox conteneurBoutons = new HBox(8, btnModifier, btnSupprimer);

            {
                // Style rapide des boutons (optionnel)
                btnModifier.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                // Event : Modifier
                btnModifier.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    handleModifierProduit(produit);
                });

                // Event : Supprimer
                btnSupprimer.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    handleSupprimerProduit(produit);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(conteneurBoutons);
                }
            }
        };

        colActions.setCellFactory(cellFactory);
    }
    
    private void handleSupprimerProduit(Produit produit) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer le produit : " + produit.getDesign() + " ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // 1. Supprimer en base de données via votre DAO/JDBI
            produitDao.delete(produit.getNum_prod());
            
            // 2. Retirer de la liste affichée dans l'interface
            observableList.remove(produit);
        } else {
            Alert ale = new Alert(Alert.AlertType.INFORMATION);
            ale.setContentText("Suppression non effectue : " + produit.getDesign() + " ?");
            ale.show();
        }
    }
    
    private void handleModifierProduit(Produit produit) {
        // Ouvrir une fenêtre d'édition pré-remplie avec les données de 'produit'
        System.out.println("Modification du produit ID : " + produit.getNum_prod());
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DossierProduit/modifierProduit.fxml"));
            Parent root = loader.load();
            
            ModifierProduit controlleur = loader.getController();
            controlleur.initialize(produit);
            Stage popUpModifProduit = new Stage();
            popUpModifProduit.setTitle("Modifier un Produit");
            popUpModifProduit.setScene(new Scene(root));
            
            // Empêche de cliquer sur la fenêtre principale tant que la pop-up est ouverte
            //popUpModifProduit.initModality(Modality.APPLICATION_MODAL);
            
            popUpModifProduit.showAndWait();
            
            chargerDonnees();

        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture de la fenêtre d'ajout : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
