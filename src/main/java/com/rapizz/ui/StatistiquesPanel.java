package com.rapizz.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.rapizz.dao.implementations.ClientDAOImpl;
import com.rapizz.dao.implementations.IngredientDAOImpl;
import com.rapizz.dao.implementations.LivraisonDAOImpl;
import com.rapizz.dao.implementations.LivreurDAOImpl;
import com.rapizz.dao.implementations.PizzaDAOImpl;
import com.rapizz.dao.implementations.VehiculeDAOImpl;
import com.rapizz.dao.interfaces.LivraisonDAO;
import com.rapizz.model.*;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StatistiquesPanel extends JPanel {
    private JLabel meilleurClientNomPrenomLabel;
    private JLabel meilleurClientPizzasLabel;
    private JLabel meilleurClientSoldeLabel;
    private JLabel pireLivreurNomPrenomLabel;
    private JLabel pireLivreurRetardsLabel;
    private JLabel pizzaPopNomLabel;
    private JLabel pizzaPopPrixLabel;
    private JLabel pizzaPopIngredientsLabel;
    private JLabel ingredientPopulaireLabel;
    private JLabel vehiculesJamaisServiLabel;
    private JLabel nbCommandesParClientLabel;
    private JLabel moyenneCommandesLabel;
    private JLabel clientsAuDessusMoyenneLabel;
    private JLabel chiffreAffairesLabel;
    private JList<String> statsList;
    private JPanel detailPanel;
    private DefaultListModel<String> statsListModel;
    private String[] statsTitles = {
        "Chiffre d'affaires total",
        "Véhicules jamais utilisés pour une livraison",
        "Nombre de commandes par client (détail)",
        "Nombre moyen de commandes par client",
        "Clients ayant commandé plus que la moyenne",
        "Meilleur client (fidélité)",
        "Livreur ayant le plus de retards",
        "Pizza la plus populaire",
        "Ingrédient le plus utilisé"
    };

    public StatistiquesPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        statsListModel = new DefaultListModel<>();
        for (String s : statsTitles) statsListModel.addElement(s);
        statsList = new JList<>(statsListModel);
        statsList.setFont(new Font("Arial", Font.PLAIN, 15));
        statsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        statsList.setSelectedIndex(0);
        JScrollPane listScroll = new JScrollPane(statsList);
        listScroll.setPreferredSize(new Dimension(220, 0));
        detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Initialisation de tous les labels pour éviter le NPE
        meilleurClientNomPrenomLabel = new JLabel();
        meilleurClientPizzasLabel = new JLabel();
        meilleurClientSoldeLabel = new JLabel();
        pireLivreurNomPrenomLabel = new JLabel();
        pireLivreurRetardsLabel = new JLabel();
        pizzaPopNomLabel = new JLabel();
        pizzaPopPrixLabel = new JLabel();
        pizzaPopIngredientsLabel = new JLabel();
        ingredientPopulaireLabel = new JLabel();
        vehiculesJamaisServiLabel = new JLabel();
        nbCommandesParClientLabel = new JLabel();
        moyenneCommandesLabel = new JLabel();
        clientsAuDessusMoyenneLabel = new JLabel();
        chiffreAffairesLabel = new JLabel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, detailPanel);
        splitPane.setDividerLocation(220);
        splitPane.setResizeWeight(0);
        add(splitPane, BorderLayout.CENTER);
        refreshData();
        statsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                showStatDetail(statsList.getSelectedIndex());
            }
        });
        showStatDetail(0);
    }

    private void showStatDetail(int index) {
        detailPanel.removeAll();
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        JLabel title = new JLabel(statsTitles[index]);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(20));
        // Ajout d'une explication pour certains cas
        switch (index) {
            case 0: // Chiffre d'affaires
                content.add(centeredLabel("Somme totale des livraisons facturées."));
                content.add(Box.createVerticalStrut(10));
                content.add(centeredLabel(chiffreAffairesLabel.getText()));
                break;
            case 1: // Véhicules jamais utilisés
                content.add(centeredLabel("Liste des véhicules qui n'ont jamais servi à une livraison."));
                content.add(Box.createVerticalStrut(10));
                content.add(centeredLabel(vehiculesJamaisServiLabel.getText()));
                break;
            case 2: // Commandes par client
                content.add(centeredLabel("Nombre de commandes passées par chaque client."));
                content.add(Box.createVerticalStrut(10));
                JTextArea area = new JTextArea(nbCommandesParClientLabel.getText());
                area.setEditable(false);
                area.setFont(new Font("Arial", Font.PLAIN, 15));
                area.setBackground(new Color(245,245,245));
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                JScrollPane scroll = new JScrollPane(area);
                scroll.setPreferredSize(new Dimension(350, 80));
                content.add(scroll);
                break;
            case 3: // Moyenne commandes/client
                content.add(centeredLabel("Moyenne du nombre de commandes par client (tous clients confondus)."));
                content.add(Box.createVerticalStrut(10));
                content.add(centeredLabel(moyenneCommandesLabel.getText()));
                break;
            case 4: // Clients ayant commandé plus que la moyenne
                content.add(centeredLabel("Liste des clients ayant passé plus de commandes que la moyenne des clients."));
                content.add(Box.createVerticalStrut(10));
                JTextArea area2 = new JTextArea(clientsAuDessusMoyenneLabel.getText());
                area2.setEditable(false);
                area2.setFont(new Font("Arial", Font.PLAIN, 15));
                area2.setBackground(new Color(245,245,245));
                area2.setLineWrap(true);
                area2.setWrapStyleWord(true);
                JScrollPane scroll2 = new JScrollPane(area2);
                scroll2.setPreferredSize(new Dimension(350, 80));
                content.add(scroll2);
                break;
            case 5: // Meilleur Client
                content.add(centeredLabel("Client ayant acheté le plus de pizzas."));
                content.add(Box.createVerticalStrut(10));
                content.add(centeredLabel(meilleurClientNomPrenomLabel.getText()));
                content.add(centeredLabel(meilleurClientPizzasLabel.getText()));
                content.add(centeredLabel(meilleurClientSoldeLabel.getText()));
                break;
            case 6: // Pire Livreur
                content.add(centeredLabel("Livreur ayant eu le plus de retards sur ses livraisons."));
                content.add(Box.createVerticalStrut(10));
                content.add(centeredLabel(pireLivreurNomPrenomLabel.getText()));
                content.add(centeredLabel(pireLivreurRetardsLabel.getText()));
                break;
            case 7: // Pizza la Plus Populaire
                content.add(centeredLabel("Pizza la plus commandée par les clients."));
                content.add(Box.createVerticalStrut(10));
                content.add(centeredLabel(pizzaPopNomLabel.getText()));
                content.add(centeredLabel(pizzaPopPrixLabel.getText()));
                content.add(centeredLabel(pizzaPopIngredientsLabel.getText()));
                break;
            case 8: // Ingrédient le Plus Populaire
                content.add(centeredLabel("Ingrédient le plus utilisé dans les pizzas commandées."));
                content.add(Box.createVerticalStrut(10));
                content.add(centeredLabel(ingredientPopulaireLabel.getText()));
                break;
        }
        detailPanel.add(content, BorderLayout.CENTER);
        detailPanel.revalidate();
        detailPanel.repaint();
    }

    private JLabel centeredLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 16));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }
    
    private void refreshData() {
        try {
            // Chiffre d'affaires
            LivraisonDAO livraisonDAO = new LivraisonDAOImpl();
            double chiffreAffaires = 0.0;
            List<Livraison> livraisons = livraisonDAO.findAll();
            for (Livraison l : livraisons) {
                if (l.isFacturation()) {
                    chiffreAffaires += l.getPrix();
                }
            }
            chiffreAffairesLabel.setText(String.format("%.2f €", chiffreAffaires));
            
            // Véhicules jamais utilisés
            VehiculeDAOImpl vehiculeDAO = new VehiculeDAOImpl();
            List<Vehicule> vehiculesJamais = vehiculeDAO.findVehiculesJamaisUtilises();
            if (vehiculesJamais.isEmpty()) {
                vehiculesJamaisServiLabel.setText("Aucun");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Vehicule v : vehiculesJamais) {
                    sb.append(v.getModele()).append(", ");
                }
                vehiculesJamaisServiLabel.setText(sb.substring(0, sb.length() - 2));
            }
            
            // Nombre de commandes par client (moyenne et détail)
            ClientDAOImpl clientDAO = new ClientDAOImpl();
            List<Client> clients = clientDAO.findAll();
            int totalCommandes = 0;
            StringBuilder commandesParClient = new StringBuilder();
            for (Client c : clients) {
                int nbCmd = 0;
                for (Livraison l : livraisons) {
                    if (l.getClient() != null && l.getClient().getId() == c.getId()) nbCmd++;
                }
                commandesParClient.append(c.getPrenom()).append(" ").append(c.getNom()).append(": ").append(nbCmd).append(" | ");
                totalCommandes += nbCmd;
            }
            nbCommandesParClientLabel.setText(commandesParClient.length() > 0 ? commandesParClient.substring(0, commandesParClient.length() - 3) : "Aucune");
            
            // Moyenne des commandes
            double moyenne = clients.isEmpty() ? 0 : (double) totalCommandes / clients.size();
            moyenneCommandesLabel.setText(String.format("%.2f", moyenne));
            
            // Clients ayant commandé plus que la moyenne
            List<Client> clientsAboveAvg = clientDAO.findClientsAboveAverage();
            if (clientsAboveAvg.isEmpty()) {
                clientsAuDessusMoyenneLabel.setText("Aucun");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Client c : clientsAboveAvg) {
                    sb.append(c.getPrenom()).append(" ").append(c.getNom()).append(", ");
                }
                clientsAuDessusMoyenneLabel.setText(sb.substring(0, sb.length() - 2));
            }
            
            // Meilleur Client
            Client meilleurClient = clientDAO.findMeilleurClient();
            if (meilleurClient != null) {
                meilleurClientNomPrenomLabel.setText(meilleurClient.getPrenom() + " " + meilleurClient.getNom());
                meilleurClientPizzasLabel.setText(meilleurClient.getNbPizzasAchetees() + " pizzas achetées");
                meilleurClientSoldeLabel.setText("Solde : " + String.format("%.2f €", meilleurClient.getSolde()));
            } else {
                meilleurClientNomPrenomLabel.setText("Aucun client trouvé");
                meilleurClientPizzasLabel.setText("");
                meilleurClientSoldeLabel.setText("");
            }
            
            // Pire Livreur
            LivreurDAOImpl livreurDAO = new LivreurDAOImpl();
            Livreur pireLivreur = livreurDAO.findPireLivreur();
            if (pireLivreur != null) {
                pireLivreurNomPrenomLabel.setText(pireLivreur.getPrenom() + " " + pireLivreur.getNom());
                pireLivreurRetardsLabel.setText(pireLivreur.getNbRetards() + " retards");
            } else {
                pireLivreurNomPrenomLabel.setText("Aucun livreur trouvé");
                pireLivreurRetardsLabel.setText("");
            }
            
            // Pizza Populaire
            PizzaDAOImpl pizzaDAO = new PizzaDAOImpl();
            Pizza pizzaPopulaire = pizzaDAO.findPizzaLaPlusDemandee();
            if (pizzaPopulaire != null) {
                pizzaPopNomLabel.setText(pizzaPopulaire.getNom());
                pizzaPopPrixLabel.setText(String.format("%.2f €", pizzaPopulaire.getPrixBase()));
                List<String> ingredients = pizzaDAO.findIngredientsByPizza(pizzaPopulaire.getId());
                String ingredientsStr = ingredients != null && !ingredients.isEmpty() 
                    ? "Ingrédients : " + String.join(", ", ingredients) 
                    : "Aucun ingrédient";
                pizzaPopIngredientsLabel.setText(ingredientsStr);
            } else {
                pizzaPopNomLabel.setText("Aucune pizza trouvée");
                pizzaPopPrixLabel.setText("");
                pizzaPopIngredientsLabel.setText("");
            }
            
            // Ingrédient Populaire
            IngredientDAOImpl ingredientDAO = new IngredientDAOImpl();
            Ingredient ingredientPopulaire = ingredientDAO.findIngredientLePlusUtilise();
            if (ingredientPopulaire != null) {
                ingredientPopulaireLabel.setText(ingredientPopulaire.getNom());
            } else {
                ingredientPopulaireLabel.setText("Aucun ingrédient trouvé");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la récupération des statistiques : " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
        showStatDetail(statsList.getSelectedIndex());
    }
} 