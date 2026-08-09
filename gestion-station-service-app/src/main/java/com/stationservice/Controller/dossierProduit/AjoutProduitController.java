/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Controller.dossierProduit;
//com.stationservice.Controller.dossierProduit.AjoutProduitController;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import com.stationservice.dao.ProduitDao;
import com.stationservice.Models.Produit;
import com.stationservice.config.DatabaseConfig;

//gestion de fermeture
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author DELL
 */
public class AjoutProduitController {
    
    @FXML private TextField txtNom ;
    @FXML private TextField txtPrix;
    @FXML private TextField txtNombreLitre;
    @FXML private Label txtMessage;
    @FXML private Button BtnEnregistrer;
    
    @FXML 
    private void handleEnregistrer() {
        
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
        if (txtNombreLitre.getText().trim().isEmpty()) {
            afficherMessage("Le nombre de litre est vide");
            return;
        }
        
        if (!txtNombreLitre.getText().matches(regexNombre)){
            afficherMessage("Le nombre de litre est invalide");
            return;
        }
        
        int Quantite = Integer.parseInt(txtNombreLitre.getText());
        if (Quantite < 0){
            afficherMessage("Le nombre de litre doit etre positif ");
            return;
        }
       
        //apres que les valeur sont actuellement sur 
        String nom = txtNom.getText();
        
        
        //ajout dans la classe roduit puis saise dans la base de donnee
        Produit newProd = new Produit(creerNumProduit(nom),nom ,Quantite,Prix);
        ProduitDao produitDao = DatabaseConfig.getDao(ProduitDao.class);
        boolean estAjoute =produitDao.insert(newProd);
        
        if (estAjoute) {
            txtMessage.setText("Produit ajouté avec succès !");
            txtMessage.setStyle("-fx-text-fill: green;");
            
            //vider les champs
            supprimerChamps();
            
            //fermer la fenetre ai
            fermetureFenetre();
            
        } else {
            txtMessage.setText("Erreur lors de l'ajout du produit");
            txtMessage.setStyle("-fx-text-fill: red;");
        }
        
        System.out.println("est ajouter  -> " + estAjoute);
    }
    
    private void fermetureFenetre(){
        Stage stage = (Stage) BtnEnregistrer.getScene().getWindow();
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
        timer.schedule((()-> {
            stage.close();
            
        }) ,3, TimeUnit.SECONDS);
    }     
    
    private void supprimerChamps(){
        txtNom.setText("");
        txtPrix.setText("");
        txtNombreLitre.setText("");
    }
    
    private String creerNumProduit(String nom){
       ProduitDao produitDao = DatabaseConfig.getDao(ProduitDao.class);
       int nbr = produitDao.nombreProduitAjouter();
       nbr++;
       
       char[] tab = nom.toCharArray();
       String code = tab[0] + "" + tab[1]+ "" + tab[2] + "-" + nbr;
       return code;
    }
    
    private void afficherMessage(String message){
        txtMessage.setText(message);
        txtMessage.setStyle("-fx-text-fill: red;");
    }
}
