package com.rapizz.ui;

import javax.swing.*;

import com.rapizz.dao.implementations.ClientDAOImpl;
import com.rapizz.dao.implementations.LivraisonDAOImpl;
import com.rapizz.dao.implementations.LivreurDAOImpl;
import com.rapizz.dao.implementations.PizzaDAOImpl;
import com.rapizz.dao.implementations.TailleDAOImpl;
import com.rapizz.dao.implementations.VehiculeDAOImpl;
import com.rapizz.dao.interfaces.ClientDAO;
import com.rapizz.dao.interfaces.LivraisonDAO;
import com.rapizz.dao.interfaces.LivreurDAO;
import com.rapizz.dao.interfaces.PizzaDAO;
import com.rapizz.dao.interfaces.TailleDAO;
import com.rapizz.dao.interfaces.VehiculeDAO;
import com.rapizz.model.*;
import com.rapizz.utils.CommandeObserver;


import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.List;

public class NouvelleCommandePanel extends JPanel {
    private ClientDAO clientDAO;
    private PizzaDAO pizzaDAO;
    private TailleDAO tailleDAO;
    private LivreurDAO livreurDAO;
    private LivraisonDAO livraisonDAO;
    private VehiculeDAO vehiculeDAO;
    private JComboBox<Client> clientComboBox;
    private JComboBox<Pizza> pizzaComboBox;
    private JComboBox<Taille> tailleComboBox;
    private JComboBox<Livreur> livreurComboBox;
    private JComboBox<Vehicule> vehiculeComboBox;
    private JLabel prixTotalLabel;
    private JTextArea noteArea;

