package com.stationservice.dao;

/*
import java.util.Optional;
import com.stationservice.Models.Entree;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

@RegisterBeanMapper(Entree.class)
public interface EntreeDao {

    // Liste de tous les réapprovisionnements
    @SqlQuery("SELECT * FROM entree ORDER BY date_entree DESC")
    List<Entree> findAll();
    
    @SqlUpdate("""
               UPDATE produit
               SET stock = stock + :stock_entree
               WHERE num_prod = :num_prod
               """)
    boolean augmenterStockProduit(
            @Bind("stock_entree") double nbr_litre,
            @Bind("num_prod") String num_prod
    );
    

    // Enregistrer une nouvelle entrée de stock (Correction: stock_entree)
    @SqlUpdate("""
               INSERT INTO entree (num_entr, num_prod, stock_entree, date_entree)
               VALUES (:numEntree, :numProd, :stockEntree, CURRENT_DATE)
               """)
    boolean insertEntree(@Bind("numEntree") String numEntree, 
                         @Bind("numProd") String numProd, 
                         @Bind("stockEntree") double stockEntree);
    
    // Compter le nombre total d'entrées existantes dans la BD
    @SqlQuery("SELECT COUNT(*) FROM entree")
    int countEntrees();
    
    @Transaction
    default boolean insertEntreeEtModificationStock(Entree entre){
        // ajout dans la table achat
        boolean confirmer = insertEntree(entre.getNum_entr(), entre.getNum_prod(), entre.getStock_entree());
        
        // modification du stock
        boolean stockMAJ = augmenterStockProduit(entre.getStock_entree(), entre.getNum_prod());
        if (!confirmer || !stockMAJ){
            throw new IllegalStateException("Impossible de modifier la base, achat non effectué");
        }
        return confirmer;
    }

    // Suppression d'une entree
    @SqlUpdate("""
              DELETE FROM entree WHERE num_entr = :num_entr
              """)
    boolean delete(@Bind("num_entr") String num_entr);
    
    
    // Récupération de la valeur dans la table produit 
    @SqlQuery("SELECT stock FROM produit WHERE num_prod = :num_produit")
    Optional<Integer> findStockProduit(@Bind("num_produit") String num_produit);
    
    @SqlQuery("SELECT stock_entree FROM entree WHERE num_entr = :num_entr")
    Optional<Integer> findAncienStockEntr(@Bind("num_entr") String num_entr);
    
    @SqlUpdate("""
               UPDATE entree SET 
               stock_entree = :stock_entree
               WHERE num_entr = :num_entr
               """)
    Boolean updateEntree(
            @Bind("num_entr") String num_entr,
            @Bind("stock_entree") int stock_entr);
    
    // Mise à jour optionnelle du stock dans la table produit
    @SqlUpdate("UPDATE produit SET stock = :nouveauStock WHERE num_prod = :num_prod")
    boolean updateStockProduit(@Bind("num_prod") String num_prod, @Bind("nouveauStock") int nouveauStock);
    
    @Transaction
    default boolean modificationEntre(Entree entree){
        Optional<Integer> stockProduitOpt = findStockProduit(entree.getNum_prod());
        Optional<Integer> ancienStockEntrOpt = findAncienStockEntr(entree.getNum_entr());

        // Vérification de l'existence des enregistrements
        if (stockProduitOpt.isEmpty() || ancienStockEntrOpt.isEmpty()) {
            return false;
        }

        int stockActuelProduit = stockProduitOpt.get();
        int ancienStockEntree = ancienStockEntrOpt.get();

        // Calcul de la différence de stock
        int difference = entree.getStock_entree() - ancienStockEntree;
        int nouveauStockProduit = stockActuelProduit + difference;

        // Empêche un stock produit négatif
        if (nouveauStockProduit < 0) {
            return false;
        }

        // 1. Mise à jour de la table entree
        boolean entreeMaj = updateEntree(entree.getNum_entr(), entree.getStock_entree());

        // 2. Ajustement du stock global dans la table produit
        boolean produitMaj = updateStockProduit(entree.getNum_prod(), nouveauStockProduit);

        return entreeMaj && produitMaj;
    }
    
    @SqlQuery("SELECT COALESCE(MAX(CAST(SUBSTRING(num_entr FROM 8) AS INTEGER)), 0) FROM entree")
    int findMaxNumeroEntree();
}*/



