package com.stationservice.Controller.dossierService;

import com.stationservice.Models.Service;
import com.stationservice.config.DatabaseConfig;
import com.stationservice.dao.ServiceDao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NouveauServiceController {

    @FXML private Label lblTitreModal;
    @FXML private Label txtMessage;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrix;

    private final ServiceDao serviceDao = DatabaseConfig.getDao(ServiceDao.class);
    private Service serviceEnCoursModif = null;

    public void chargerDataPourModification(Service service) {
        this.serviceEnCoursModif = service;
        if (lblTitreModal != null) lblTitreModal.setText("Modifier le Service");
        if (txtNom != null) txtNom.setText(service.getNom());
        if (txtPrix != null) txtPrix.setText(String.valueOf(service.getPrix()));
    }

    @FXML
    private void handleEnregistrer() {
        if (txtMessage != null) txtMessage.setText("");

        String nom = txtNom.getText().trim();
        String prixText = txtPrix.getText().trim();

        if (nom.isEmpty() || prixText.isEmpty()) {
            setMessage("Veuillez remplir tous les champs.");
            return;
        }

        try {
            int prix = Integer.parseInt(prixText);
            if (prix < 0) {
                setMessage("Le prix doit être positif.");
                return;
            }

            boolean success;
            if (serviceEnCoursModif == null) {
                String numServ = "SERV-" + (System.currentTimeMillis() % 100000000);
                Service service = new Service(numServ, nom, prix);
                success = serviceDao.insert(service);
            } else {
                serviceEnCoursModif.setNom(nom);
                serviceEnCoursModif.setPrix(prix);
                success = serviceDao.update(serviceEnCoursModif);
            }

            if (success) {
                closeWindow();
            } else {
                setMessage("Erreur lors de la sauvegarde.");
            }
        } catch (NumberFormatException e) {
            setMessage("Veuillez saisir un prix valide.");
        }
    }

    @FXML
    private void handleAnnuler() {
        closeWindow();
    }

    private void setMessage(String msg) {
        if (txtMessage != null) txtMessage.setText(msg);
    }

    private void closeWindow() {
        Stage stage = (Stage) txtNom.getScene().getWindow();
        stage.close();
    }
}