package com.stationservice.dao;

import com.stationservice.Models.Entretien;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind; // Ajout important
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Entretien.class)
public interface EntretienDao {

    @SqlQuery("SELECT num_entr AS numEntr, immatriculation_voiture AS immatriculationVoiture, nom_client AS nomClient, date_entretien AS dateEntretien, prix_entretien AS prixEntretien FROM entretien")
    List<Entretien> findAll();

    @SqlUpdate("INSERT INTO entretien (num_entr, immatriculation_voiture, nom_client, date_entretien, prix_entretien) VALUES (:numEntr, :immatriculationVoiture, :nomClient, :dateEntretien, :prixEntretien)")
    boolean insert(@BindBean Entretien entretien);

    @SqlUpdate("UPDATE entretien SET immatriculation_voiture = :immatriculationVoiture, nom_client = :nomClient, prix_entretien = :prixEntretien WHERE num_entr = :numEntr")
    boolean update(@BindBean Entretien entretien);

    // Ajout de l'annotation @Bind("numEntr") pour corriger le crash lors de la suppression
    @SqlUpdate("DELETE FROM entretien WHERE num_entr = :numEntr")
    boolean delete(@Bind("numEntr") String numEntr);

    @SqlQuery("SELECT COALESCE(SUM(prix_entretien), 0) FROM entretien")
    int getChiffreAffairesEntretien();
}