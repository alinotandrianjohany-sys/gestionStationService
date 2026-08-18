package com.stationservice.dao;

import com.stationservice.Models.Entretien;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface EntretienDao {

    // Récupérer tous les entretiens triés du plus récent au plus ancien
    @SqlQuery("SELECT num_entr AS numEntr, immatriculation_voiture AS immatriculationVoiture, " +
            "nom_client AS nomClient, date_entretien AS dateEntretien, " +
            "prix_entretien AS prixEntretien " +
            "FROM entretien ORDER BY date_entretien DESC")
    @RegisterBeanMapper(Entretien.class)
    List<Entretien> getAllEntretiens();

    // Insérer un nouvel entretien
    @SqlUpdate("INSERT INTO entretien (num_entr, immatriculation_voiture, nom_client, date_entretien, prix_entretien) " +
            "VALUES (:numEntr, :immatriculationVoiture, :nomClient, :dateEntretien, :prixEntretien)")
    void insertEntretien(@BindBean Entretien entretien);

    // Liaisons avec la table DETAILS
    @SqlUpdate("INSERT INTO details (num_entr, num_serv, prix_applique) VALUES (:numEntr, :numServ, :prixApplique)")
    void insertDetail(@Bind("numEntr") String numEntr, @Bind("numServ") String numServ, @Bind("prixApplique") int prixApplique);

    // Chiffre d'affaires total
    @SqlQuery("SELECT COALESCE(SUM(prix_entretien), 0) FROM entretien")
    int getChiffreAffairesEntretien();

    // Nombre total d'entretiens
    @SqlQuery("SELECT COUNT(*) FROM entretien")
    int getNombreEntretiens();
}