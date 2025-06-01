package com.rapizz.dao.implementations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.rapizz.dao.interfaces.PizzaDAO;
import com.rapizz.model.Ingredient;
import com.rapizz.model.Pizza;
import com.rapizz.utils.DatabaseConnection;

public class PizzaDAOImpl implements PizzaDAO {
    @Override
    public void create(Pizza pizza) {
        String sql = "INSERT INTO PIZZA (nom, prix_base) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pizza.getNom());
            stmt.setDouble(2, pizza.getPrixBase());
            stmt.executeUpdate();

            // Récupération de l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pizza.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création de la pizza a échoué, aucun ID généré.");
                }
            }

            // Ajout des ingrédients
            for (Ingredient ingredient : pizza.getIngredients()) {
                addIngredientToPizza(pizza.getId(), ingredient.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pizza read(int id) {
        String sql = "SELECT * FROM PIZZA WHERE id_pizza = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pizza pizza = new Pizza(
                    rs.getInt("id_pizza"),
                    rs.getString("nom"),
                    rs.getDouble("prix_base")
                );
                pizza.setIngredients(getIngredientsForPizza(id));
                return pizza;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Pizza pizza) {
        String sql = "UPDATE PIZZA SET nom = ?, prix_base = ? WHERE id_pizza = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pizza.getNom());
            stmt.setDouble(2, pizza.getPrixBase());
            stmt.setInt(3, pizza.getId());
            stmt.executeUpdate();

            // Mise à jour des ingrédients
            // Suppression des anciens ingrédients
            String deleteSql = "DELETE FROM COMPO_PIZZA WHERE id_pizza = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, pizza.getId());
                deleteStmt.executeUpdate();
            }

            // Ajout des nouveaux ingrédients
            for (Ingredient ingredient : pizza.getIngredients()) {
                addIngredientToPizza(pizza.getId(), ingredient.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Suppression des ingrédients associés
            String deleteCompoSql = "DELETE FROM COMPO_PIZZA WHERE id_pizza = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteCompoSql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            // Suppression de la pizza
            String sql = "DELETE FROM PIZZA WHERE id_pizza = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public List<Pizza> findAll() {
        List<Pizza> pizzas = new ArrayList<>();
        String sql = "SELECT p.*, i.id_ingredient, i.nom as nom_ingredient " +
                    "FROM PIZZA p " +
                    "LEFT JOIN COMPO_PIZZA cp ON p.id_pizza = cp.id_pizza " +
                    "LEFT JOIN INGREDIENT i ON cp.id_ingredient = i.id_ingredient " +
                    "ORDER BY p.id_pizza";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            Pizza currentPizza = null;
            while (rs.next()) {
                int pizzaId = rs.getInt("id_pizza");
                
                if (currentPizza == null || currentPizza.getId() != pizzaId) {
                    if (currentPizza != null) {
                        pizzas.add(currentPizza);
                    }
                    currentPizza = new Pizza(
                        pizzaId,
                        rs.getString("nom"),
                        rs.getDouble("prix_base")
                    );
                }
                
                // Ajouter l'ingrédient s'il existe
                if (rs.getInt("id_ingredient") != 0) {
                    currentPizza.addIngredient(new Ingredient(
                        rs.getInt("id_ingredient"),
                        rs.getString("nom_ingredient")
                    ));
                }
            }
            
            // Ajouter la dernière pizza
            if (currentPizza != null) {
                pizzas.add(currentPizza);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pizzas;
    }

    @Override
    public List<Pizza> findPizzasByIngredient(int idIngredient) {
        List<Pizza> pizzas = new ArrayList<>();
        String sql = "SELECT p.*, i.id_ingredient, i.nom as nom_ingredient " +
                    "FROM PIZZA p " +
                    "JOIN COMPO_PIZZA cp ON p.id_pizza = cp.id_pizza " +
                    "JOIN INGREDIENT i ON cp.id_ingredient = i.id_ingredient " +
                    "WHERE cp.id_ingredient = ? " +
                    "ORDER BY p.id_pizza";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idIngredient);
            ResultSet rs = stmt.executeQuery();
            
            Pizza currentPizza = null;
            while (rs.next()) {
                int pizzaId = rs.getInt("id_pizza");
                
                if (currentPizza == null || currentPizza.getId() != pizzaId) {
                    if (currentPizza != null) {
                        pizzas.add(currentPizza);
                    }
                    currentPizza = new Pizza(
                        pizzaId,
                        rs.getString("nom"),
                        rs.getDouble("prix_base")
                    );
                }
                
                // Ajouter l'ingrédient s'il existe
                if (rs.getInt("id_ingredient") != 0) {
                    currentPizza.addIngredient(new Ingredient(
                        rs.getInt("id_ingredient"),
                        rs.getString("nom_ingredient")
                    ));
                }
            }
            
            // Ajouter la dernière pizza
            if (currentPizza != null) {
                pizzas.add(currentPizza);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pizzas;
    }

    @Override
    public Pizza findPizzaLaPlusDemandee() throws SQLException {
        String sql = "SELECT p.*, COUNT(l.ID_LIVRAISON) as nb_commandes " +
                    "FROM PIZZA p " +
                    "LEFT JOIN LIVRAISON l ON p.ID_PIZZA = l.ID_PIZZA " +
                    "GROUP BY p.ID_PIZZA " +
                    "ORDER BY nb_commandes DESC " +
                    "LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                Pizza pizza = new Pizza(
                    rs.getInt("ID_PIZZA"),
                    rs.getString("NOM"),
                    rs.getDouble("PRIX_BASE")
                );
                pizza.setIngredients(getIngredientsForPizza(pizza.getId()));
                return pizza;
            }
            return null;
        }
    }

    @Override
    public List<String> findIngredientsByPizza(int idPizza) throws SQLException {
        String sql = "SELECT i.NOM FROM INGREDIENT i " +
                    "JOIN COMPO_PIZZA cp ON i.ID_INGREDIENT = cp.ID_INGREDIENT " +
                    "WHERE cp.ID_PIZZA = ?";
        List<String> ingredients = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPizza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(rs.getString("NOM"));
                }
            }
            return ingredients;
        }
    }

    @Override
    public void addIngredientToPizza(int idPizza, int idIngredient) {
        String sql = "INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPizza);
            stmt.setInt(2, idIngredient);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeIngredientFromPizza(int idPizza, int idIngredient) {
        String sql = "DELETE FROM COMPO_PIZZA WHERE id_pizza = ? AND id_ingredient = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPizza);
            stmt.setInt(2, idIngredient);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ingredient> getIngredientsForPizza(int pizzaId) {
        List<Ingredient> ingredients = new ArrayList<>();
        String query = "SELECT i.* FROM INGREDIENT i " +
                      "JOIN COMPO_PIZZA cp ON i.id_ingredient = cp.id_ingredient " +
                      "WHERE cp.id_pizza = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, pizzaId);
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

    @Override
    public double getPrixPourTaille(int idPizza, String taille) {
        Pizza pizza = read(idPizza);
        if (pizza != null) {
            return pizza.getPrixPourTaille(taille);
        }
        return 0.0;
    }

    @Override
    public Pizza findByName(String nom) {
        String sql = "SELECT * FROM PIZZA WHERE nom = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Pizza(
                    rs.getInt("id_pizza"),
                    rs.getString("nom"),
                    rs.getDouble("prix_base")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 