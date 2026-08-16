package com.stationservice.dao;

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
               INSERT INTO entree (num_entree, num_prod, stock_entree, date_entree)
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
        
        // modification de la  stock
        boolean stockMAJ = augmenterStockProduit(entre.getStock_entree(), entre.getNum_prod());
        if (!confirmer || !stockMAJ){
            
            throw new IllegalStateException("Impossible de modifier la base, achat non effectuer");
        }
        return confirmer;
    }

    // Insertion d'une nouvelle entrée
    
}