import java.util.Optional;
import com.stationservice.Models.Entree;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

@RegisterBeanMapper(Entree.class)
public interface EntreeDao {

    // Liste de tous les réapprovisionnements
    @SqlQuery("SELECT * FROM entree ORDER BY date_entree DESC")
    List<Entree> findAll();
    
    @SqlUpdate("""
               UPDATE produit
               SET stock = stock + :stock_entree
               WHERE num_prod = :num_prod
               """)
    boolean augmenterStockProduit(
            @Bind("stock_entree") double nbr_litre,
            @Bind("num_prod") String num_prod
    );

    // Enregistrer une nouvelle entrée de stock
    @SqlUpdate("""
               INSERT INTO entree (num_entr, num_prod, stock_entree, date_entree)
               VALUES (:numEntree, :numProd, :stockEntree, CURRENT_DATE)
               """)
    boolean insertEntree(@Bind("numEntree") String numEntree, 
                         @Bind("numProd") String numProd, 
                         @Bind("stockEntree") double stockEntree);
    
    // Compter le nombre total d'entrées
    @SqlQuery("SELECT COUNT(*) FROM entree")
    int countEntrees();
    
    @Transaction
    default boolean insertEntreeEtModificationStock(Entree entre){
        boolean confirmer = insertEntree(entre.getNum_entr(), entre.getNum_prod(), entre.getStock_entree());
        boolean stockMAJ = augmenterStockProduit(entre.getStock_entree(), entre.getNum_prod());
        
        if (!confirmer || !stockMAJ){
            throw new IllegalStateException("Impossible de modifier la base, achat non effectué");
        }
        return confirmer;
    }

    // Suppression d'une entree
    @SqlUpdate("DELETE FROM entree WHERE num_entr = :num_entr")
    boolean delete(@Bind("num_entr") String num_entr);
    
    // Récupération du stock produit (double)
    @SqlQuery("SELECT stock FROM produit WHERE num_prod = :num_produit")
    Optional<Double> findStockProduit(@Bind("num_produit") String num_produit);
    
    // Récupération de l'ancien stock d'entrée (double)
    @SqlQuery("SELECT stock_entree FROM entree WHERE num_entr = :num_entr")
    Optional<Double> findAncienStockEntr(@Bind("num_entr") String num_entr);
    
    @SqlUpdate("""
               UPDATE entree SET 
               stock_entree = :stock_entree
               WHERE num_entr = :num_entr
               """)
    Boolean updateEntree(
            @Bind("num_entr") String num_entr,
            @Bind("stock_entree") double stock_entr);
    
    // Mise à jour du stock global dans la table produit
    @SqlUpdate("UPDATE produit SET stock = :nouveauStock WHERE num_prod = :num_prod")
    boolean updateStockProduit(@Bind("num_prod") String num_prod, @Bind("nouveauStock") double nouveauStock);
    
    @Transaction
    default boolean modificationEntre(Entree entree){
        Optional<Double> stockProduitOpt = findStockProduit(entree.getNum_prod());
        Optional<Double> ancienStockEntrOpt = findAncienStockEntr(entree.getNum_entr());

        // Vérification de l'existence des enregistrements
        if (stockProduitOpt.isEmpty() || ancienStockEntrOpt.isEmpty()) {
            return false;
        }

        double stockActuelProduit = stockProduitOpt.get();
        double ancienStockEntree = ancienStockEntrOpt.get();

        // Calcul de la différence de stock
        double difference = entree.getStock_entree() - ancienStockEntree;
        double nouveauStockProduit = stockActuelProduit + difference;

        // Empêche un stock produit négatif
        if (nouveauStockProduit < 0) {
            return false;
        }

        // 1. Mise à jour de la table entree
        boolean entreeMaj = updateEntree(entree.getNum_entr(), entree.getStock_entree());

        // 2. Ajustement du stock global dans la table produit
        boolean produitMaj = updateStockProduit(entree.getNum_prod(), nouveauStockProduit);

        return entreeMaj && produitMaj;
    }
    
    // Recherche du plus grand identifiant numérique généré
    @SqlQuery("SELECT COALESCE(MAX(CAST(SUBSTRING(num_entr FROM 8) AS INTEGER)), 0) FROM entree")
    int findMaxNumeroEntree();
}