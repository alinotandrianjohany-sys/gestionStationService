/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 * com.stationservice.dao.AchatDao;
*/
package com.stationservice.dao;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import com.stationservice.Models.Achat;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.transaction.Transaction;


import java.util.List;

import java.util.Optional;

/**
 *String num_achat, String num_prod, String nom_client,Double nbr_litre, int montant_paye_achat
 * @author DELL
 */
@RegisterBeanMapper(Achat.class)
public interface AchatDao {
    
    // Cumul du chiffre d'affaires carburant (utilisé pour le Dashboard)
    @SqlQuery("SELECT COALESCE(SUM(montant_paye_achat), 0) FROM achat")
    int getChiffreAffairesCarburant();

    // Nombre total de ventes réalisées
    @SqlQuery("SELECT COUNT(*) FROM achat")
    int getNombreAchats();
    
    //recuperer tout les produit
    @SqlQuery("SELECT * FROM achat")
    List<Achat> select();
    
    //recuperer un unique
    @SqlQuery("SELECT * FROM ACHAT WHERE num_prod = :num_prod")
    Optional<Achat> findById(@Bind("num_prod")  String num_prod);
    
    /*
    @SqlUpdate("""
               UPDATE achat SET 
               num_prod = :num_prod,
               nom_client = :nom_client ,
               nbr_litre = :nbr_litre, 
               montant_paye_achat = :montant_paye_achat , 
               date_achat = :date_achat  
               """)
    Boolean update(@BindBean Achat achat);*/
    
    @SqlUpdate("""
               UPDATE achat SET 
               nom_client = :nom_client ,
               WHERE num_achat = :num_achat
               """)
    Boolean update(@Bind("nom_client") String nom_client, @Bind("num_achat") String num_achat);
    
    //insertion dans la base
    @SqlUpdate("""
              INSERT INTO achat ( num_achat, num_prod ,nom_client, nbr_litre, montant_paye_achat, date_achat )
              VALUES (:num_achat , :num_prod, :nom_client, :nbr_litre, :montant_paye_achat, :date_achat)
              """)
    Boolean insert(@BindBean Achat achat);
    
    @SqlUpdate("""
               UPDATE produit
               SET stock = stock - :nbr_litre,
               WHERE num_prod = :num_prod
               """)
    boolean diminuerStockProduit(
            @Bind("nbr_litre") double nbr_litre,
            @Bind("num_prod") String num_prod
    );
    
    @Transaction
    default boolean enregistrerVenteEtMettreAJourStock(Achat achat){
        // ajout dans la table achat
        boolean confirmer = insert(achat);
        
        // modification de la  stock
        boolean stockMAJ = diminuerStockProduit(achat.getNbr_litre(), achat.getNum_prod());
        if (!confirmer || !stockMAJ){
            
            throw new IllegalStateException("Impossible de modifier la base, achat non effectuer");
        }
        return confirmer;
    }
    
}
