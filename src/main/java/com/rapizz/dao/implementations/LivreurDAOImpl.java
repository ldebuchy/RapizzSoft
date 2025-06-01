package com.rapizz.dao.implementations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.rapizz.dao.interfaces.LivreurDAO;
import com.rapizz.model.Livreur;
import com.rapizz.utils.DatabaseConnection;

public class LivreurDAOImpl implements LivreurDAO {
    @Override
    public void create(Livreur livreur) {
        String sql = "INSERT INTO LIVREUR (id_livreur, nom, prenom, telephone, nb_retards) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livreur.getId());
            stmt.setString(2, livreur.getNom());
            stmt.setString(3, livreur.getPrenom());
            stmt.setString(4, livreur.getTelephone());
            stmt.setInt(5, livreur.getNbRetards());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Livreur read(int id) {
        String sql = "SELECT * FROM LIVREUR WHERE id_livreur = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Livreur(
                    rs.getInt("id_livreur"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getInt("nb_retards")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Livreur livreur) {
        String sql = "UPDATE LIVREUR SET nom = ?, prenom = ?, telephone = ?, nb_retards = ? WHERE id_livreur = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livreur.getNom());
            stmt.setString(2, livreur.getPrenom());
            stmt.setString(3, livreur.getTelephone());
            stmt.setInt(4, livreur.getNbRetards());
            stmt.setInt(5, livreur.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM LIVREUR WHERE id_livreur = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Livreur> findAll() {
        List<Livreur> livreurs = new ArrayList<>();
        String sql = "SELECT * FROM LIVREUR";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livreurs.add(new Livreur(
                    rs.getInt("id_livreur"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getInt("nb_retards")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livreurs;
    }

    @Override
    public void incrementerRetards(int idLivreur) {
        String sql = "UPDATE LIVREUR SET nb_retards = nb_retards + 1 WHERE id_livreur = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivreur);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Livreur findPireLivreur() {
        String sql = "SELECT * FROM LIVREUR ORDER BY nb_retards DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new Livreur(
                    rs.getInt("id_livreur"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getInt("nb_retards")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Livreur> findLivreursDisponibles() {
        List<Livreur> livreurs = new ArrayList<>();
        String sql = "SELECT l.* FROM LIVREUR l " +
                    "WHERE l.id_livreur NOT IN (" +
                    "    SELECT DISTINCT l2.id_livreur " +
                    "    FROM LIVRAISON l2 " +
                    "    WHERE l2.status_livraison = 'En cours'" +
                    ")";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livreurs.add(new Livreur(
                    rs.getInt("id_livreur"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getInt("nb_retards")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livreurs;
    }

    @Override
    public int getNombreRetards(int idLivreur) {
        String sql = "SELECT nb_retards FROM LIVREUR WHERE id_livreur = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivreur);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("nb_retards");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Livreur findByNom(String nom) {
        String sql = "SELECT * FROM LIVREUR WHERE nom = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Livreur(
                    rs.getInt("id_livreur"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getInt("nb_retards")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Livreur findLivreurAvecPlusDeRetards() {
        String sql = "SELECT * FROM LIVREUR ORDER BY nb_retards DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new Livreur(
                    rs.getInt("id_livreur"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getInt("nb_retards")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 