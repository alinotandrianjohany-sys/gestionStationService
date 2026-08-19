package com.stationservice.Controller.dossierEntretien;

import com.stationservice.Models.Entretien;
import com.stationservice.Models.Service;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;

public class NewEntretienController {

    @FXML private TextField txtNumEntretien;
    @FXML private TextField txtNomClient;
    @FXML private TextField txtImmatriculation;
    @FXML private ListView<ServiceSelection> listServices;
    @FXML private Label lblTotal;

    private Entretien newEntretien = null;
    private final ObservableList<ServiceSelection> itemsService = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Exemples de prestations de test
        Service s1 = new Service("Lavage", 20000);
        Service s2 = new Service("Gonflage", 2000);
        Service s3 = new Service("Vidange", 35000);
        Service s4 = new Service("Remplacement Filtre", 15000);

        for (Service s : new Service[]{s1, s2, s3, s4}) {
            ServiceSelection selection = new ServiceSelection(s);
            selection.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> recalculateTotal());
            itemsService.add(selection);
        }

        listServices.setItems(itemsService);
        listServices.setCellFactory(CheckBoxListCell.forListView(ServiceSelection::selectedProperty));
    }

    private void recalculateTotal() {
        int total = 0;
        for (ServiceSelection sel : itemsService) {
            if (sel.isSelected()) {
                total += sel.getService().getPrix();
            }
        }
        lblTotal.setText(total + " AR");
    }

    @FXML
    private void handleSave() {
        if (txtNumEntretien.getText().isBlank() || txtNomClient.getText().isBlank() || txtImmatriculation.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
            return;
        }

        newEntretien = new Entretien(
                txtNumEntretien.getText().trim(),
                txtImmatriculation.getText().trim(),
                txtNomClient.getText().trim()
        );

        for (ServiceSelection sel : itemsService) {
            if (sel.isSelected()) {
                newEntretien.ajouterService(sel.getService());
            }
        }

        txtNumEntretien.getScene().getWindow().hide();
    }

    @FXML
    private void handleCancel() {
        newEntretien = null;
        txtNumEntretien.getScene().getWindow().hide();
    }

    public Entretien getNewEntretien() {
        return newEntretien;
    }

    // Classe interne pour la gestion de la sélection des services dans la ListView
    public static class ServiceSelection {
        private final Service service;
        private final BooleanProperty selected = new SimpleBooleanProperty(false);

        public ServiceSelection(Service service) {
            this.service = service;
        }

        public Service getService() { return service; }
        public BooleanProperty selectedProperty() { return selected; }
        public boolean isSelected() { return selected.get(); }

        @Override
        public String toString() {
            return service.getNom() + " - " + service.getPrix() + " AR";
        }
    }
}