package com.stationservice.config;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static Jdbi jdbi;

    // Constructeur privé pour empêcher l'instanciation (Pattern Singleton)
    private DatabaseConfig() {}

    /**
     * Récupère l'instance unique de Jdbi pour exécuter des requêtes.
     */
    public static Jdbi getJdbi() {
        if (jdbi == null) {
            try {
                // Chargement des identifiants depuis le fichier db.properties
                Properties props = new Properties();
                try (InputStream input = DatabaseConfig.class.getResourceAsStream("/db.properties")) {
                    if (input == null) {
                        throw new RuntimeException("Fichier 'db.properties' introuvable dans src/main/resources/");
                    }
                    props.load(input);
                }

                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String pass = props.getProperty("db.password");

                // Initialisation de Jdbi
                jdbi = Jdbi.create(url, user, pass);

                // ACTIVATION DU PLUGIN SQLOBJECT (Indispensable pour utiliser les interfaces DAO avec @SqlQuery / @SqlUpdate)
                jdbi.installPlugin(new SqlObjectPlugin());

                System.out.println("Connexion à la base de données PostgreSQL réussie !");

            } catch (Exception e) {
                System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Impossible d'initialiser la base de données.", e);
            }
        }
        return jdbi;
    }

    /**
     * Utilitaire rapide pour récupérer directement une instance de DAO.
     */
    public static <T> T getDao(Class<T> daoClass) {
        return getJdbi().onDemand(daoClass);
    }
}