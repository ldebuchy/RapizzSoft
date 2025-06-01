package com.rapizz.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.rapizz.model.Ingredient;
import com.rapizz.model.Pizza;

public interface PizzaDAO {
    // Opérations CRUD de base
    void create(Pizza pizza);
    Pizza read(int id);
    void update(Pizza pizza);
    void delete(int id) throws SQLException;
    List<Pizza> findAll();
    Pizza findByName(String nom);

    // Opérations métier spécifiques
    List<Pizza> findPizzasByIngredient(int idIngredient);
    Pizza findPizzaLaPlusDemandee() throws SQLException;
    void addIngredientToPizza(int idPizza, int idIngredient);
    void removeIngredientFromPizza(int idPizza, int idIngredient);
    List<Ingredient> getIngredientsForPizza(int idPizza);
    double getPrixPourTaille(int idPizza, String taille);
    List<String> findIngredientsByPizza(int idPizza) throws SQLException;
} 