package com.rapizz.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.rapizz.dao.implementations.ClientDAOImpl;
import com.rapizz.dao.interfaces.ClientDAO;
import com.rapizz.model.Client;
import com.rapizz.utils.CommandeObserver;


import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.List;

public class ClientsPanel extends JPanel {
    private ClientDAO clientDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, rechargerButton;

    public ClientsPanel() {
        clientDAO = new ClientDAOImpl();
        setLayout(new BorderLayout());

        // Création du modèle de table
        String[] columns = {"Nom", "Prénom", "Téléphone", "Adresse", "Nombre de pizzas", "Solde"};
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
        addButton = new JButton("Ajouter");
        editButton = new JButton("Modifier");
        deleteButton = new JButton("Supprimer");
        rechargerButton = new JButton("Recharger le compte");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(rechargerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ajout des écouteurs d'événements
        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteSelectedClient());
        rechargerButton.addActionListener(e -> showRechargerDialog());

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
        CommandeObserver.getInstance().addObserver(this::refreshTable);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Client> clients = clientDAO.findAll();
        for (Client client : clients) {
            tableModel.addRow(new Object[]{
                client.getNom(),
                client.getPrenom(),
                client.getTelephone(),
                client.getAdresse(),
                client.getNbPizzasAchetees(),
                String.format("%.2f €", client.getSolde())
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
        JTextField prenomField = new JTextField();
        JTextField telephoneField = new JTextField();
        JTextField adresseField = new JTextField();
        JTextField soldeField = new JTextField("0.0");

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom:"));
        panel.add(prenomField);
        panel.add(new JLabel("Téléphone:"));
        panel.add(telephoneField);
        panel.add(new JLabel("Adresse:"));
        panel.add(adresseField);
        panel.add(new JLabel("Solde initial:"));
        panel.add(soldeField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Nouveau client", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nom = nomField.getText().trim();
                String prenom = prenomField.getText().trim();
                String telephone = telephoneField.getText().trim();
                String adresse = adresseField.getText().trim();
                double solde = Double.parseDouble(soldeField.getText().trim());

                if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty() || adresse.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (solde < 0) {
                    JOptionPane.showMessageDialog(this, "Le solde ne peut pas être négatif",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Client client = new Client(nom, prenom, telephone, adresse);
                client.setSolde(solde);
                clientDAO.create(client);
                refreshTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Le solde doit être un nombre",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à modifier",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String nom = (String) table.getValueAt(selectedRow, 0);
        String prenom = (String) table.getValueAt(selectedRow, 1);
        Client client = clientDAO.findByNomPrenom(nom, prenom);
        if (client == null) {
            JOptionPane.showMessageDialog(this, "Client non trouvé",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField nomField = new JTextField(client.getNom());
        JTextField prenomField = new JTextField(client.getPrenom());
        JTextField telephoneField = new JTextField(client.getTelephone());
        JTextField adresseField = new JTextField(client.getAdresse());
        JTextField soldeField = new JTextField(String.valueOf(client.getSolde()));

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom:"));
        panel.add(prenomField);
        panel.add(new JLabel("Téléphone:"));
        panel.add(telephoneField);
        panel.add(new JLabel("Adresse:"));
        panel.add(adresseField);
        panel.add(new JLabel("Solde:"));
        panel.add(soldeField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Modifier le client", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String newNom = nomField.getText().trim();
                String newPrenom = prenomField.getText().trim();
                String telephone = telephoneField.getText().trim();
                String adresse = adresseField.getText().trim();
                double solde = Double.parseDouble(soldeField.getText().trim());

                if (newNom.isEmpty() || newPrenom.isEmpty() || telephone.isEmpty() || adresse.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (solde < 0) {
                    JOptionPane.showMessageDialog(this, "Le solde ne peut pas être négatif",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                client.setNom(newNom);
                client.setPrenom(newPrenom);
                client.setTelephone(telephone);
                client.setAdresse(adresse);
                client.setSolde(solde);
                clientDAO.update(client);
                refreshTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Le solde doit être un nombre",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedClient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à supprimer",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String nom = (String) table.getValueAt(selectedRow, 0);
        String prenom = (String) table.getValueAt(selectedRow, 1);
        Client client = clientDAO.findByNomPrenom(nom, prenom);
        if (client == null) {
            JOptionPane.showMessageDialog(this, "Client non trouvé",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer ce client ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                clientDAO.delete(client.getId());
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Impossible de supprimer ce client car il est lié à une commande déjà effectuée ou à d'autres données.\n" +
                    "Veuillez d'abord supprimer ou modifier les éléments associés.",
                    "Erreur de suppression",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRechargerDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String nom = (String) table.getValueAt(selectedRow, 0);
        String prenom = (String) table.getValueAt(selectedRow, 1);
        Client client = clientDAO.findByNomPrenom(nom, prenom);
        if (client == null) {
            JOptionPane.showMessageDialog(this, "Client non trouvé",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField montantField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Montant à ajouter:"));
        panel.add(montantField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Recharger le compte", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double montant = Double.parseDouble(montantField.getText().trim());
                if (montant <= 0) {
                    JOptionPane.showMessageDialog(this, "Le montant doit être positif",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                client.setSolde(client.getSolde() + montant);
                clientDAO.update(client);
                refreshTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Le montant doit être un nombre",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 