package com.rapizz.ui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private ClientsPanel clientsPanel;
    private PizzasPanel pizzasPanel;
    private LivreursPanel livreursPanel;
    private VehiculesPanel vehiculesPanel;
    private LivraisonsPanel livraisonsPanel;
    private NouvelleCommandePanel nouvelleCommandePanel;
    private SuiviCommandesPanel suiviCommandesPanel;
    private StatistiquesPanel statistiquesPanel;

    public MainWindow() {
        setTitle("RaPizzSoft");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,500);
        setLocationRelativeTo(null);

        // Chargement de l'icône
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/icon.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'icône : " + e.getMessage());
        }

        // Création du menu
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(menuBar.getWidth(), 30)); // Augmentation de la hauteur du menu
        
        // Menu Commandes
        JMenu menuCommandes = new JMenu("Commandes");
        JMenuItem menuNouvelleCommande = new JMenuItem("Nouvelle Commande");
        JMenuItem menuSuiviCommandes = new JMenuItem("Suivi des Commandes");
        menuCommandes.add(menuNouvelleCommande);
        menuCommandes.add(menuSuiviCommandes);
        
        // Menu Gestion
        JMenu menuGestion = new JMenu("Gestion");
        JMenuItem menuClients = new JMenuItem("Clients");
        JMenuItem menuPizzas = new JMenuItem("Pizzas");
        JMenuItem menuLivreurs = new JMenuItem("Livreurs");
        JMenuItem menuVehicules = new JMenuItem("Véhicules");
        menuGestion.add(menuClients);
        menuGestion.add(menuPizzas);
        menuGestion.add(menuLivreurs);
        menuGestion.add(menuVehicules);
        
        // Menu Historique
        JMenu menuHistorique = new JMenu("Historique");
        JMenuItem menuLivraisons = new JMenuItem("Historique des livraisons");
        menuHistorique.add(menuLivraisons);
        
        // Menu Statistiques
        JMenu menuStats = new JMenu("Statistiques");
        JMenuItem menuStatistiques = new JMenuItem("Voir les statistiques");
        menuStats.add(menuStatistiques);
        
        menuBar.add(menuCommandes);
        menuBar.add(menuGestion);
        menuBar.add(menuHistorique);
        menuBar.add(menuStats);
        setJMenuBar(menuBar);

        // Panel principal avec CardLayout
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        add(mainPanel);

        // Initialisation des panels
        clientsPanel = new ClientsPanel();
        pizzasPanel = new PizzasPanel();
        livreursPanel = new LivreursPanel();
        vehiculesPanel = new VehiculesPanel();
        livraisonsPanel = new LivraisonsPanel();
        nouvelleCommandePanel = new NouvelleCommandePanel();
        suiviCommandesPanel = new SuiviCommandesPanel();
        statistiquesPanel = new StatistiquesPanel();
        
        mainPanel.add(clientsPanel, "clients");
        mainPanel.add(pizzasPanel, "pizzas");
        mainPanel.add(livreursPanel, "livreurs");
        mainPanel.add(vehiculesPanel, "vehicules");
        mainPanel.add(livraisonsPanel, "livraisons");
        mainPanel.add(nouvelleCommandePanel, "nouvelleCommande");
        mainPanel.add(suiviCommandesPanel, "suiviCommandes");
        mainPanel.add(statistiquesPanel, "statistiques");

        // Ajout des écouteurs d'événements
        menuClients.addActionListener(e -> showClientsPanel());
        menuPizzas.addActionListener(e -> showPizzasPanel());
        menuLivreurs.addActionListener(e -> showLivreursPanel());
        menuVehicules.addActionListener(e -> showVehiculesPanel());
        menuLivraisons.addActionListener(e -> showLivraisonsPanel());
        menuNouvelleCommande.addActionListener(e -> showNouvelleCommandePanel());
        menuSuiviCommandes.addActionListener(e -> showSuiviCommandesPanel());
        menuStatistiques.addActionListener(e -> showStatistiquesPanel());

        // Afficher le panel Nouvelle Commande par défaut
        showNouvelleCommandePanel();
    }

    private void showClientsPanel() {
        cardLayout.show(mainPanel, "clients");
        updateWindowTitle("Gestion des Clients");
    }

    private void showPizzasPanel() {
        cardLayout.show(mainPanel, "pizzas");
        updateWindowTitle("Gestion des Pizzas");
    }

    private void showLivreursPanel() {
        cardLayout.show(mainPanel, "livreurs");
        updateWindowTitle("Gestion des Livreurs");
    }

    private void showVehiculesPanel() {
        cardLayout.show(mainPanel, "vehicules");
        updateWindowTitle("Gestion des Véhicules");
    }

    private void showLivraisonsPanel() {
        cardLayout.show(mainPanel, "livraisons");
        updateWindowTitle("Historique des Livraisons");
    }

    private void showNouvelleCommandePanel() {
        cardLayout.show(mainPanel, "nouvelleCommande");
        updateWindowTitle("Nouvelle Commande");
    }

    private void showSuiviCommandesPanel() {
        cardLayout.show(mainPanel, "suiviCommandes");
        updateWindowTitle("Suivi des Commandes");
    }

    private void showStatistiquesPanel() {
        cardLayout.show(mainPanel, "statistiques");
        updateWindowTitle("Statistiques");
    }

    private void updateWindowTitle(String panelTitle) {
        setTitle("RaPizzSoft - " + panelTitle);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
} 