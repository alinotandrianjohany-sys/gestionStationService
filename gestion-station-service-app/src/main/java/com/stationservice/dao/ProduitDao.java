/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.stationservice.dao;

//package com.stationservice.dao.ProduitDao;

import com.stationservice.Models.Produit;
//import com.stationservice.Models.Entree;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
//import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author DELL
 */
//@RegisterConstructorMapper(Entree.class)
@RegisterBeanMapper(Produit.class)
public interface ProduitDao {
    
    //Lecture 
    @SqlQuery("SELECT * FROM produit ORDER BY num_prod ASC")
    List<Produit> findAll();
    
    @SqlQuery("SELECT * FROM produit WHERE num_prod = :num_prod")
    Optional<Produit> findById(@Bind("num_prod") String num_prod);
    
    //delete 
    @SqlUpdate("DELETE FROM produit WHERE num_prod = :num_prod")
    boolean delete(@Bind("num_prod") String num_prod);
    
    @SqlUpdate("""
            UPDATE produit 
            SET 
            stock = :stock,
            prix_litre_prod = :prix_litre_prod,
            design = :design  
            WHERE num_prod = :num_prod
            """)
    boolean update(@BindBean Produit produit);
    
    //insertion 
    @SqlUpdate("""
               INSERT INTO Produit (num_prod, design, prix_litre_prod, stock )
               VALUES (:num_prod, :design, :prix_litre_prod, :stock)
               """)
    boolean insert(@BindBean Produit produit);
    
    //requete pour la verification de stock
    @SqlQuery("SELECT * FROM PRODUIT WHERE stock < 10")
    List<Produit> findStockFaible();
    
    @SqlQuery("select COUNT(*) from produit")
    int nombreProduitAjouter();
    /*@SqlUpdate("""
            UPDATE SET 
            stock = stock + :stock_entree
            WHERE num_prod = :num_prod
            """)
    boolean aprovisionnementProduit(@BindBean Entree entree );*/
    
    
}
