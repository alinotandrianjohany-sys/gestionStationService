package com.stationservice.dao;

import com.stationservice.Models.Service;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Service.class)
public interface ServiceDao {

    // Récupérer tous les services
    @SqlQuery("SELECT * FROM service ORDER BY num_serv ASC")
    List<Service> findAll();

    // Ajouter un nouveau service (Lavage, Vidange, etc.)
    @SqlUpdate("""
               INSERT INTO service (num_serv, service, prix_service)
               VALUES (:numServ, :service, :prixService)
               """)
    boolean insert(@BindBean Service service);
}