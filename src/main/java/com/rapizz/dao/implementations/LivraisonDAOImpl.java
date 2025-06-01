package com.rapizz.dao.implementations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.rapizz.dao.interfaces.ClientDAO;
import com.rapizz.dao.interfaces.LivraisonDAO;
import com.rapizz.dao.interfaces.LivreurDAO;
import com.rapizz.dao.interfaces.PizzaDAO;
import com.rapizz.dao.interfaces.TailleDAO;
import com.rapizz.dao.interfaces.VehiculeDAO;
import com.rapizz.model.Client;
import com.rapizz.model.Livraison;
import com.rapizz.model.Pizza;
import com.rapizz.utils.DatabaseConnection;

import java.util.Date;

public class LivraisonDAOImpl implements LivraisonDAO {
    private ClientDAO clientDAO;
    private LivreurDAO livreurDAO;
    private PizzaDAO pizzaDAO;
    private VehiculeDAO vehiculeDAO;
    private TailleDAO tailleDAO;

    public LivraisonDAOImpl() {
        this.clientDAO = new ClientDAOImpl();
        this.livreurDAO = new LivreurDAOImpl();
        this.pizzaDAO = new PizzaDAOImpl();
        this.vehiculeDAO = new VehiculeDAOImpl();
        this.tailleDAO = new TailleDAOImpl();
    }

