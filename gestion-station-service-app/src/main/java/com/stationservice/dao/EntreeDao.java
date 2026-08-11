package com.stationservice.dao;

import com.stationservice.Models.Entree;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Entree.class)
public interface EntreeDao {

    // Liste de tous les réapprovisionnements
    @SqlQuery("SELECT * FROM entree ORDER BY date_entree DESC")
    List<Entree> findAll();

    // Enregistrer une nouvelle entrée de stock
    @SqlUpdate("""
               INSERT INTO entree (num_entree, num_prod, stock_entree)
               VALUES (:numEntree, :numProd, :stockEntree)
               """)
    boolean insert(@BindBean Entree entree);
}