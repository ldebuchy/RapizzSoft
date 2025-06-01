package com.rapizz.dao.interfaces;

import java.util.List;

import com.rapizz.model.Taille;

public interface TailleDAO {
    void create(Taille taille);
    Taille read(int id);
    void update(Taille taille);
    void delete(int id) throws java.sql.SQLException;
    List<Taille> findAll();
    Taille findByNom(String nom);
} 
