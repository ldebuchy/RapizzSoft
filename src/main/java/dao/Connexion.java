package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Connexion {

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = Connexion.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new SQLException("Fichier de configuration introuvable");
            }
            props.load(input);
        } catch (IOException e) {
            throw new SQLException("Erreur de lecture du fichier de configuration", e);
        }
        String url = props.getProperty("jdbc.url");
        String user = props.getProperty("jdbc.user");
        String password = props.getProperty("jdbc.password");
        return DriverManager.getConnection(url, user, password);
    }
}

