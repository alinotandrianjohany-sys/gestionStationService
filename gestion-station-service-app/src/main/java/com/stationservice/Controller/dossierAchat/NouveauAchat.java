/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Controller.dossierAchat;
//com.stationservice.Controller.dossierAchat.NouveauAchat



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

import java.util.Optional;

/**
 *
 * @author DELL
 */
public class NouveauAchat {
    @FXML private TextField txtNumProduit ;
    @FXML private TextField txtNomClient;
    @FXML private TextField txtChoix;
    @FXML private Button BtnEnregistrer;
    //@FXML private TextField txtMontantPayer;
    @FXML private ComboBox ComboTypeAchat;
    @FXML private Label txtMessage;
    
    private ProduitDao produitDao = DatabaseConfig.getDao(ProduitDao.class);
    private AchatDao achatDao = DatabaseConfig.getDao(AchatDao.class);
    
    public void initialize(){
        ComboTypeAchat.getItems().addAll("Ariary");
        ComboTypeAchat.setValue("Litre");
    }
    
    @FXML
    private void Btn_effectuerNouveauAchat(){
        //filtrer les ajout 
        String regexNom = "^[\\p{L}\\s\\-]+$"; // Exemple : lettres et chiffres seulement
        if (txtNomClient.getText().trim().isEmpty()){
            afficherMessage("Le nom du client est vide");
            return;
        }
        
        if (!txtNomClient.getText().matches(regexNom)) {
            afficherMessage("Nom du client invalide invalide");
            return;
        }
        
        //verification de valeur de 
        String regexNumProd = "^[a-zA-Z]{3}\\-[0-9]+$";
        if (txtNumProduit.getText().trim().isEmpty()) {
            afficherMessage("Le produit est vide");
            return;
        }
        
        if (!txtNumProduit.getText().matches(regexNumProd)) {
            afficherMessage("Le forme de numero du produit est invalide");
            return;
        }
        
        Optional<Produit> produitOpt = produitDao.findById(txtNumProduit.getText());
        if (!produitOpt.isPresent()) {
            afficherMessage("Aucun produit trouvé avec ce numéro.");
            return;
        } 
        
        Produit produit = produitOpt.get();
        
        //String regexNombre = "^[0-9]+(?:\\.[0-9]{1,2})?$";
        String regexNombre = "^[0-9]+$";
        /*if (txtMontantPayer.getText().trim().isEmpty()) {
            
        }*/
        
        //ajout dans la classe roduit puis saise dans la base de donnee
        boolean estAjoute = false;
        
        if (ComboTypeAchat.getValue().toString().trim().isEmpty()){
            afficherMessage("Type Achat : Litre ou Mountant.");
            return;
        }
        
        String typeChoisie = ComboTypeAchat.getValue().toString();
        if (typeChoisie.equals("Ariary")){
            
            if (txtChoix.getText().trim().isEmpty()){
                afficherMessage("Aucun montant payer est vide");
                return;
            }
            
            if (txtChoix.getText().matches(regexNombre)){
                afficherMessage("Veillez saisir le montant");
                return;
            }
            
            int Prix = Integer.parseInt(txtChoix.getText());
            if (Prix <= 0){
                afficherMessage("Le Prix de doit etre superieur à 0 ");
                return;
            }
            
            double Quantite = Prix / produit.getPrix_litre_prod();
            
            if (Quantite > produit.getStock()){
                afficherMessage("Desole, le Stock est insuffisant ...");
                return;
            }
            
            try {
                Achat newAchat = new Achat("ka",produit.getNum_prod(),txtNomClient.getText(), Quantite, Prix);
                //ProduitDao produitDao = DatabaseConfig.getDao(ProduitDao.class);
                estAjoute = achatDao.insert(newAchat);
                
                if (estAjoute) {
                    
                    txtMessage.setText("Merci de votre confiance, A Bientot");
                    txtMessage.setStyle("-fx-text-fill: green;");

                    //vider les champs
                    supprimerChamps();
                    fermetureFenetre();

                } else {
                    afficherMessage("Erreur lors de l'ajout du produit");
                    return;
                }     

            } catch (JdbiException e) {
                if (e.getCause() instanceof PSQLException psqlException) {

                    // Code SQLState 23505 = Violation de contrainte UNIQUE
                    if ("23505".equals(psqlException.getSQLState())) {
                        afficherMessage("Erreur de doublon : Le Achat existe déjà !");
                        return;
                    }
                }
            }
            
            
        } else if (typeChoisie.equals("Litre")){
            
            if (txtChoix.getText().trim().isEmpty()){
                afficherMessage("Quantite est vide");
                return;
            }
            String saisie = txtChoix.getText().trim();
            if (saisie.matches(regexNombre)){
                afficherMessage("Veillez saisir un nombre");
                return;
            }
            
            Double Quantite = Double.parseDouble(saisie.replace("," , "."));
            if (Quantite <= 0){
                afficherMessage("La quantite doit etre superieur à 0 ");
                return;
            }
            
            if (Quantite > produit.getStock()){
                afficherMessage("Le nombre Produit est insuffisant");
                return;
            }
            
            Double calcule  = Quantite * produit.getPrix_litre_prod();
            int montantAPayer = Integer.parseInt(calcule.toString());
            
            try {
                Achat newAchat = new Achat("ka",produit.getNum_prod(),txtNomClient.getText(), Quantite, montantAPayer);
                //ProduitDao produitDao = DatabaseConfig.getDao(ProduitDao.class);
                estAjoute = achatDao.insert(newAchat);
                
                if (estAjoute) {
                    
                    txtMessage.setText("Merci de votre confiance, A Bientot");
                    txtMessage.setStyle("-fx-text-fill: green;");

                    //vider les champs
                    supprimerChamps();
                    fermetureFenetre();

                } else {
                    afficherMessage("Erreur lors de l'ajout du produit");
                    return;
                } 

            } catch (JdbiException e) {
                if (e.getCause() instanceof PSQLException psqlException) {

                    // Code SQLState 23505 = Violation de contrainte UNIQUE
                    if ("23505".equals(psqlException.getSQLState())) {
                        afficherMessage("Erreur de doublon : Le Achat existe déjà !");
                        return;
                    }
                }
            }            
        }   
        System.out.println("est ajouter  -> " + estAjoute);
    }
    
    private void supprimerChamps(){
        //riens
        txtNumProduit.setText("");
        txtNomClient.setText("");
        txtChoix.setText("");
    }
    
    //private String creerNumProduit(){
        //rien
    //}
    
    private void afficherMessage(String message){
        txtMessage.setText(message);
        txtMessage.setStyle("-fx-text-fill: red;");
    }
    
    private void fermetureFenetre(){
        Stage stage = (Stage) BtnEnregistrer.getScene().getWindow();
        stage.close();
    }     
}
