package com.rapizz.model;

public class Ingredient {
    private int id;
    private String nom;

    // Constructeur pour la création (sans ID)
    public Ingredient(String nom) {
        this.nom = nom;
    }

    // Constructeur pour la lecture depuis la base de données (avec ID)
    public Ingredient(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public String toString() {
        return nom;
    }
} 
