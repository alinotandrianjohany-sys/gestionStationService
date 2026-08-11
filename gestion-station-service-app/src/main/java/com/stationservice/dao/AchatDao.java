package com.stationservice.dao;

import com.stationservice.Models.Achat;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Achat.class)
public interface AchatDao {

    // Récupérer la liste complète des ventes de carburant
    @SqlQuery("SELECT * FROM achat ORDER BY date_achat DESC")
    List<Achat> findAll();

    // Enregistrer un nouvel achat / vente de carburant
    @SqlUpdate("""
               INSERT INTO achat (num_achat, num_prod, nom_client, nbr_litre, montant_paye_achat)
               VALUES (:numAchat, :numProd, :nomClient, :nbrLitre, :montantPayeAchat)
               """)
    boolean insert(@BindBean Achat achat);

    // Cumul du chiffre d'affaires carburant (utilisé pour le Dashboard)
    @SqlQuery("SELECT COALESCE(SUM(montant_paye_achat), 0) FROM achat")
    int getChiffreAffairesCarburant();

    // Nombre total de ventes réalisées
    @SqlQuery("SELECT COUNT(*) FROM achat")
    int getNombreAchats();
}