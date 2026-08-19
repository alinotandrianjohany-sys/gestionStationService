package com.stationservice.Controller.dossierEntretien;

import com.stationservice.Models.Entretien;
import com.stationservice.dao.EntretienDao;
import com.stationservice.config.DatabaseConfig;
import com.stationservice.Utilitaires.PdfGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class EntretienController {

    @FXML private TableView<Entretien> tableEntretien;
    @FXML private TableColumn<Entretien, String> colNumEntr;
    @FXML private TableColumn<Entretien, String> colImmatriculation;
    @FXML private TableColumn<Entretien, String> colNomClient;
    @FXML private TableColumn<Entretien, LocalDateTime> colDate;
    @FXML private TableColumn<Entretien, Integer> colPrix;

    private final EntretienDao entretienDao = DatabaseConfig.getDao(EntretienDao.class);
    private final ObservableList<Entretien> entretienList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (colNumEntr != null) colNumEntr.setCellValueFactory(new PropertyValueFactory<>("numEntr"));
        if (colImmatriculation != null) colImmatriculation.setCellValueFactory(new PropertyValueFactory<>("immatriculationVoiture"));
        if (colNomClient != null) colNomClient.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        if (colDate != null) colDate.setCellValueFactory(new PropertyValueFactory<>("dateEntretien"));
        if (colPrix != null) colPrix.setCellValueFactory(new PropertyValueFactory<>("prixEntretien"));

        chargerEntretiens();
    }

    public void chargerEntretiens() {
        entretienList.clear();
        List<Entretien> liste = entretienDao.findAll();
        if (liste != null) {
            entretienList.addAll(liste);
        }
        if (tableEntretien != null) {
            tableEntretien.setItems(entretienList);
            tableEntretien.refresh();
        }
    }

    @FXML private void handleNouvelEntretien() { ouvrirModalFormulaire(null); }
    @FXML private void handleNew() { handleNouvelEntretien(); }

    @FXML
    private void handleModifierEntretien() {
        Entretien selection = tableEntretien != null ? tableEntretien.getSelectionModel().getSelectedItem() : null;
        if (selection != null) {
            ouvrirModalFormulaire(selection);
        } else {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un entretien à modifier.", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void handleSupprimerEntretien() {
        Entretien selection = tableEntretien != null ? tableEntretien.getSelectionModel().getSelectedItem() : null;
        if (selection != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cet entretien ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                entretienDao.delete(selection.getNumEntr());
                chargerEntretiens();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un entretien à supprimer.", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void handleImprimerRecu() {
        Entretien selection = tableEntretien != null ? tableEntretien.getSelectionModel().getSelectedItem() : null;
        if (selection != null) {
            // Ouvre un sélecteur de fichier pour choisir où enregistrer le PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le reçu PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf"));
            fileChooser.setInitialFileName("Recu_" + selection.getNumEntr() + ".pdf");

            File file = fileChooser.showSaveDialog(tableEntretien.getScene().getWindow());

            if (file != null) {
                try {
                    PdfGenerator.genererRecuEntretien(selection, file.getAbsolutePath());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Reçu PDF généré avec succès :\n" + file.getAbsolutePath(), ButtonType.OK);
                    alert.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la génération du PDF :\n" + e.getMessage(), ButtonType.OK);
                    alert.showAndWait();
                }
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un entretien à imprimer.", ButtonType.OK).showAndWait();
        }
    }

    private void ouvrirModalFormulaire(Entretien entretien) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dossierEntretien/nouveauEntretien.fxml"));
            Parent root = loader.load();

            if (entretien != null) {
                NouveauEntretienController controller = loader.getController();
                controller.chargerDataPourModification(entretien);
            }

            Stage stage = new Stage();
            stage.setTitle(entretien == null ? "Nouvel Entretien" : "Modifier Entretien");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            chargerEntretiens();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}