    public NouvelleCommandePanel() {
        clientDAO = new ClientDAOImpl();
        pizzaDAO = new PizzaDAOImpl();
        tailleDAO = new TailleDAOImpl();
        livreurDAO = new LivreurDAOImpl();
        vehiculeDAO = new VehiculeDAOImpl();
        livraisonDAO = new LivraisonDAOImpl();

        setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(2, 2, 2, 2);
        mainGbc.fill = GridBagConstraints.HORIZONTAL;

        // Section Client
        JPanel clientPanel = new JPanel(new GridBagLayout());
        clientPanel.setBorder(BorderFactory.createTitledBorder("Client"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        clientPanel.add(new JLabel("Client:"), gbc);

        gbc.gridx = 1;
        clientComboBox = new JComboBox<>();
        clientComboBox.setPreferredSize(new Dimension(200, 25));
        clientPanel.add(clientComboBox, gbc);

        // Ajouter la section client au panel principal
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainPanel.add(clientPanel, mainGbc);

        // Section Pizza
        JPanel pizzaPanel = new JPanel(new GridBagLayout());
        pizzaPanel.setBorder(BorderFactory.createTitledBorder("Pizza"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pizzaPanel.add(new JLabel("Pizza:"), gbc);

        gbc.gridx = 1;
        pizzaComboBox = new JComboBox<>();
        pizzaComboBox.setPreferredSize(new Dimension(200, 25));
        pizzaPanel.add(pizzaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pizzaPanel.add(new JLabel("Taille:"), gbc);

        gbc.gridx = 1;
        tailleComboBox = new JComboBox<>();
        tailleComboBox.setPreferredSize(new Dimension(200, 25));
        pizzaPanel.add(tailleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pizzaPanel.add(new JLabel("Prix total:"), gbc);

        gbc.gridx = 1;
        prixTotalLabel = new JLabel("0.00 €");
        prixTotalLabel.setPreferredSize(new Dimension(200, 25));
        pizzaPanel.add(prixTotalLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton voirCarteButton = new JButton("Voir la carte");
        voirCarteButton.setPreferredSize(new Dimension(150, 25));
        pizzaPanel.add(voirCarteButton, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Ajouter la section pizza au panel principal
        mainGbc.gridy = 1;
        mainPanel.add(pizzaPanel, mainGbc);

        // Section Livraison
        JPanel livraisonPanel = new JPanel(new GridBagLayout());
        livraisonPanel.setBorder(BorderFactory.createTitledBorder("Livraison"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        livraisonPanel.add(new JLabel("Livreur:"), gbc);

        gbc.gridx = 1;
        livreurComboBox = new JComboBox<>();
        livreurComboBox.setPreferredSize(new Dimension(200, 25));
        livraisonPanel.add(livreurComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        livraisonPanel.add(new JLabel("Véhicule:"), gbc);

        gbc.gridx = 1;
        vehiculeComboBox = new JComboBox<>();
        vehiculeComboBox.setPreferredSize(new Dimension(200, 25));
        livraisonPanel.add(vehiculeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        livraisonPanel.add(new JLabel("Note:"), gbc);

        gbc.gridx = 1;
        noteArea = new JTextArea(3, 20);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        JScrollPane noteScroll = new JScrollPane(noteArea);
        noteScroll.setPreferredSize(new Dimension(200, 60));
        livraisonPanel.add(noteScroll, gbc);

        // Ajouter la section livraison au panel principal
        mainGbc.gridy = 2;
        mainPanel.add(livraisonPanel, mainGbc);

        // Bouton de commande
        JButton commanderButton = new JButton("Commander");
        commanderButton.setPreferredSize(new Dimension(150, 30));
        mainGbc.gridy = 3;
        mainGbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(commanderButton, mainGbc);

        // Ajouter le panel principal dans un scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Chargement des données
        refreshAllData();

        // Ajout des écouteurs d'événements
        clientComboBox.addActionListener(e -> updatePrixTotal());
        pizzaComboBox.addActionListener(e -> updatePrixTotal());
        tailleComboBox.addActionListener(e -> updatePrixTotal());
        livreurComboBox.addActionListener(e -> updatePrixTotal());
        vehiculeComboBox.addActionListener(e -> updatePrixTotal());
        commanderButton.addActionListener(e -> validerCommande());
        voirCarteButton.addActionListener(e -> {
            MenuPizzasDialog dialog = new MenuPizzasDialog((Frame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
        });

        // Ajouter un écouteur pour rafraîchir les données quand le panel devient visible
        addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                    refreshAllData();
                }
            }
        });

        // S'inscrire comme observateur pour les changements de commandes
        CommandeObserver.getInstance().addObserver(this::refreshAllData);
    }

    public void refreshAllData() {
        loadClients();
        loadPizzas();
        loadTailles();
        loadLivreurs();
        loadVehicules();
        updatePrixTotal();
    }

    private void loadClients() {
        clientComboBox.removeAllItems();
        List<Client> clients = clientDAO.findAll();
        for (Client client : clients) {
            clientComboBox.addItem(client);
        }
    }

    private void loadPizzas() {
        pizzaComboBox.removeAllItems();
        List<Pizza> pizzas = pizzaDAO.findAll();
        for (Pizza pizza : pizzas) {
            pizzaComboBox.addItem(pizza);
        }
    }

    private void loadTailles() {
        tailleComboBox.removeAllItems();
        List<Taille> tailles = tailleDAO.findAll();
        for (Taille taille : tailles) {
            tailleComboBox.addItem(taille);
        }
    }

    public void loadLivreurs() {
        livreurComboBox.removeAllItems();
        List<Livreur> livreurs = livreurDAO.findLivreursDisponibles();
        for (Livreur livreur : livreurs) {
            livreurComboBox.addItem(livreur);
        }
    }

    private void loadVehicules() {
        vehiculeComboBox.removeAllItems();
        List<Vehicule> vehicules = vehiculeDAO.findVehiculesDisponibles();
        for (Vehicule vehicule : vehicules) {
            vehiculeComboBox.addItem(vehicule);
        }
    }

    private void updatePrixTotal() {
        Pizza pizza = (Pizza) pizzaComboBox.getSelectedItem();
        Taille taille = (Taille) tailleComboBox.getSelectedItem();
        
        if (pizza != null && taille != null) {
            double prixTotal = pizza.getPrixBase() * taille.getCoefficient();
            prixTotalLabel.setText(String.format("%.2f €", prixTotal));
        } else {
            prixTotalLabel.setText("0.00 €");
        }
    }

    private void validerCommande() {
        // Vérifier qu'une pizza est sélectionnée
        if (pizzaComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une pizza",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifier qu'une taille est sélectionnée
        if (tailleComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une taille",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifier qu'un livreur est sélectionné
        if (livreurComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livreur",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifier qu'un véhicule est sélectionné
        if (vehiculeComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Récupérer les objets sélectionnés
        Pizza pizza = (Pizza) pizzaComboBox.getSelectedItem();
        Taille taille = (Taille) tailleComboBox.getSelectedItem();
        Livreur livreur = (Livreur) livreurComboBox.getSelectedItem();
        Client client = (Client) clientComboBox.getSelectedItem();
        Vehicule vehicule = (Vehicule) vehiculeComboBox.getSelectedItem();
        String note = noteArea.getText().trim();

        // Calculer le prix total
        double prixTotal = pizza.getPrixBase() * taille.getCoefficient();

        // Vérifier si le client a assez de solde
        if (client.getSolde() < prixTotal) {
            JOptionPane.showMessageDialog(this, "Solde insuffisant pour effectuer la commande",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Créer la livraison
        Livraison livraison = new Livraison(client, pizza, taille, livreur, vehicule, note, false);

        try {
            // Débiter le client
            client.setSolde(client.getSolde() - prixTotal);
            clientDAO.update(client);

            // Sauvegarder la livraison
            livraisonDAO.create(livraison);
            
            // Récupérer l'ID de la livraison créée
            int idLivraison = livraisonDAO.getLastId();

            // Rafraîchir les données
            refreshAllData();

            // Créer un message de confirmation détaillé
            String message = "";
            message += "Commande enregistrée avec succès !\n\n";
            message += "Détails de la commande :\n";
            message += "------------------------\n";
            message += "Numéro de commande : " + idLivraison + "\n";
            message += "Client : " + client.getNom() + " " + client.getPrenom() + "\n";
            message += "Pizza : " + pizza.getNom() + " (" + taille.getNom() + ")\n";
            message += "Prix : " + String.format("%.2f €", prixTotal) + "\n";
            message += "Livreur : " + livreur.getNom() + " " + livreur.getPrenom() + "\n";
            message += "Véhicule : " + vehicule.getModele() + "\n";
            message += "Solde client après commande : " + String.format("%.2f €", client.getSolde()) + "\n";
            if (!note.isEmpty()) {
                message += "Note : " + note + "\n";
            }
            // TODO: Ajouter le temps de livraison estimé

            // Afficher le message de confirmation
            JOptionPane.showMessageDialog(this, message,
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

            // Prévenir les autres fenêtres qu'il y a eu un changement
            CommandeObserver.getInstance().notifyObservers();
        } catch (Exception ex) {
            // En cas d'erreur, rembourser le client
            try {
                client.setSolde(client.getSolde() + prixTotal);
                clientDAO.update(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Erreur lors de la validation de la commande: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
} 