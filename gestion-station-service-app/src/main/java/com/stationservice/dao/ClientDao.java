/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.stationservice.dao;

import com.stationservice.Models.Client;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

/**
 *
 * @author DELL
 */
@RegisterBeanMapper(Client.class)
public interface ClientDao {
    
    // 1. Tous les clients triés par ordre alphabétique
    @SqlQuery("""
        SELECT nom_client, SUM(montant_paye_achat) AS total_paye 
        FROM achat 
        GROUP BY nom_client 
        ORDER BY nom_client ASC
    """)
    @RegisterBeanMapper(Client.class)
    List<Client> obtenirTousLesClients();

    // 2. Recherche par nom (insensible à la casse)
    @SqlQuery("""
        SELECT nom_client, SUM(montant_paye_achat) AS total_paye 
        FROM achat 
        WHERE LOWER(nom_client) LIKE LOWER(:nom) 
        GROUP BY nom_client 
        ORDER BY nom_client ASC
    """)
    @RegisterBeanMapper(Client.class)
    List<Client> rechercherClientsParNom(@Bind("nom") String nom);

    // 3. Top 5 des meilleurs clients (triés par montant total décroissant)
    @SqlQuery("""
        SELECT nom_client, SUM(montant_paye_achat) AS total_paye 
        FROM achat 
        GROUP BY nom_client 
        ORDER BY total_paye DESC 
        LIMIT 5
    """)
    @RegisterBeanMapper(Client.class)
    List<Client> obtenirTop5Clients();

}

