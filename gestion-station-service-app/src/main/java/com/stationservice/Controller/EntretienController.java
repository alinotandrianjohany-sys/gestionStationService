package com.stationservice.Controller;

import com.stationservice.Models.Entretien;
import com.stationservice.Models.Service;
import com.stationservice.Utilitaires.PdfGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDateTime;

public class EntretienController {

    @FXML private TableView<Entretien> tableEntretiens;
    @FXML private TableColumn<Entretien, String> colNumEntr;
    @FXML private TableColumn<Entretien, String> colImmatriculation;
    @FXML private TableColumn<Entretien, String> colNomClient;
    @FXML private TableColumn<Entretien, LocalDateTime> colDate;
    @FXML private TableColumn<Entretien, Integer> colPrixTotal;

    private final ObservableList<Entretien> listeEntretiens = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Liaison des colonnes aux getters de Entretien.java
        colNumEntr.setCellValueFactory(new PropertyValueFactory<>("numEntretien"));
        colImmatriculation.setCellValueFactory(new PropertyValueFactory<>("immatriculationVoiture"));
        colNomClient.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateEntretien"));
        colPrixTotal.setCellValueFactory(new PropertyValueFactory<>("prixEntretien"));

        chargerDonnees();
    }

    private void chargerDonnees() {
        // Exemples de test conformes à ton sujet
        Entretien e1 = new Entretien("ENT001", "3333 FE", "RAKOTO Bernard");
        e1.ajouterService(new Service("Lavage", 20000));
        e1.ajouterService(new Service("Gonflage", 2000));

        Entretien e2 = new Entretien("ENT002", "5678 TAF", "Rabe Marie");
        e2.ajouterService(new Service("Vidange", 35000));

        listeEntretiens.setAll(e1, e2);
        tableEntretiens.setItems(listeEntretiens);
    }

    @FXML
    private void handleNouveau() {
        System.out.println("Ouverture du formulaire de création d'entretien");
        // TODO: Prochaine étape CRUD - Formulaire d'ajout/choix des services
    }

    @FXML
    private void handleImprimerRecu() {
        Entretien entretienSelectionne = tableEntretiens.getSelectionModel().getSelectedItem();

        if (entretienSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un entretien dans le tableau pour imprimer le reçu.");
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le reçu PDF");
        fileChooser.setInitialFileName("Recu_" + entretienSelectionne.getNumEntretien() + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf"));

        File file = fileChooser.showSaveDialog(tableEntretiens.getScene().getWindow());

        if (file != null) {
            try {
                PdfGenerator.genererRecuEntretien(entretienSelectionne, file.getAbsolutePath());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Le reçu a été généré avec succès !");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}