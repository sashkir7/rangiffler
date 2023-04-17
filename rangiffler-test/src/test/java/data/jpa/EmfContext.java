package data.jpa;

import config.DatabaseProperties;
import data.DataBase;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public enum EmfContext {
    INSTANCE;

    private final Map<DataBase, EntityManagerFactory> emfContext = new HashMap<>();

    public synchronized EntityManagerFactory getEmf(DataBase dataBase) {
        if (emfContext.get(dataBase) == null) {
            Map<String, String> settings = new HashMap<>();
            settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            settings.put("hibernate.connection.username", DatabaseProperties.DATABASE_USERNAME);
            settings.put("hibernate.connection.password", DatabaseProperties.DATABASE_PASSWORD);
            settings.put("hibernate.connection.url", dataBase.toString());

            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
                    "persistence-unit-name", settings);
            this.emfContext.put(dataBase, new ThreadLocalEntityManagerFactory(entityManagerFactory));
        }
        return emfContext.get(dataBase);
    }

    public Collection<EntityManagerFactory> storedEmf() {
        return emfContext.values();
    }

}
