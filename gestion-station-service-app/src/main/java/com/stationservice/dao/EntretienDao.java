package com.stationservice.dao;

import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface EntretienDao {

    // Total des recettes générées par les services d'entretien
    @SqlQuery("SELECT COALESCE(SUM(prix_entretien), 0) FROM entretien")
    int getChiffreAffairesEntretien();

    // Nombre total d'entretiens réalisés
    @SqlQuery("SELECT COUNT(*) FROM entretien")
    int getNombreEntretiens();
}