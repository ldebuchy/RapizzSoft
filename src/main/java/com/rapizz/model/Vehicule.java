package com.rapizz.model;

public class Vehicule {
    private int id;
    private String modele;
    private String type;

    public Vehicule(int id, String modele, String type) {
        this.id = id;
        this.modele = modele;
        this.type = type;
    }

    public Vehicule(String modele, String type) {
        this.id = 0;
        this.modele = modele;
        this.type = type;
    }

    // Getters
    public int getId() { return id; }
    public String getModele() { return modele; }
    public String getType() { return type; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setModele(String modele) { this.modele = modele; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return modele + " (" + type + ")";
    }
} 