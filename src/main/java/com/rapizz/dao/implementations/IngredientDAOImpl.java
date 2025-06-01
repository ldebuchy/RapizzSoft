package com.rapizz.dao.implementations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.rapizz.dao.interfaces.IngredientDAO;
import com.rapizz.model.Ingredient;
import com.rapizz.utils.DatabaseConnection;

public class IngredientDAOImpl implements IngredientDAO {
    @Override
    public void create(Ingredient ingredient) {
        String sql = "INSERT INTO INGREDIENT (nom) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ingredient.getNom());
            stmt.executeUpdate();

            // Récupération de l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ingredient.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création de l'ingrédient a échoué, aucun ID généré.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Ingredient read(int id) {
        String sql = "SELECT * FROM INGREDIENT WHERE id_ingredient = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Ingredient(
                    rs.getInt("id_ingredient"),
                    rs.getString("nom")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Ingredient ingredient) {
        String sql = "UPDATE INGREDIENT SET nom = ? WHERE id_ingredient = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ingredient.getNom());
            stmt.setInt(2, ingredient.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM INGREDIENT WHERE id_ingredient = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ingredient> findAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM INGREDIENT";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ingredients.add(new Ingredient(
                    rs.getInt("id_ingredient"),
                    rs.getString("nom")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    @Override
    public Ingredient findIngredientLePlusUtilise() throws SQLException {
        String sql = "SELECT i.*, COUNT(cp.id_pizza) as nb_utilisations " +
                    "FROM INGREDIENT i " +
                    "JOIN COMPO_PIZZA cp ON i.id_ingredient = cp.id_ingredient " +
                    "GROUP BY i.id_ingredient, i.nom " +
                    "ORDER BY nb_utilisations DESC " +
                    "LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new Ingredient(
                    rs.getInt("id_ingredient"),
                    rs.getString("nom")
                );
            }
        }
        return null;
    }

    @Override
    public List<Ingredient> findIngredientsByPizza(int idPizza) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT i.* FROM INGREDIENT i " +
                    "JOIN COMPO_PIZZA cp ON i.id_ingredient = cp.id_ingredient " +
                    "WHERE cp.id_pizza = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPizza);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ingredients.add(new Ingredient(
                    rs.getInt("id_ingredient"),
                    rs.getString("nom")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }
} 