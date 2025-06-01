package com.rapizz.model;

public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    private int nbPizzasAchetees;
    private double solde;

    // Constructeur pour la création (sans ID)
    public Client(String nom, String prenom, String telephone, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.nbPizzasAchetees = 0;
        this.solde = 0.0;
    }

    // Constructeur pour la lecture depuis la base de données (avec ID)
    public Client(int id, String nom, String prenom, String telephone, String adresse, int nbPizzasAchetees, double solde) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.nbPizzasAchetees = nbPizzasAchetees;
        this.solde = solde;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getTelephone() { return telephone; }
    public String getAdresse() { return adresse; }
    public int getNbPizzasAchetees() { return nbPizzasAchetees; }
    public double getSolde() { return solde; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setNbPizzasAchetees(int nbPizzasAchetees) { this.nbPizzasAchetees = nbPizzasAchetees; }
    public void setSolde(double solde) { this.solde = solde; }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }
} 