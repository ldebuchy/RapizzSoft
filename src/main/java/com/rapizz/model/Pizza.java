package com.rapizz.model;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private int id;
    private String nom;
    private double prixBase;
    private List<Ingredient> ingredients;

    // Constructeur pour la création (sans ID)
    public Pizza(String nom, double prixBase) {
        this.nom = nom;
        this.prixBase = prixBase;
        this.ingredients = new ArrayList<>();
    }

    // Constructeur pour la lecture depuis la base de données (avec ID)
    public Pizza(int id, String nom, double prixBase) {
        this.id = id;
        this.nom = nom;
        this.prixBase = prixBase;
        this.ingredients = new ArrayList<>();
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public double getPrixBase() { return prixBase; }
    public List<Ingredient> getIngredients() { return ingredients; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrixBase(double prixBase) { this.prixBase = prixBase; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public double getPrixPourTaille(String taille) {
        double coefficient = switch (taille.toLowerCase()) {
            case "petite" -> 1.0;
            case "moyenne" -> 1.2;
            case "grande" -> 1.5;
            default -> 1.0;
        };
        return prixBase * coefficient;
    }

    @Override
    public String toString() {
        return nom + " (" + prixBase + "€)";
    }
} 
