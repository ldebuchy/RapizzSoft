package com.rapizz.dao.implementations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.rapizz.dao.interfaces.ClientDAO;
import com.rapizz.model.Client;
import com.rapizz.utils.DatabaseConnection;

public class ClientDAOImpl implements ClientDAO {
    @Override
    public void create(Client client) {
        String sql = "INSERT INTO CLIENT (nom, prenom, telephone, adresse, nb_pizzas_achetees, solde) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getTelephone());
            stmt.setString(4, client.getAdresse());
            stmt.setInt(5, client.getNbPizzasAchetees());
            stmt.setDouble(6, client.getSolde());
            stmt.executeUpdate();

            // Récupération de l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création du client a échoué, aucun ID généré.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client read(int id) {
        String sql = "SELECT * FROM CLIENT WHERE id_client = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(
                    rs.getInt("id_client"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("adresse"),
                    rs.getInt("nb_pizzas_achetees"),
                    rs.getDouble("solde")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Client client) {
        String sql = "UPDATE CLIENT SET nom = ?, prenom = ?, telephone = ?, adresse = ?, nb_pizzas_achetees = ?, solde = ? WHERE id_client = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getTelephone());
            stmt.setString(4, client.getAdresse());
            stmt.setInt(5, client.getNbPizzasAchetees());
            stmt.setDouble(6, client.getSolde());
            stmt.setInt(7, client.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM CLIENT WHERE id_client = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM CLIENT";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(new Client(
                    rs.getInt("id_client"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("adresse"),
                    rs.getInt("nb_pizzas_achetees"),
                    rs.getDouble("solde")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Client findMeilleurClient() {
        String sql = "SELECT * FROM CLIENT ORDER BY nb_pizzas_achetees DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new Client(
                    rs.getInt("id_client"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("adresse"),
                    rs.getInt("nb_pizzas_achetees"),
                    rs.getDouble("solde")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Client> findClientsAboveAverage() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT c.*, COUNT(l.id_livraison) as nb_commandes " +
                    "FROM CLIENT c " +
                    "JOIN LIVRAISON l ON c.id_client = l.id_client " +
                    "GROUP BY c.id_client " +
                    "HAVING nb_commandes > (SELECT AVG(nb_cmd) FROM (" +
                    "    SELECT COUNT(id_livraison) as nb_cmd " +
                    "    FROM LIVRAISON " +
                    "    GROUP BY id_client) as cmd_count)";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(new Client(
                    rs.getInt("id_client"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("adresse"),
                    rs.getInt("nb_pizzas_achetees"),
                    rs.getDouble("solde")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Client findByNomPrenom(String nom, String prenom) {
        String sql = "SELECT * FROM CLIENT WHERE nom = ? AND prenom = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(
                    rs.getInt("id_client"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("adresse"),
                    rs.getInt("nb_pizzas_achetees"),
                    rs.getDouble("solde")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 