    @Override
    public void create(Livraison livraison) {
        String sql = "INSERT INTO LIVRAISON (id_livraison, date_commande, prix, status_livraison, date_livraison, facturation, note, retard, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livraison.getId());
            stmt.setTimestamp(2, new java.sql.Timestamp(livraison.getDateCommande().getTime()));
            stmt.setDouble(3, livraison.getPrix());
            stmt.setString(4, livraison.getStatusLivraison());
            stmt.setTimestamp(5, livraison.getDateLivraison() != null ? new java.sql.Timestamp(livraison.getDateLivraison().getTime()) : null);
            stmt.setBoolean(6, livraison.isFacturation());
            stmt.setString(7, livraison.getNote());
            stmt.setBoolean(8, livraison.isRetard());
            stmt.setInt(9, livraison.getVehicule().getId());
            stmt.setInt(10, livraison.getLivreur().getId());
            stmt.setInt(11, livraison.getClient().getId());
            stmt.setInt(12, livraison.getTaille().getId());
            stmt.setInt(13, livraison.getPizza().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Livraison read(int id) {
        String sql = "SELECT * FROM LIVRAISON WHERE id_livraison = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return createLivraisonFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Livraison livraison) {
        String sql = "UPDATE LIVRAISON SET date_commande = ?, prix = ?, status_livraison = ?, date_livraison = ?, facturation = ?, note = ?, retard = ?, id_vehicule = ?, id_livreur = ?, id_client = ?, id_taille = ?, id_pizza = ? WHERE id_livraison = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new java.sql.Timestamp(livraison.getDateCommande().getTime()));
            stmt.setDouble(2, livraison.getPrix());
            stmt.setString(3, livraison.getStatusLivraison());
            stmt.setTimestamp(4, livraison.getDateLivraison() != null ? new java.sql.Timestamp(livraison.getDateLivraison().getTime()) : null);
            stmt.setBoolean(5, livraison.isFacturation());
            stmt.setString(6, livraison.getNote());
            stmt.setBoolean(7, livraison.isRetard());
            stmt.setInt(8, livraison.getVehicule().getId());
            stmt.setInt(9, livraison.getLivreur().getId());
            stmt.setInt(10, livraison.getClient().getId());
            stmt.setInt(11, livraison.getTaille().getId());
            stmt.setInt(12, livraison.getPizza().getId());
            stmt.setInt(13, livraison.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM LIVRAISON WHERE id_livraison = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Livraison> findAll() {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT * FROM LIVRAISON";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livraisons.add(createLivraisonFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livraisons;
    }

    @Override
    public List<Livraison> findLivraisonsEnCours() {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT * FROM LIVRAISON WHERE status_livraison = 'En cours'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livraisons.add(createLivraisonFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livraisons;
    }

    @Override
    public List<Livraison> findLivraisonsByClient(int idClient) {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT * FROM LIVRAISON WHERE id_client = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                livraisons.add(createLivraisonFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livraisons;
    }

    @Override
    public List<Livraison> findLivraisonsByLivreur(int idLivreur) {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT * FROM LIVRAISON WHERE id_livreur = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivreur);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                livraisons.add(createLivraisonFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livraisons;
    }

    @Override
    public List<Livraison> findLivraisonsByDate(Date date) {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT * FROM LIVRAISON WHERE DATE(date_commande) = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                livraisons.add(createLivraisonFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livraisons;
    }

    @Override
    public void marquerCommeLivree(int idLivraison, boolean retard) {
        // Récupérer la livraison avant de la mettre à jour
        Livraison livraison = read(idLivraison);
        if (livraison == null) {
            return;
        }

        // Vérifier si c'est la 10ème pizza du client
        int nbPizzasLivrees = getNombrePizzasAchetees(livraison.getClient().getId());
        boolean estGratuite = nbPizzasLivrees == 9;

        String sql = "UPDATE LIVRAISON SET status_livraison = 'Livrée', date_livraison = NOW(), " +
                    "retard = ?, facturation = ? WHERE id_livraison = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, retard);
            // Si retard ou si c'est la 10ème pizza, pas de facturation
            stmt.setBoolean(2, !retard && !estGratuite);
            stmt.setInt(3, idLivraison);
            stmt.executeUpdate();

            if (retard) {
                // Incrémenter le nombre de retards du livreur
                livreurDAO.incrementerRetards(livraison.getLivreur().getId());
                
                // Rembourser le client
                Client client = livraison.getClient();
                client.setSolde(client.getSolde() + livraison.getPrix());
                clientDAO.update(client);
            }

            // Mettre à jour le compteur de pizzas du client
            Client client = livraison.getClient();
            client.setNbPizzasAchetees(client.getNbPizzasAchetees() + 1);
            clientDAO.update(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void annulerLivraison(int idLivraison) {
        // Récupérer la livraison avant de l'annuler
        Livraison livraison = read(idLivraison);
        if (livraison != null) {
            // Rembourser le client
            Client client = livraison.getClient();
            client.setSolde(client.getSolde() + livraison.getPrix());
            clientDAO.update(client);
        }

        String sql = "UPDATE LIVRAISON SET status_livraison = 'Annulée' WHERE id_livraison = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivraison);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double calculerPrixLivraison(int idPizza, String taille, boolean estGratuite) {
        if (estGratuite) {
            return 0.0;
        }
        Pizza pizza = pizzaDAO.read(idPizza);
        if (pizza != null) {
            return pizza.getPrixPourTaille(taille);
        }
        return 0.0;
    }

    @Override
    public double getSoldeClient(int idClient) {
        String sql = "SELECT SUM(prix) as solde FROM LIVRAISON WHERE id_client = ? AND facturation = true";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("solde");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public int getNombrePizzasAchetees(int idClient) {
        String sql = "SELECT nb_pizzas_achetees FROM CLIENT WHERE id_client = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("nb_pizzas_achetees");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean verifierSoldeSuffisant(int idClient, double montant) {
        return getSoldeClient(idClient) >= montant;
    }

    @Override
    public int getLastId() {
        String sql = "SELECT MAX(id_livraison) as last_id FROM LIVRAISON";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("last_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Livraison createLivraisonFromResultSet(ResultSet rs) throws SQLException {
        return new Livraison(
            rs.getInt("id_livraison"),
            rs.getTimestamp("date_commande"),
            rs.getDouble("prix"),
            rs.getString("status_livraison"),
            rs.getTimestamp("date_livraison"),
            rs.getBoolean("facturation"),
            rs.getString("note"),
            rs.getBoolean("retard"),
            vehiculeDAO.read(rs.getInt("id_vehicule")),
            livreurDAO.read(rs.getInt("id_livreur")),
            clientDAO.read(rs.getInt("id_client")),
            tailleDAO.read(rs.getInt("id_taille")),
            pizzaDAO.read(rs.getInt("id_pizza"))
        );
    }
} 