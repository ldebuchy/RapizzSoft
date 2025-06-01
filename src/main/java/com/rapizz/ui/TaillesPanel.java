package com.rapizz.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.rapizz.dao.implementations.TailleDAOImpl;
import com.rapizz.dao.interfaces.TailleDAO;
import com.rapizz.model.Taille;

import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.List;

public class TaillesPanel extends JPanel {
    private TailleDAO tailleDAO;
    private DefaultTableModel tableModel;
    private JTable table;

    public TaillesPanel() {
        tailleDAO = new TailleDAOImpl();
        setLayout(new BorderLayout());

        // Création du modèle de table
        String[] columns = {"Nom", "Coefficient"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel des boutons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ajout des écouteurs d'événements
        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteSelectedTaille());

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
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Taille> tailles = tailleDAO.findAll();
        for (Taille taille : tailles) {
            tableModel.addRow(new Object[]{
                taille.getNom(),
                taille.getCoefficient()
            });
        }
        adjustColumnWidths();
    }

    private void adjustColumnWidths() {
        // Ajuster la largeur des colonnes en fonction du contenu
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Largeur minimale
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(width, comp.getPreferredSize().width + 20); // Ajouter un peu de padding
            }
            // Définir une largeur maximale pour éviter les colonnes trop larges
            width = Math.min(width, 300);
            table.getColumnModel().getColumn(column).setPreferredWidth(width);
        }
    }

    private void showAddDialog() {
        JTextField nomField = new JTextField();
        JTextField coefficientField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Coefficient:"));
        panel.add(coefficientField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Nouvelle taille", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nom = nomField.getText().trim();
                double coefficient = Double.parseDouble(coefficientField.getText().trim());

                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Le nom ne peut pas être vide",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Taille taille = new Taille(0, nom, coefficient);
                tailleDAO.create(taille);
                refreshTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Le coefficient doit être un nombre",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une taille à modifier",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        Taille taille = tailleDAO.read(id);
        if (taille == null) {
            JOptionPane.showMessageDialog(this, "Taille non trouvée",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField nomField = new JTextField(taille.getNom());
        JTextField coefficientField = new JTextField(String.valueOf(taille.getCoefficient()));

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Coefficient:"));
        panel.add(coefficientField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Modifier la taille", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nom = nomField.getText().trim();
                double coefficient = Double.parseDouble(coefficientField.getText().trim());

                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Le nom ne peut pas être vide",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                taille.setNom(nom);
                taille.setCoefficient(coefficient);
                tailleDAO.update(taille);
                refreshTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Le coefficient doit être un nombre",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedTaille() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une taille à supprimer",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cette taille ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                tailleDAO.delete(id);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Impossible de supprimer cette taille car elle est utilisée dans des pizzas ou livraisons.\n" +
                    "Veuillez d'abord supprimer ou modifier les éléments associés.",
                    "Erreur de suppression",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 
