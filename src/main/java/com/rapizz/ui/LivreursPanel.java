package com.rapizz.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.rapizz.dao.implementations.LivreurDAOImpl;
import com.rapizz.dao.interfaces.LivreurDAO;
import com.rapizz.model.Livreur;
import com.rapizz.utils.CommandeObserver;


import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.List;

public class LivreursPanel extends JPanel {
    private JTable livreurTable;
    private DefaultTableModel tableModel;
    private LivreurDAO livreurDAO;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    public LivreursPanel() {
        setLayout(new BorderLayout());
        livreurDAO = new LivreurDAOImpl();

        // Création du modèle de table
        String[] columns = {"Nom", "Prénom", "Téléphone", "Retards"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        livreurTable = new JTable(tableModel);
        livreurTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(livreurTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Ajouter");
        editButton = new JButton("Modifier");
        deleteButton = new JButton("Supprimer");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ajout des écouteurs d'événements
        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteLivreur());

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
        try {
            // Vider la table
            tableModel.setRowCount(0);
            
            // Charger les données
            List<Livreur> livreurs = livreurDAO.findAll();
            for (Livreur livreur : livreurs) {
                Object[] row = {
                    livreur.getNom(),
                    livreur.getPrenom(),
                    livreur.getTelephone(),
                    livreur.getNbRetards()
                };
                tableModel.addRow(row);
            }
            adjustColumnWidths();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du rafraîchissement des données : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adjustColumnWidths() {
        // Ajuster la largeur des colonnes en fonction du contenu
        for (int column = 0; column < livreurTable.getColumnCount(); column++) {
            int width = 50; // Largeur minimale
            for (int row = 0; row < livreurTable.getRowCount(); row++) {
                TableCellRenderer renderer = livreurTable.getCellRenderer(row, column);
                Component comp = livreurTable.prepareRenderer(renderer, row, column);
                width = Math.max(width, comp.getPreferredSize().width + 20); // Ajouter un peu de padding
            }
            // Définir une largeur maximale pour éviter les colonnes trop larges
            width = Math.min(width, 300);
            livreurTable.getColumnModel().getColumn(column).setPreferredWidth(width);
        }
    }

    private void showAddDialog() {
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField telephoneField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom:"));
        panel.add(prenomField);
        panel.add(new JLabel("Téléphone:"));
        panel.add(telephoneField);

        int result = JOptionPane.showConfirmDialog(this, panel,
            "Nouveau livreur", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Livreur livreur = new Livreur(
                    0, // L'ID sera généré par la base de données
                    nomField.getText(),
                    prenomField.getText(),
                    telephoneField.getText(),
                    0 // Nombre de retards initial
                );
                livreurDAO.create(livreur);
                refreshTable();
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialog() {
        int selectedRow = livreurTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livreur à modifier.");
            return;
        }

        String nom = (String) tableModel.getValueAt(selectedRow, 0);
        Livreur livreur = livreurDAO.findByNom(nom);
        if (livreur == null) {
            JOptionPane.showMessageDialog(this, "Livreur non trouvé.");
            return;
        }

        JTextField txtNom = new JTextField(livreur.getNom());
        JTextField txtPrenom = new JTextField(livreur.getPrenom());
        JTextField txtTelephone = new JTextField(livreur.getTelephone());

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Nom:"));
        panel.add(txtNom);
        panel.add(new JLabel("Prénom:"));
        panel.add(txtPrenom);
        panel.add(new JLabel("Téléphone:"));
        panel.add(txtTelephone);

        int result = JOptionPane.showConfirmDialog(this, panel,
            "Modifier le livreur", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            livreur.setNom(txtNom.getText());
            livreur.setPrenom(txtPrenom.getText());
            livreur.setTelephone(txtTelephone.getText());
            livreurDAO.update(livreur);
            refreshTable();
        }
    }

    private void deleteLivreur() {
        int selectedRow = livreurTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livreur à supprimer.");
            return;
        }

        String nom = (String) tableModel.getValueAt(selectedRow, 0);
        Livreur livreur = livreurDAO.findByNom(nom);
        if (livreur == null) {
            JOptionPane.showMessageDialog(this, "Livreur non trouvé.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer ce livreur ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                livreurDAO.delete(livreur.getId());
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Impossible de supprimer ce livreur car il est lié à une commande déjà effectuée ou à d'autres données.\n" +
                    "Veuillez d'abord supprimer ou modifier les éléments associés.",
                    "Erreur de suppression",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 