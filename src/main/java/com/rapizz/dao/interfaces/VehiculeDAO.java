package com.rapizz.dao.interfaces;

import java.util.List;

import com.rapizz.model.Vehicule;

public interface VehiculeDAO {
    // Opérations CRUD de base
    Vehicule create(Vehicule vehicule);
    Vehicule read(int id);
    List<Vehicule> findAll();
    List<Vehicule> findVehiculesDisponibles();
    Vehicule update(Vehicule vehicule);
    void delete(int id);

    // Opérations métier spécifiques
    List<Vehicule> findVehiculesJamaisUtilises();
    List<Vehicule> findVehiculesByType(String type);
    Vehicule findByModele(String modele);
} 