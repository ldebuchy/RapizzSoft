package com.rapizz.dao.implementations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.rapizz.dao.interfaces.TailleDAO;
import com.rapizz.model.Taille;
import com.rapizz.utils.DatabaseConnection;

public class TailleDAOImpl implements TailleDAO {
    @Override
    public void create(Taille taille) {
        String sql = "INSERT INTO TAILLE (nom, coeff_prix) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, taille.getNom());
            stmt.setDouble(2, taille.getCoefficient());
            stmt.executeUpdate();
            
            // Récupérer l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    taille.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Taille read(int id) {
        String sql = "SELECT * FROM TAILLE WHERE id_taille = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Taille(
                    rs.getInt("id_taille"),
                    rs.getString("nom"),
                    rs.getDouble("coeff_prix")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Taille taille) {
        String sql = "UPDATE TAILLE SET nom = ?, coeff_prix = ? WHERE id_taille = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, taille.getNom());
            stmt.setDouble(2, taille.getCoefficient());
            stmt.setInt(3, taille.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM TAILLE WHERE id_taille = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Taille> findAll() {
        List<Taille> tailles = new ArrayList<>();
        String sql = "SELECT * FROM TAILLE";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tailles.add(new Taille(
                    rs.getInt("id_taille"),
                    rs.getString("nom"),
                    rs.getDouble("coeff_prix")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tailles;
    }

    @Override
    public Taille findByNom(String nom) {
        String sql = "SELECT * FROM TAILLE WHERE nom = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Taille(
                    rs.getInt("id_taille"),
                    rs.getString("nom"),
                    rs.getDouble("coeff_prix")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 
