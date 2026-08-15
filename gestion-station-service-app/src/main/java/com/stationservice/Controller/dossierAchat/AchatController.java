/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

/**
 *
 * @author DELL
 */
public class AchatController {
    
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
}
