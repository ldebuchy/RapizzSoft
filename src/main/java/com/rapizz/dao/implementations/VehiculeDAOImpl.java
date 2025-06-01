package com.rapizz.dao.implementations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.rapizz.dao.interfaces.VehiculeDAO;
import com.rapizz.model.Vehicule;
import com.rapizz.utils.DatabaseConnection;

public class VehiculeDAOImpl implements VehiculeDAO {
    @Override
    public Vehicule create(Vehicule vehicule) {
        String sql = "INSERT INTO VEHICULE (modele, type) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vehicule.getModele());
            stmt.setString(2, vehicule.getType());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vehicule.setId(rs.getInt(1));
                }
            }
            return vehicule;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du véhicule", e);
        }
    }

    @Override
    public Vehicule read(int id) {
        String sql = "SELECT * FROM VEHICULE WHERE id_vehicule = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Vehicule(
                        rs.getInt("id_vehicule"),
                        rs.getString("modele"),
                        rs.getString("type")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture du véhicule", e);
        }
    }

    @Override
    public List<Vehicule> findAll() {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT * FROM VEHICULE";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicules.add(new Vehicule(
                    rs.getInt("id_vehicule"),
                    rs.getString("modele"),
                    rs.getString("type")
                ));
            }
            return vehicules;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture des véhicules", e);
        }
    }

    @Override
    public List<Vehicule> findVehiculesDisponibles() {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT v.* FROM VEHICULE v " +
                    "WHERE NOT EXISTS (SELECT 1 FROM LIVRAISON l " +
                    "WHERE l.id_vehicule = v.id_vehicule " +
                    "AND l.status_livraison = 'En cours')";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicules.add(new Vehicule(
                    rs.getInt("id_vehicule"),
                    rs.getString("modele"),
                    rs.getString("type")
                ));
            }
            return vehicules;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture des véhicules disponibles", e);
        }
    }

    @Override
    public Vehicule update(Vehicule vehicule) {
        String sql = "UPDATE VEHICULE SET modele = ?, type = ? WHERE id_vehicule = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vehicule.getModele());
            stmt.setString(2, vehicule.getType());
            stmt.setInt(3, vehicule.getId());
            stmt.executeUpdate();
            return vehicule;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du véhicule", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM VEHICULE WHERE id_vehicule = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du véhicule", e);
        }
    }

    @Override
    public List<Vehicule> findVehiculesJamaisUtilises() {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT v.* FROM VEHICULE v " +
                    "WHERE NOT EXISTS (SELECT 1 FROM LIVRAISON l " +
                    "WHERE l.id_vehicule = v.id_vehicule)";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicules.add(new Vehicule(
                    rs.getInt("id_vehicule"),
                    rs.getString("modele"),
                    rs.getString("type")
                ));
            }
            return vehicules;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture des véhicules jamais utilisés", e);
        }
    }

    @Override
    public List<Vehicule> findVehiculesByType(String type) {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT * FROM VEHICULE WHERE type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(new Vehicule(
                        rs.getInt("id_vehicule"),
                        rs.getString("modele"),
                        rs.getString("type")
                    ));
                }
            }
            return vehicules;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture des véhicules par type", e);
        }
    }

    @Override
    public Vehicule findByModele(String modele) {
        String sql = "SELECT * FROM VEHICULE WHERE modele = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, modele);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Vehicule(
                        rs.getInt("id_vehicule"),
                        rs.getString("modele"),
                        rs.getString("type")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du véhicule par modèle", e);
        }
    }
} 