/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Controller.dossierClient;

/**
 * com.stationservice.Controller.dossierClient.ClientController;
 * @author DELL
 */
import com.stationservice.Models.Client;
import com.stationservice.dao.ClientDao;
import com.stationservice.config.DatabaseConfig;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ClientController {

    // --- Composants Table 1 (Tous les clients) ---
    @FXML
    private TableView<Client> tableTousClients;

    @FXML
    private TableColumn<Client, String> colNom_client;

    @FXML
    private TableColumn<Client, Integer> colTotal_paye;

    @FXML
    private TextField txtRecherche;

    // --- Composants Table 2 (Top 5 clients) ---
    @FXML
    private TableView<Client> tableTop5Clients;

    @FXML
    private TableColumn<Client, Integer> colRangClient;

    @FXML
    private TableColumn<Client, String> colTopNomClient;

    @FXML
    private TableColumn<Client, Integer> colTopTotalPaye;

    // --- Contextes de données ---
    private final ObservableList<Client> listeClients = FXCollections.observableArrayList();
    private final ObservableList<Client> listeTop5 = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuration de la Table 1 (Liste complète)
        colNom_client.setCellValueFactory(new PropertyValueFactory<>("nom_client"));
        colTotal_paye.setCellValueFactory(new PropertyValueFactory<>("total_paye"));
        tableTousClients.setItems(listeClients);

        // Configuration de la Table 2 (Top 5)
        // Rang automatique basé sur l'index de la ligne + 1
        colRangClient.setCellValueFactory(cellData -> 
            new ReadOnlyObjectWrapper<>(tableTop5Clients.getItems().indexOf(cellData.getValue()) + 1)
        );
        colTopNomClient.setCellValueFactory(new PropertyValueFactory<>("nom_client"));
        colTopTotalPaye.setCellValueFactory(new PropertyValueFactory<>("total_paye"));
        tableTop5Clients.setItems(listeTop5);

        // Chargement initial des données
        chargerTousLesClients();
        chargerTop5Clients();
    }

    /**
     * Charge la liste complète des clients
     */
    private void chargerTousLesClients() {
        try {
            Jdbi jdbi = DatabaseConfig.getJdbi();
            List<Client> resultats = jdbi.withExtension(ClientDao.class, ClientDao::obtenirTousLesClients);
            listeClients.setAll(resultats);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des clients : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Charge les 5 premiers clients selon le total payé
     */
    private void chargerTop5Clients() {
        try {
            Jdbi jdbi = DatabaseConfig.getJdbi();
            List<Client> resultats = jdbi.withExtension(ClientDao.class, ClientDao::obtenirTop5Clients);
            listeTop5.setAll(resultats);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du Top 5 : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Recherche un client par son nom lors du clic sur "Rechercher"
     */
    @FXML
    private void handleRechercher(ActionEvent event) {
        String recherche = txtRecherche.getText() != null ? txtRecherche.getText().trim() : "";

        if (recherche.isEmpty()) {
            chargerTousLesClients();
            return;
        }

        try {
            Jdbi jdbi = DatabaseConfig.getJdbi();
            List<Client> resultats = jdbi.withExtension(ClientDao.class, 
                dao -> dao.rechercherClientsParNom("%" + recherche + "%"));
            
            listeClients.setAll(resultats);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Réinitialise le champ texte et recharge la liste complète
     */
    @FXML
    private void handleAnnuler(ActionEvent event) {
        txtRecherche.clear();
        chargerTousLesClients();
    }
}
