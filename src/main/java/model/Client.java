package model;

public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private float solde;

    public Client(int id, String nom, String prenom, String telephone, float solde) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.solde = solde;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getTelephone() { return telephone; }
    public float getSolde() { return solde; }
}