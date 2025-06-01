package com.rapizz.dao.interfaces;

import java.util.List;

import com.rapizz.model.Livreur;

public interface LivreurDAO {
    // Opérations CRUD de base
    void create(Livreur livreur);
    Livreur read(int id);
    void update(Livreur livreur);
    void delete(int id) throws java.sql.SQLException;
    List<Livreur> findAll();
    Livreur findByNom(String nom);

    // Opérations métier spécifiques
    void incrementerRetards(int idLivreur);
    Livreur findPireLivreur();
    List<Livreur> findLivreursDisponibles();
    int getNombreRetards(int idLivreur);
    Livreur findLivreurAvecPlusDeRetards();
} 