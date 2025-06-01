package com.rapizz.dao.interfaces;

import java.util.List;

import com.rapizz.model.Livraison;

import java.util.Date;

public interface LivraisonDAO {
    // Opérations CRUD de base
    void create(Livraison livraison);
    Livraison read(int id);
    void update(Livraison livraison);
    void delete(int id);
    List<Livraison> findAll();

    // Opérations métier spécifiques
    List<Livraison> findLivraisonsEnCours();
    List<Livraison> findLivraisonsByClient(int idClient);
    List<Livraison> findLivraisonsByLivreur(int idLivreur);
    List<Livraison> findLivraisonsByDate(Date date);
    void marquerCommeLivree(int idLivraison, boolean retard);
    void annulerLivraison(int idLivraison);
    double calculerPrixLivraison(int idPizza, String taille, boolean estGratuite);
    
    // Gestion du solde et des pizzas achetées
    double getSoldeClient(int idClient);
    int getNombrePizzasAchetees(int idClient);
    boolean verifierSoldeSuffisant(int idClient, double montant);
    
    // Récupération de l'ID de la dernière livraison
    int getLastId();
} 