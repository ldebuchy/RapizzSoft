package model;

public class Pizza {
    private int idPizza;
    private String nom;
    private double prix;

    public Pizza(int idPizza, String nom, double prix) {
        this.idPizza = idPizza;
        this.nom = nom;
        this.prix = prix;
    }

    public int getIdPizza() {
        return idPizza;
    }
    public String getNom() {
        return nom;
    }
    public double getPrix() {
        return prix;
    }
    public void setIdPizza(int idPizza) {
        this.idPizza = idPizza;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setPrix(double prix) {
        this.prix = prix;
    }
}

