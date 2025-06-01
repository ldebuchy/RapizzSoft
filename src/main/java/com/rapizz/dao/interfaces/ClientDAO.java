package com.rapizz.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.rapizz.model.Client;

public interface ClientDAO {
    // Opérations CRUD de base
    void create(Client client);
    Client read(int id);
    void update(Client client);
    void delete(int id) throws java.sql.SQLException;
    List<Client> findAll();

    // Opérations métier spécifiques
    Client findMeilleurClient() throws SQLException;
    List<Client> findClientsAboveAverage();
    Client findByNomPrenom(String nom, String prenom);
} 