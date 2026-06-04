package com.pao.project.fooddelivery.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Properties props = new Properties();
            InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties");
            if (is == null) {
                throw new RuntimeException("db.properties nu a fost gasit in resources/");
            }
            props.load(is);

            String url      = props.getProperty("db.url");
            String user     = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            this.connection = DriverManager.getConnection(url, user, password);
        } catch (IOException e) {
            throw new RuntimeException("Eroare la citirea db.properties: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la conectarea la baza de date: " + e.getMessage(), e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
