/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 * com.stationservice.dao.AchatDao;
 */
package com.stationservice.dao;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import com.stationservice.Models.Achat;
import com.stationservice.Models.RecetteMensuelle;

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

    //recuperer un unique
    @SqlQuery("SELECT * FROM ACHAT WHERE num_prod = :num_prod")
    Optional<Achat> findById(@Bind("num_prod")  String num_prod);


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
               SET stock = stock - :nbr_litre
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

    //recuperation des valeur pour le tableau
    @SqlQuery("SELECT a.*, p.design FROM achat a LEFT JOIN produit p ON a.num_prod = p.num_prod")
    List<Achat> select();

    //supprimer achat
    @SqlUpdate("DELETE FROM achat WHERE num_achat = :num_achat")
    Boolean delete(@Bind("num_achat") String num_achat);

    @SqlQuery("""
        SELECT 
            TO_CHAR(date_achat, 'MM/YYYY') AS moisAnnee,
            COALESCE(SUM(montant_paye_achat), 0) AS totalMensuel
        FROM achat
        WHERE date_achat >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '4 month')
        GROUP BY DATE_TRUNC('month', date_achat), TO_CHAR(date_achat, 'MM/YYYY')
        ORDER BY DATE_TRUNC('month', date_achat) ASC
        LIMIT 5
    """)
    @RegisterBeanMapper(RecetteMensuelle.class)
    List<RecetteMensuelle> obtenirRecettesCinqDerniersMois();
    
    
    
    
    //pARTIE MODIFICATION
    
    // 1. Ancien volume d'achat
    @SqlQuery("SELECT nbr_litre FROM achat WHERE num_achat = :num_achat")
    Optional<Double> findAncienNbrLitre(@Bind("num_achat") String num_achat);

    // 2. Stock actuel du produit
    @SqlQuery("SELECT stock FROM produit WHERE num_prod = :num_prod")
    Optional<Double> findStockProduit(@Bind("num_prod") String num_prod);

    // 3. Mise à jour des informations modifiables de l'achat
    @SqlUpdate("""
        UPDATE achat SET 
            nom_client = :nom_client,
            nbr_litre = :nbr_litre,
            montant_paye_achat = :montant_paye_achat
        WHERE num_achat = :num_achat
        """)
    boolean updateAchat(
        @Bind("num_achat") String num_achat,
        @Bind("nom_client") String nom_client,
        @Bind("nbr_litre") double nbr_litre,
        @Bind("montant_paye_achat") int montant_paye_achat
    );

    // 4. Mise à jour du stock dans produit
    @SqlUpdate("UPDATE produit SET stock = :nouveauStock WHERE num_prod = :num_prod")
    boolean updateStockProduit(@Bind("num_prod") String num_prod, @Bind("nouveauStock") double nouveauStock);

    // 5. Transaction globale
    @Transaction
    default boolean modificationAchat(Achat achat) {
        Optional<Double> stockProduitOpt = findStockProduit(achat.getNum_prod());
        Optional<Double> ancienNbrLitreOpt = findAncienNbrLitre(achat.getNum_achat());

        if (stockProduitOpt.isEmpty() || ancienNbrLitreOpt.isEmpty()) {
            return false;
        }

        double stockActuelProduit = stockProduitOpt.get();
        double ancienNbrLitre = ancienNbrLitreOpt.get();

        // Calcul de la différence de volume
        double difference = achat.getNbr_litre() - ancienNbrLitre;
        double nouveauStockProduit = stockActuelProduit - difference;

        // Empêche d'avoir un stock négatif
        if (nouveauStockProduit < 0) {
            return false;
        }

        // Exécution des mises à jour
        boolean achatMaj = updateAchat(
            achat.getNum_achat(),
            achat.getNom_client(),
            achat.getNbr_litre(),
            achat.getMontant_paye_achat()
        );

        boolean produitMaj = updateStockProduit(achat.getNum_prod(), nouveauStockProduit);

        return achatMaj && produitMaj;
    }
    
    @SqlQuery("SELECT COALESCE(MAX(CAST(NULLIF(regexp_replace(num_achat, '\\D', '', 'g'), '') AS INTEGER)), 0) FROM achat")
    int getLastAchatNumber();
    
}

