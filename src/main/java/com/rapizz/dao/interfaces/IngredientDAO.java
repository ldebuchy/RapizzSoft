package com.rapizz.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.rapizz.model.Ingredient;

public interface IngredientDAO {
    // Opérations CRUD de base
    void create(Ingredient ingredient);
    Ingredient read(int id);
    void update(Ingredient ingredient);
    void delete(int id);
    List<Ingredient> findAll();

    // Opérations métier spécifiques
    Ingredient findIngredientLePlusUtilise() throws SQLException;
    List<Ingredient> findIngredientsByPizza(int idPizza);
} 