package dao;

import model.Client;
import java.sql.*;
import java.util.*;

public class ClientDAO {
    private Connection conn;

    public ClientDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM CLIENT";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(new Client(
                    rs.getInt("id_client"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getFloat("solde")
                ));
            }
        }
        return clients;
    }
}