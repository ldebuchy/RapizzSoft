package com.rapizz.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.rapizz.dao.implementations.LivraisonDAOImpl;
import com.rapizz.dao.interfaces.LivraisonDAO;
import com.rapizz.model.Livraison;
import com.rapizz.utils.CommandeObserver;


import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class LivraisonsPanel extends JPanel {
    JTable table;
    LivraisonDAO livraisonDAO;
    private DefaultTableModel tableModel;
    private SimpleDateFormat dateFormat;

    public LivraisonsPanel() {
        livraisonDAO = new LivraisonDAOImpl();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        setLayout(new BorderLayout());

        // Création du modèle de table
        String[] columns = {"n°", "Date Commande", "Prix", "Statut", "Date Livraison", "Facturation", "Retard", "Client", "Livreur", "Véhicule", "Taille", "Pizza", "Note"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true); // Permet de trier les colonnes
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel des boutons
        JPanel buttonPanel = new JPanel();
        JButton ficheButton = new JButton("Fiche de livraison");
        buttonPanel.add(ficheButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ajout des écouteurs d'événements
        ficheButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une livraison pour voir sa fiche",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int id = (int) table.getValueAt(selectedRow, 0);
            Livraison l = livraisonDAO.read(id);
            FicheLivraisonDialog dialog = new FicheLivraisonDialog((Frame) SwingUtilities.getWindowAncestor(this), l);
            dialog.setVisible(true);
        });

        // Chargement initial des données
        refreshTable();

        // Ajouter un écouteur pour rafraîchir les données quand le panel devient visible
        addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                    refreshTable();
                }
            }
        });

        // S'inscrire comme observateur pour les changements de commandes
        CommandeObserver.getInstance().addObserver(() -> {
            refreshTable();
            // Rafraîchir aussi les panels de nouvelle commande
            for (Component comp : getParent().getComponents()) {
                if (comp instanceof NouvelleCommandePanel) {
                    ((NouvelleCommandePanel) comp).refreshAllData();
                }
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Livraison> livraisons = livraisonDAO.findAll();
        for (Livraison l : livraisons) {
            Object[] row = {
                l.getId(),
                dateFormat.format(l.getDateCommande()),
                String.format("%.2f €", l.getPrix()),
                l.getStatusLivraison(),
                l.getDateLivraison() != null ? dateFormat.format(l.getDateLivraison()) : "",
                l.isFacturation() ? "Oui" : "Non",
                l.isRetard() ? "Oui" : "Non",
                l.getClient().getNom() + " " + l.getClient().getPrenom(),
                l.getLivreur().getNom() + " " + l.getLivreur().getPrenom(),
                l.getVehicule().getModele(),
                l.getTaille().getNom(),
                l.getPizza().getNom(),
                l.getNote()
            };
            tableModel.addRow(row);
        }

        // Ajuster la largeur des colonnes
        for (int i = 0; i < table.getColumnCount(); i++) {
            int width = 100; // Largeur par défaut
            switch (i) {
                case 0: // n°
                    width = 40;
                    break;
                case 1: // Date Commande
                case 4: // Date Livraison
                    width = 120;
                    break;
                case 2: // Prix
                    width = 80;
                    break;
                case 3: // Statut
                    width = 100;
                    break;
                case 5: // Facturation
                    width = 80;
                    break;
                case 6: // Retard
                    width = 60;
                    break;
                case 7: // Client
                case 8: // Livreur
                    width = 150;
                    break;
                case 9: // Véhicule
                    width = 120;
                    break;
                case 10: // Taille
                    width = 80;
                    break;
                case 11: // Pizza
                    width = 150;
                    break;
                case 12: // Note
                    width = 200;
                    break;
            }
            table.getColumnModel().getColumn(i).setPreferredWidth(width);
        }
    }
} 