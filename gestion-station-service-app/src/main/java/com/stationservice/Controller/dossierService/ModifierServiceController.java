package com.stationservice.Controller.dossierService;

import com.stationservice.Models.Service;
import com.stationservice.dao.ServiceDao;
import com.stationservice.config.DatabaseConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.postgresql.util.PSQLException;

public class ModifierServiceController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrix;
    @FXML private Label txtMessage;

    private final ServiceDao daoService = DatabaseConfig.getDao(ServiceDao.class);
    private Service serviceAModifier;

    public void initData(Service service) {
        this.serviceAModifier = service;
        if (service != null) {
            if (txtNom != null) txtNom.setText(service.getNom());
            if (txtPrix != null) txtPrix.setText(String.valueOf(service.getPrix()));
        }
    }

    @FXML
    private void Btn_EnregistrerModification() {
        String nom = txtNom.getText().trim();
        String prixStr = txtPrix.getText().trim();

        if (nom.isEmpty() || prixStr.isEmpty()) {
            afficherMessage("Veuillez remplir tous les champs.");
            return;
        }

        int prix;
        try {
            prix = Integer.parseInt(prixStr);
        } catch (NumberFormatException e) {
            afficherMessage("Le prix doit être un nombre valide.");
            return;
        }

        serviceAModifier.setNom(nom);
        serviceAModifier.setPrix(prix);

        boolean estModifie = false;

        try {
            estModifie = daoService.update(serviceAModifier);
        } catch (Exception e) {
            if (e.getCause() instanceof PSQLException psqlException) {
                if ("23505".equals(psqlException.getSQLState())) {
                    afficherMessage("Erreur : Ce service existe déjà !");
                    return;
                }
            }
            afficherMessage("Erreur technique lors de la modification.");
            e.printStackTrace();
            return;
        }

        if (estModifie) {
            fermerFenetre();
        }
    }

    private void afficherMessage(String msg) {
        if (txtMessage != null) txtMessage.setText(msg);
    }

    private void fermerFenetre() {
        Stage stage = (Stage) txtNom.getScene().getWindow();
        stage.close();
    }
}