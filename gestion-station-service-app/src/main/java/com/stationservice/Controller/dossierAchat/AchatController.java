package com.stationservice.Controller.dossierAchat;

import com.stationservice.Models.Achat;
import com.stationservice.Models.Produit;
import com.stationservice.dao.AchatDao;
import com.stationservice.dao.ProduitDao;
import com.stationservice.config.DatabaseConfig;
import com.stationservice.Controller.dossierAchat.ModifierAchatController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AchatController {

    @FXML private TableView<Achat> tableAchats;
    @FXML private TableColumn<Achat, String> num_achat;
    @FXML private TableColumn<Achat, String> num_prod;
    @FXML private TableColumn<Achat, Double> nbr_litre;
    @FXML private TableColumn<Achat, String> nom_client;
    @FXML private TableColumn<Achat, Integer> montant_paye_achat;
    @FXML private TableColumn<Achat, Void> colAction;

    private final ObservableList<Achat> _listeAchat = FXCollections.observableArrayList();
    
    // Initialisation des DAO via DatabaseConfig (JDBI 3)
    public AchatDao _achatDao = DatabaseConfig.getDao(AchatDao.class);
    public ProduitDao _produitDao = DatabaseConfig.getDao(ProduitDao.class);

    @FXML
    public void initialize() {
        // 1. Liaison des colonnes standards
        num_achat.setCellValueFactory(new PropertyValueFactory<>("num_achat"));
        nbr_litre.setCellValueFactory(new PropertyValueFactory<>("nbr_litre"));
        montant_paye_achat.setCellValueFactory(new PropertyValueFactory<>("montant_paye_achat"));
        nom_client.setCellValueFactory(new PropertyValueFactory<>("nom_client"));
        // 2. Affichage du NOM du produit au lieu de num_prod
        num_prod.setCellValueFactory(cellData -> {
            Achat achat = cellData.getValue();
            if (achat.getDesign() != null && !achat.getDesign().isEmpty()) {
                return new SimpleStringProperty(achat.getDesign());
            }
            // Recherche par le ProduitDao si nom_prod n'est pas rempli par la requête SQL
            if (_produitDao != null && achat.getNum_prod() != null) {
                Optional<Produit> p = _produitDao.findById(achat.getNum_prod());
                if (p.isPresent()) {
                    return new SimpleStringProperty(p.get().getDesign());
                }
            }
            return new SimpleStringProperty(achat.getNum_prod());
        });

        tableAchats.setItems(_listeAchat);

        // 3. Configuration des boutons Modifier et Supprimer
        ajouterBoutonsActions();

        // 4. Charger les données
        rechargerLesListeAchatBD();
    }

    private void ajouterBoutonsActions() {
        Callback<TableColumn<Achat, Void>, TableCell<Achat, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Achat, Void> call(final TableColumn<Achat, Void> param) {
                return new TableCell<>() {

                    private final Button btnEditer = new Button("Éditer");
                    private final Button btnSupprimer = new Button("Supprimer");
                    private final HBox container = new HBox(6, btnEditer, btnSupprimer);

                    {
                        container.setAlignment(Pos.CENTER);
                        btnEditer.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");
                        btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");

                        btnEditer.setOnAction(event -> {
                            Achat achat = getTableView().getItems().get(getIndex());
                            handleModifierAchat(achat);
                        });

                        btnSupprimer.setOnAction(event -> {
                            Achat achat = getTableView().getItems().get(getIndex());
                            handleSupprimerAchat(achat);
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

        colAction.setCellFactory(cellFactory);
    }

    private void handleModifierAchat(Achat achat) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DossierAchat/modifierAchat.fxml"));
            Parent root = loader.load();
            
            ModifierAchatController controller = loader.getController();
            controller.Initialise(achat);
            Stage popUp = new Stage();
            popUp.setTitle("Modifier l'achat N° " + achat.getNum_achat());
            popUp.setScene(new Scene(root));
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.showAndWait();

            rechargerLesListeAchatBD();
        } catch (IOException e) {
            System.err.println("Erreur ouverture modification : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleSupprimerAchat(Achat achat) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer l'achat N° " + achat.getNum_achat() + " ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                _achatDao.delete(achat.getNum_achat()); // Assurez-vous d'avoir la méthode delete dans AchatDao
                _listeAchat.remove(achat);
            } catch (Exception e) {
                Alert errAlert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression : " + e.getMessage());
                errAlert.show();
            }
        }
    }

    @FXML
    private void Btn_afficherFenetreNouveauAchat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DossierAchat/nouveauAchat.fxml"));
            Parent root = loader.load();

            Stage popUpNouvAchat = new Stage();
            popUpNouvAchat.setTitle("Effectuer un achat");
            popUpNouvAchat.setScene(new Scene(root));
            popUpNouvAchat.initModality(Modality.APPLICATION_MODAL);
            popUpNouvAchat.showAndWait();

            rechargerLesListeAchatBD();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture de la fenêtre d'achat : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void rechargerLesListeAchatBD() {
        if (_achatDao != null) {
            List<Achat> lesAchatBD = _achatDao.select();
            _listeAchat.setAll(lesAchatBD);
            tableAchats.setItems(_listeAchat);
        }
    }
}