package com.rapizz.model;

import java.util.Date;

/**
 * Une livraison de pizza
 */
public class Livraison {
    // Les informations de la livraison
    private int id;
    private Date dateCommande;
    private double prix;
    private String statusLivraison;
    private Date dateLivraison;
    private boolean facturation;
    private String note;
    private boolean retard;
    private Vehicule vehicule;
    private Livreur livreur;
    private Client client;
    private Taille taille;
    private Pizza pizza;

    // Pour créer une nouvelle livraison
    public Livraison(Client client, Pizza pizza, Taille taille, Livreur livreur, Vehicule vehicule, String note, boolean estGratuite) {
        this.id = 0; // L'ID sera mis par la base de données
        this.dateCommande = new Date();
        
        // Calcul du prix (arrondi à 2 chiffres après la virgule)
        double prixBase = pizza.getPrixBase() * taille.getCoefficient();
        this.prix = Math.round(prixBase * 100.0) / 100.0;
        
        this.statusLivraison = "En cours";
        this.dateLivraison = null;
        this.facturation = !estGratuite; // Si c'est gratuit, on ne facture pas
        this.note = note;
        this.retard = false;
        this.vehicule = vehicule;
        this.livreur = livreur;
        this.client = client;
        this.taille = taille;
        this.pizza = pizza;
    }

    // Pour lire une livraison depuis la base de données
    public Livraison(int id, Date dateCommande, double prix, String statusLivraison,
                    Date dateLivraison, boolean facturation, String note, boolean retard,
                    Vehicule vehicule, Livreur livreur, Client client, Taille taille, Pizza pizza) {
        this.id = id;
        this.dateCommande = dateCommande;
        this.prix = prix;
        this.statusLivraison = statusLivraison;
        this.dateLivraison = dateLivraison;
        this.facturation = facturation;
        this.note = note;
        this.retard = retard;
        this.vehicule = vehicule;
        this.livreur = livreur;
        this.client = client;
        this.taille = taille;
        this.pizza = pizza;
    }

    // Les getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Date getDateCommande() { return dateCommande; }
    public void setDateCommande(Date dateCommande) { this.dateCommande = dateCommande; }
    
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    
    public String getStatusLivraison() { return statusLivraison; }
    public void setStatusLivraison(String statusLivraison) { this.statusLivraison = statusLivraison; }
    
    public Date getDateLivraison() { return dateLivraison; }
    public void setDateLivraison(Date dateLivraison) { this.dateLivraison = dateLivraison; }
    
    public boolean isFacturation() { return facturation; }
    public void setFacturation(boolean facturation) { this.facturation = facturation; }
    
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    
    public boolean isRetard() { return retard; }
    public void setRetard(boolean retard) { this.retard = retard; }
    
    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }
    
    public Livreur getLivreur() { return livreur; }
    public void setLivreur(Livreur livreur) { this.livreur = livreur; }
    
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    
    public Taille getTaille() { return taille; }
    public void setTaille(Taille taille) { this.taille = taille; }
    
    public Pizza getPizza() { return pizza; }
    public void setPizza(Pizza pizza) { this.pizza = pizza; }

    // Pour afficher la livraison dans la console
    public String toString() {
        return "Livraison #" + id + " - " + pizza.getNom() + " (" + taille.getNom() + ") - " + client.toString();
    }
} 