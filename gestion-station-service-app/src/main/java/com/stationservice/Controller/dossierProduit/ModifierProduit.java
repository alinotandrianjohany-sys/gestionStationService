/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Controller.dossierProduit;
//com.stationservice.Controller.dossierProduit.ModifierProduit

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import com.stationservice.dao.ProduitDao;
import com.stationservice.Models.Produit;
import com.stationservice.config.DatabaseConfig;

/**
 *
 * @author DELL
 */
public class ModifierProduit {
    @FXML private TextField txtNom;
    @FXML private TextField txtPrix;
    @FXML private Label txtNombreLitre;
    @FXML private Label txtMessage;
    @FXML private Button BtnEnregistrer;
    @FXML private Button BtnFermer;
    
    @FXML private Produit ProduitAModifier;
    @FXML private ProduitDao DAOProduit = DatabaseConfig.getDao(ProduitDao.class);
    
    @FXML 
    public void initialize(Produit produit){
        ProduitAModifier = produit;
        
        txtNom.setText(produit.getDesign());
        txtPrix.setText(String.valueOf(produit.getPrix_litre_prod()));
        txtNombreLitre.setText(String.valueOf(produit.getStock()));
    }
    
    @FXML
    private void Btn_EnregistrerModification(){
        //filtrer les ajout 
        String regexNom = "^[\\p{L}0-9\\s\\-]+$"; // Exemple : lettres et chiffres seulement
        if (txtNom.getText().trim().isEmpty()){
            afficherMessage("Le nom du produit est vide");
            return;
        }
        
        if (!txtNom.getText().matches(regexNom)) {
            afficherMessage("Nom de produit invalide");
            return;
        }
       
        
        //String regexNombre = "^[0-9]+(?:\\.[0-9]{1,2})?$";
        String regexNombre = "^[0-9]+$";
        if (txtPrix.getText().trim().isEmpty()) {
            afficherMessage("Le prix du produit est vide");
            return;
        }
        
        if (!txtPrix.getText().matches(regexNombre)) {
            afficherMessage("Le prix du produit est invalide");
            return;
        }
        
        int Prix = Integer.parseInt(txtPrix.getText());
        if (Prix <= 0){
            afficherMessage("Le nombre de litre doit etre superieur à 0 ");
            return;
        }
        
        //le nombre de litre 
       
        //apres que les valeur sont actuellement sur 
        String nom = txtNom.getText();
        ProduitAModifier.setDesign(nom);
        ProduitAModifier.setPrix_litre_prod(Prix);
        
        //ajout dans la base de donne
        Boolean estModifier = DAOProduit.update(ProduitAModifier);
        if (estModifier){
            Stage stage = (Stage) BtnEnregistrer.getScene().getWindow();
            supprimerChamps();
            stage.close();
        } else {
            afficherMessage("Erreur s'est produit lors du modification");
        }
    }
    
    @FXML 
    private void Btn_annulerModification(){
        Stage stage = (Stage) BtnFermer.getScene().getWindow();
        stage.close();
        supprimerChamps();
    }
    
    
    
    private void afficherMessage(String message){
        txtMessage.setText(message);
        txtMessage.setStyle("-fx-text-fill: red;");
    }
    
    private void supprimerChamps(){
        txtNom.setText("");
        txtPrix.setText("");
    }
}
