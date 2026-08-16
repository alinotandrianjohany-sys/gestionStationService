/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
<TableView fx:id="tableAchats" prefHeight="250.0" prefWidth="425.0">
                                            <columns>
                                              <TableColumn fx:id="colIdAchat" prefWidth="65.0" text="ID" />
                                              <TableColumn fx:id="colProduit" prefWidth="180.0" text="Produit" />
                                              <TableColumn fx:id="colQuantite" prefWidth="80.0" text="Quantité" />
                                              <TableColumn fx:id="colTotal" prefWidth="100.0" text="Total" />
                                            </columns>
                                          </TableView> 
*/
package com.stationservice.Controller.dossierAchat;
//com.stationservice.Controller.dossierAchat.AchatController

import com.stationservice.dao.ProduitDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import com.stationservice.dao.AchatDao;
import com.stationservice.Models.Achat;
import com.stationservice.Models.Produit;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.time.LocalDateTime;

/**
 *
 * @author DELL
 */
public class AchatController {
    
    @FXML private TableView<Achat> tableAchats;
    @FXML private TableColumn<Achat, String> num_achat;
    @FXML private TableColumn<Achat, String> num_prod;
    @FXML private TableColumn<Achat, String> nom_client;
    @FXML private TableColumn<Achat, Double> nbr_litre;
    @FXML private TableColumn<Achat, Integer> montant_paye_achat;
    @FXML private TableColumn<Achat, LocalDateTime> date_achat;
    
    @FXML ObservableList<Achat> _listeAchat = FXCollections.observableArrayList();
    public AchatDao _achatDao;
    
    @FXML
    public void Initialise(){
        rechargerLesListeAchatBD();
    }
    
    @FXML
    private void Btn_afficherFenetreNouveauAchat(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DossierAchat/nouveauAchat.fxml"));
            Parent root = loader.load();
            
            Stage popUpNouvAchat = new Stage();
            popUpNouvAchat.setTitle("Effectuer un achat");
            popUpNouvAchat.setScene(new Scene(root));
            
            popUpNouvAchat.showAndWait();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture de la fenêtre d'achat : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void rechargerLesListeAchatBD(){
        List<Achat> lesAchatBD = _achatDao.select();
        _listeAchat.setAll(lesAchatBD);
        tableAchats.setItems(_listeAchat);
    }
}
