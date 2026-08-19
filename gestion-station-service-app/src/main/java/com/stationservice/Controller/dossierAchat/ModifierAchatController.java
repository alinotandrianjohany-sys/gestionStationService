/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Controller.dossierAchat;

/**
 *com.stationservice.Controller.dossierAchat.ModifierAchatController
 * @author DELL
 */

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import com.stationservice.dao.ProduitDao;
import com.stationservice.dao.AchatDao;
import com.stationservice.Models.Achat;
import com.stationservice.Models.Produit;
import com.stationservice.config.DatabaseConfig;

//gestion de fermeture
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.ComboBox;

import org.postgresql.util.PSQLException;
import org.jdbi.v3.core.JdbiException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

import java.util.Optional;

public class ModifierAchatController {
    @FXML private TextField txtNumProduit ;
    @FXML private TextField txtNomClient;
    @FXML private TextField txtChoix;
    @FXML private Button BtnEnregistrer;
    //@FXML private TextField txtMontantPayer;
    @FXML private ComboBox ComboTypeAchat;
    @FXML private Label txtMessage;

    private ProduitDao produitDao = DatabaseConfig.getDao(ProduitDao.class);
    private AchatDao achatDao = DatabaseConfig.getDao(AchatDao.class);

    public void Initialise(Achat achat){
        txtNumProduit.setText(achat.getNum_prod());
        txtNomClient.setText(achat.getNom_client());
        txtChoix.setText(achat.getNbr_litre().toString());
        //gestion de combobox
        ComboTypeAchat.getItems().addAll("Litre", "Ariary");
        ComboTypeAchat.setValue("Litre");

    }

    public void Btn_effectuerModificationAchat(){

    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}