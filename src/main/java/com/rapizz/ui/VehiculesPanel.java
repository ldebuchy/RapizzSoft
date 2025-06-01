package com.rapizz.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.rapizz.dao.implementations.VehiculeDAOImpl;
import com.rapizz.dao.interfaces.VehiculeDAO;
import com.rapizz.model.Vehicule;


import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.List;

public class VehiculesPanel extends JPanel {
    private VehiculeDAO vehiculeDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public VehiculesPanel() {
        vehiculeDAO = new VehiculeDAOImpl();
        setLayout(new BorderLayout());

        // Création du modèle de table
        String[] columns = {"Modèle", "Type"};
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
        deleteButton.addActionListener(e -> deleteSelectedVehicule());

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
        List<Vehicule> vehicules = vehiculeDAO.findAll();
        for (Vehicule v : vehicules) {
            tableModel.addRow(new Object[]{
                v.getModele(),
                v.getType()
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
        JTextField modeleField = new JTextField();
        JTextField typeField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Modèle:"));
        panel.add(modeleField);
        panel.add(new JLabel("Type:"));
        panel.add(typeField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter un véhicule",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Vehicule vehicule = new Vehicule(
                    modeleField.getText(),
                    typeField.getText()
                );
                vehiculeDAO.create(vehicule);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du véhicule: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule à modifier",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String modele = (String) table.getValueAt(selectedRow, 0);
        Vehicule vehicule = vehiculeDAO.findByModele(modele);
        if (vehicule == null) {
            JOptionPane.showMessageDialog(this, "Véhicule non trouvé",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField modeleField = new JTextField(vehicule.getModele());
        JTextField typeField = new JTextField(vehicule.getType());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Modèle:"));
        panel.add(modeleField);
        panel.add(new JLabel("Type:"));
        panel.add(typeField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modifier le véhicule",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                vehicule.setModele(modeleField.getText());
                vehicule.setType(typeField.getText());
                vehiculeDAO.update(vehicule);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du véhicule: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedVehicule() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule à supprimer.");
            return;
        }

        String modele = (String) table.getValueAt(selectedRow, 0);
        Vehicule vehicule = vehiculeDAO.findByModele(modele);
        if (vehicule == null) {
            JOptionPane.showMessageDialog(this, "Véhicule non trouvé.");
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer ce véhicule ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                vehiculeDAO.delete(vehicule.getId());
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Impossible de supprimer ce véhicule car il est utilisé dans une commande déjà effectuée (livraison).\n" +
                    "Veuillez d'abord supprimer ou modifier les livraisons associées.",
                    "Erreur de suppression",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 