package com.rapizz.model;

public class Livreur {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private int nbRetards;

    public Livreur(int id, String nom, String prenom, String telephone, int nbRetards) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.nbRetards = nbRetards;
    }

    public Livreur(int id, String nom, String telephone) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
    }

    public Livreur(String nom, String telephone) {
        this(0, nom, telephone);
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getTelephone() { return telephone; }
    public int getNbRetards() { return nbRetards; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setNbRetards(int nbRetards) { this.nbRetards = nbRetards; }

    public void incrementerRetards() {
        this.nbRetards++;
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }
} 
