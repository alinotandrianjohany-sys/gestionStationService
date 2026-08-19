package com.stationservice.Controller.dossierService;

import com.stationservice.Models.Service;
import com.stationservice.dao.ServiceDao;
import com.stationservice.config.DatabaseConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ServiceController {

    @FXML private TableView<Service> tableServices;
    @FXML private TableColumn<Service, String> colNom;
    @FXML private TableColumn<Service, Integer> colPrix;
    @FXML private TableColumn<Service, Void> colActions;
    @FXML private Label lblTotalServices; // Corrigé pour correspondre au fx:id du fichier FXML

    private final ServiceDao serviceDao = DatabaseConfig.getDao(ServiceDao.class);
    private final ObservableList<Service> serviceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (colNom != null) colNom.setCellValueFactory(new PropertyValueFactory<>("nomService"));
        if (colPrix != null) colPrix.setCellValueFactory(new PropertyValueFactory<>("prixService"));

        // Configuration de la colonne Actions avec les boutons Modifier et Supprimer
        if (colActions != null) {
            colActions.setCellFactory(param -> new TableCell<>() {
                private final Button btnModifier = new Button("Modifier");
                private final Button btnSupprimer = new Button("Supprimer");
                private final HBox pane = new HBox(10, btnModifier, btnSupprimer);

                {
                    btnModifier.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");
                    btnSupprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");

                    btnModifier.setOnAction(event -> {
                        Service service = getTableView().getItems().get(getIndex());
                        ouvrirModalFormulaire(service);
                    });

                    btnSupprimer.setOnAction(event -> {
                        Service service = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce service ?", ButtonType.YES, ButtonType.NO);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.YES) {
                            // Suppression sécurisée via le DAO
                            serviceDao.delete(service.getNumServ());
                            chargerServices();
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : pane);
                }
            });
        }

        chargerServices();
    }

    public void chargerServices() {
        serviceList.clear();
        List<Service> services = serviceDao.findAll();
        if (services != null) {
            serviceList.addAll(services);
        }

        if (tableServices != null) {
            tableServices.setItems(serviceList);
        }

        // Mise à jour du label total du nombre de services
        String texteTotal = "Total : " + serviceList.size() + " service(s)";
        if (lblTotalServices != null) {
            lblTotalServices.setText(texteTotal);
        } else {
            System.out.println("-> [Info] " + texteTotal);
        }
    }

    @FXML private void handleNouveauService() { ouvrirModalFormulaire(null); }

    private void ouvrirModalFormulaire(Service service) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dossierService/nouveauService.fxml"));
            Parent root = loader.load();

            if (service != null) {
                NouveauServiceController controller = loader.getController();
                controller.chargerDataPourModification(service);
            }

            Stage stage = new Stage();
            stage.setTitle(service == null ? "Nouveau Service" : "Modifier le Service");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            chargerServices();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}