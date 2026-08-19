package com.stationservice.Controller.dossierEntretien;

import com.stationservice.Models.Entretien;
import com.stationservice.Models.ServiceItem;
import com.stationservice.dao.EntretienDao;
import com.stationservice.dao.ServiceDao;
import com.stationservice.config.DatabaseConfig;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NouveauEntretienController {

    @FXML private Label lblTitreModal;
    @FXML private TextField txtNomClient;
    @FXML private TextField txtImmatriculation;
    @FXML private Label lblDateAutomatique;
    @FXML private VBox vboxServices;
    @FXML private Label lblCoutTotal;
    @FXML private Label txtMessage;

    private final EntretienDao entretienDao = DatabaseConfig.getDao(EntretienDao.class);
    private final ServiceDao serviceDao = DatabaseConfig.getDao(ServiceDao.class);

    private final List<ServiceItem> listeServices = new ArrayList<>();
    private Entretien entretienEnCoursModif = null;
    private int prixTotalCalcul = 0;

    @FXML
    public void initialize() {
        if (lblDateAutomatique != null) {
            lblDateAutomatique.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }
        chargerServices();
    }

    private void chargerServices() {
        if (vboxServices != null) {
            vboxServices.getChildren().clear();
        }
        listeServices.clear();

        List<ServiceItem> servicesBD = serviceDao.findAllItems();
        if (servicesBD != null) {
            for (ServiceItem s : servicesBD) {
                CheckBox cb = new CheckBox(s.getNomService() + " (" + s.getPrixService() + " Ar)");
                cb.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    s.setSelected(isNowSelected);
                    recalculerTotal();
                });
                if (vboxServices != null) {
                    vboxServices.getChildren().add(cb);
                }
                listeServices.add(s);
            }
        }
    }

    private void recalculerTotal() {
        prixTotalCalcul = listeServices.stream()
                .filter(ServiceItem::isSelected)
                .mapToInt(ServiceItem::getPrixService)
                .sum();
        if (lblCoutTotal != null) {
            lblCoutTotal.setText(prixTotalCalcul + " Ar");
        }
    }

    public void chargerDataPourModification(Entretien entretien) {
        this.entretienEnCoursModif = entretien;

        if (lblTitreModal != null) lblTitreModal.setText("Modifier l'Entretien");
        if (txtNomClient != null) txtNomClient.setText(entretien.getNomClient());
        if (txtImmatriculation != null) txtImmatriculation.setText(entretien.getImmatriculationVoiture());

        if (entretien.getDateEntretien() != null && lblDateAutomatique != null) {
            lblDateAutomatique.setText(entretien.getDateEntretien().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }

        this.prixTotalCalcul = entretien.getPrixEntretien();
        if (lblCoutTotal != null) {
            lblCoutTotal.setText(prixTotalCalcul + " Ar");
        }
    }

    @FXML
    private void handleEnregistrer() {
        String immat = txtImmatriculation != null ? txtImmatriculation.getText().trim() : "";
        String nomClient = txtNomClient != null ? txtNomClient.getText().trim() : "";

        if (immat.isEmpty() || nomClient.isEmpty()) {
            setMessage("Veuillez remplir le nom du client et l'immatriculation.");
            return;
        }

        if (prixTotalCalcul == 0) {
            setMessage("Veuillez cocher au moins un service.");
            return;
        }

        LocalDateTime dateNow = LocalDateTime.now();

        if (entretienEnCoursModif == null) {
            String newNum = "ENT" + (System.currentTimeMillis() % 10000);
            Entretien nouveau = new Entretien(newNum, immat, nomClient, dateNow, prixTotalCalcul);
            entretienDao.insert(nouveau);
        } else {
            entretienEnCoursModif.setImmatriculationVoiture(immat);
            entretienEnCoursModif.setNomClient(nomClient);
            entretienEnCoursModif.setPrixEntretien(prixTotalCalcul);
            entretienDao.update(entretienEnCoursModif);
        }

        fermerFenetre();
    }

    @FXML
    private void handleAnnuler() {
        fermerFenetre();
    }

    private void setMessage(String msg) {
        if (txtMessage != null) txtMessage.setText(msg);
    }

    private void fermerFenetre() {
        if (txtImmatriculation != null && txtImmatriculation.getScene() != null) {
            Stage stage = (Stage) txtImmatriculation.getScene().getWindow();
            stage.close();
        }
    }
}