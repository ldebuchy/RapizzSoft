package com.rapizz.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static HikariDataSource dataSource;
    private static final String PROPERTIES_FILE = "src/main/resources/config/database.properties";
    private static boolean initialized = false;

    private static void initialize() {
        if (initialized) return;
        
        try {
            // Chargement du driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Chargement des propriétés depuis le fichier
            Properties properties = new Properties();
            properties.load(new FileInputStream(PROPERTIES_FILE));
            
            // Configuration de HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("jdbc.url"));
            config.setUsername(properties.getProperty("jdbc.user"));
            config.setPassword(properties.getProperty("jdbc.password"));
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            
            // Configuration du pool
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setIdleTimeout(300000); // 5 minutes
            config.setConnectionTimeout(20000); // 20 secondes
            config.setMaxLifetime(1200000); // 20 minutes
            
            // Configuration des connexions
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            
            dataSource = new HikariDataSource(config);
            initialized = true;
        } catch (ClassNotFoundException e) {
            showErrorDialog("Erreur lors du chargement du driver MySQL", e);
            System.exit(1);
        } catch (IOException e) {
            showErrorDialog("Erreur lors de la lecture du fichier de configuration", e);
            System.exit(1);
        } catch (Exception e) {
            showErrorDialog("Erreur lors de l'initialisation de la connexion à la base de données", e);
            System.exit(1);
        }
    }

    private static void showErrorDialog(String message, Exception e) {
        JOptionPane.showMessageDialog(null,
            message + "\n\nDétails de l'erreur :\n" + e.getMessage(),
            "Erreur de connexion à la base de données",
            JOptionPane.ERROR_MESSAGE);
    }

    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            initialize();
        }
        
        if (dataSource == null) {
            String message = "Le pool de connexions n'est pas initialisé";
            showErrorDialog(message, new SQLException(message));
            System.exit(1);
            throw new SQLException(message);
        }
        
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            showErrorDialog("Erreur lors de la connexion à la base de données", e);
            throw e;
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
} 