package com.rapizz.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.rapizz.dao.implementations.IngredientDAOImpl;
import com.rapizz.dao.implementations.PizzaDAOImpl;
import com.rapizz.dao.implementations.TailleDAOImpl;
import com.rapizz.dao.interfaces.IngredientDAO;
import com.rapizz.dao.interfaces.PizzaDAO;
import com.rapizz.dao.interfaces.TailleDAO;
import com.rapizz.model.Ingredient;
import com.rapizz.model.Pizza;
import com.rapizz.model.Taille;

import java.awt.*;
import java.util.List;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.sql.SQLException;

public class PizzasPanel extends JPanel {
    private PizzaDAO pizzaDAO;
    private IngredientDAO ingredientDAO;
    private TailleDAO tailleDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnGestionIngredients, btnGestionTailles;

    public PizzasPanel() {
        pizzaDAO = new PizzaDAOImpl();
        ingredientDAO = new IngredientDAOImpl();
        tailleDAO = new TailleDAOImpl();
        setLayout(new BorderLayout());

        // Création de la table
        String[] columns = {"Nom", "Prix de base", "Ingrédients"};
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
        btnAdd = new JButton("Ajouter");
        btnEdit = new JButton("Modifier");
        btnDelete = new JButton("Supprimer");
        btnGestionIngredients = new JButton("Gérer les ingrédients");
        btnGestionTailles = new JButton("Gérer les tailles");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnGestionIngredients);
        buttonPanel.add(btnGestionTailles);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ajout des écouteurs d'événements
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelectedPizza());
        btnGestionIngredients.addActionListener(e -> showGestionIngredientsDialog());
        btnGestionTailles.addActionListener(e -> showGestionTaillesDialog());

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
        try {
            List<Pizza> pizzas = pizzaDAO.findAll();
            for (Pizza pizza : pizzas) {
                List<String> ingredients = pizzaDAO.findIngredientsByPizza(pizza.getId());
                String ingredientsStr = ingredients != null && !ingredients.isEmpty() 
                    ? String.join(", ", ingredients) 
                    : "Aucun ingrédient";
                tableModel.addRow(new Object[]{
                    pizza.getNom(),
                    String.format("%.2f €", pizza.getPrixBase()),
                    ingredientsStr
                });
            }
            adjustColumnWidths();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des pizzas : " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
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
        JTextField txtNom = new JTextField();
        JTextField txtPrixBase = new JTextField();
        
        // Création de la liste des ingrédients
        DefaultListModel<Ingredient> listModel = new DefaultListModel<>();
        JList<Ingredient> ingredientList = new JList<>(listModel);
        ingredientList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane ingredientScroll = new JScrollPane(ingredientList);
        ingredientScroll.setPreferredSize(new Dimension(300, 150));

        // Charger les ingrédients
        try {
            List<Ingredient> ingredients = ingredientDAO.findAll();
            for (Ingredient ingredient : ingredients) {
                listModel.addElement(ingredient);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des ingrédients : " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel pour les champs de texte
        JPanel textPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        textPanel.add(new JLabel("Nom de la pizza:"));
        textPanel.add(txtNom);
        textPanel.add(new JLabel("Prix de base (max 999.99 €):"));
        textPanel.add(txtPrixBase);
        mainPanel.add(textPanel, BorderLayout.NORTH);

        // Panel pour la liste des ingrédients
        JPanel listPanel = new JPanel(new BorderLayout(5, 5));
        listPanel.add(new JLabel("Ingrédients:"), BorderLayout.NORTH);
        listPanel.add(ingredientScroll, BorderLayout.CENTER);
        mainPanel.add(listPanel, BorderLayout.CENTER);

        int option = JOptionPane.showConfirmDialog(this, mainPanel, "Nouvelle pizza",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nom = txtNom.getText().trim();
                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Le nom ne peut pas être vide.",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String prixBaseStr = txtPrixBase.getText().replace(',', '.');
                double prixBase = Double.parseDouble(prixBaseStr);
                if (prixBase <= 0 || prixBase > 999.99) {
                    JOptionPane.showMessageDialog(this, "Le prix de base doit être compris entre 0 et 999.99 €.",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Pizza pizza = new Pizza(nom, prixBase);
                pizzaDAO.create(pizza);

                // Ajouter les ingrédients sélectionnés
                List<Ingredient> selectedIngredients = ingredientList.getSelectedValuesList();
                for (Ingredient ingredient : selectedIngredients) {
                    pizzaDAO.addIngredientToPizza(pizza.getId(), ingredient.getId());
                }

                refreshTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer une valeur numérique valide pour le prix de base (utilisez . ou , comme séparateur décimal).",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création de la pizza : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une pizza à modifier.");
            return;
        }

        String nom = (String) table.getValueAt(selectedRow, 0);
        Pizza pizza = pizzaDAO.findByName(nom);
        if (pizza == null) {
            JOptionPane.showMessageDialog(this, "Pizza non trouvée.");
            return;
        }

        JTextField txtNom = new JTextField(pizza.getNom());
        JTextField txtPrixBase = new JTextField(String.valueOf(pizza.getPrixBase()));

        // Création de la liste des ingrédients
        DefaultListModel<Ingredient> listModel = new DefaultListModel<>();
        JList<Ingredient> ingredientList = new JList<>(listModel);
        ingredientList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane ingredientScroll = new JScrollPane(ingredientList);
        ingredientScroll.setPreferredSize(new Dimension(300, 150));

        // Charger les ingrédients
        try {
            List<Ingredient> allIngredients = ingredientDAO.findAll();
            for (Ingredient ingredient : allIngredients) {
                listModel.addElement(ingredient);
            }

            // Sélectionner les ingrédients actuels de la pizza
            List<String> currentIngredients = pizzaDAO.findIngredientsByPizza(pizza.getId());
            for (int i = 0; i < listModel.getSize(); i++) {
                Ingredient ingredient = listModel.getElementAt(i);
                if (currentIngredients.contains(ingredient.getNom())) {
                    ingredientList.addSelectionInterval(i, i);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des ingrédients : " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel pour les champs de texte
        JPanel textPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        textPanel.add(new JLabel("Nom de la pizza:"));
        textPanel.add(txtNom);
        textPanel.add(new JLabel("Prix de base (max 999.99 €):"));
        textPanel.add(txtPrixBase);
        mainPanel.add(textPanel, BorderLayout.NORTH);

        // Panel pour la liste des ingrédients
        JPanel listPanel = new JPanel(new BorderLayout(5, 5));
        listPanel.add(new JLabel("Ingrédients:"), BorderLayout.NORTH);
        listPanel.add(ingredientScroll, BorderLayout.CENTER);
        mainPanel.add(listPanel, BorderLayout.CENTER);

        int option = JOptionPane.showConfirmDialog(this, mainPanel, "Modifier la pizza",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String newNom = txtNom.getText().trim();
                if (newNom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Le nom ne peut pas être vide.",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String prixBaseStr = txtPrixBase.getText().replace(',', '.');
                double prixBase = Double.parseDouble(prixBaseStr);
                if (prixBase <= 0 || prixBase > 999.99) {
                    JOptionPane.showMessageDialog(this, "Le prix de base doit être compris entre 0 et 999.99 €.",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                pizza.setNom(newNom);
                pizza.setPrixBase(prixBase);
                pizzaDAO.update(pizza);

                // Mettre à jour les ingrédients
                List<Ingredient> currentIngredients = pizzaDAO.getIngredientsForPizza(pizza.getId());
                for (Ingredient ingredient : currentIngredients) {
                    pizzaDAO.removeIngredientFromPizza(pizza.getId(), ingredient.getId());
                }

                List<Ingredient> selectedIngredients = ingredientList.getSelectedValuesList();
                for (Ingredient ingredient : selectedIngredients) {
                    pizzaDAO.addIngredientToPizza(pizza.getId(), ingredient.getId());
                }

                refreshTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer une valeur numérique valide pour le prix de base (utilisez . ou , comme séparateur décimal).",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification de la pizza : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedPizza() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une pizza à supprimer.");
            return;
        }

        String nom = (String) table.getValueAt(selectedRow, 0);
        Pizza pizza = pizzaDAO.findByName(nom);
        if (pizza == null) {
            JOptionPane.showMessageDialog(this, "Pizza non trouvée.");
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cette pizza ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                pizzaDAO.delete(pizza.getId());
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Impossible de supprimer cette pizza car elle est utilisée dans une commande déjà effectuée (livraison).\n" +
                    "Veuillez d'abord supprimer ou modifier les livraisons associées.",
                    "Erreur de suppression",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showGestionIngredientsDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Gestion des ingrédients", true);
        dialog.setLayout(new BorderLayout());

        // Table des ingrédients
        String[] columns = {"Nom"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable ingredientsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(ingredientsTable);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Panel des boutons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton refreshButton = new JButton("Rafraîchir");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Fonction de rafraîchissement
        Runnable refreshTable = () -> {
            model.setRowCount(0);
            try {
                List<Ingredient> ingredients = ingredientDAO.findAll();
                for (Ingredient ingredient : ingredients) {
                    model.addRow(new Object[]{ingredient.getNom()});
                }
                // Ajuster la largeur de la colonne
                ingredientsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur lors du chargement des ingrédients : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        };

        // Écouteurs d'événements
        addButton.addActionListener(e -> {
            String nom = JOptionPane.showInputDialog(dialog, "Nom de l'ingrédient:");
            if (nom != null && !nom.trim().isEmpty()) {
                try {
                    ingredientDAO.create(new Ingredient(nom.trim()));
                    refreshTable.run();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Erreur lors de l'ajout de l'ingrédient : " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = ingredientsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog,
                    "Veuillez sélectionner un ingrédient à modifier",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            String currentNom = (String) ingredientsTable.getValueAt(selectedRow, 0);
            String newNom = JOptionPane.showInputDialog(dialog, "Nouveau nom:", currentNom);
            if (newNom != null && !newNom.trim().isEmpty()) {
                try {
                    // Trouver l'ingrédient par son nom
                    List<Ingredient> ingredients = ingredientDAO.findAll();
                    Ingredient ingredientToUpdate = null;
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient.getNom().equals(currentNom)) {
                            ingredientToUpdate = ingredient;
                            break;
                        }
                    }
                    
                    if (ingredientToUpdate != null) {
                        ingredientToUpdate.setNom(newNom.trim());
                        ingredientDAO.update(ingredientToUpdate);
                        refreshTable.run();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Erreur lors de la modification de l'ingrédient : " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = ingredientsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog,
                    "Veuillez sélectionner un ingrédient à supprimer",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nom = (String) ingredientsTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(dialog,
                "Êtes-vous sûr de vouloir supprimer l'ingrédient '" + nom + "' ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Trouver l'ingrédient par son nom
                    List<Ingredient> ingredients = ingredientDAO.findAll();
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient.getNom().equals(nom)) {
                            ingredientDAO.delete(ingredient.getId());
                            break;
                        }
                    }
                    refreshTable.run();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Erreur lors de la suppression de l'ingrédient : " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        refreshButton.addActionListener(e -> refreshTable.run());

        // Chargement initial des données
        refreshTable.run();

        // Afficher la boîte de dialogue
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showGestionTaillesDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Gestion des tailles", true);
        dialog.setLayout(new BorderLayout());

        // Création du modèle de table
        String[] columns = {"Nom", "Coefficient"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Panel des boutons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Charger les tailles
        List<Taille> tailles = tailleDAO.findAll();
        for (Taille taille : tailles) {
            tableModel.addRow(new Object[]{
                taille.getNom(),
                String.format("%.2f", taille.getCoefficient())
            });
        }

        // Ajout des écouteurs d'événements
        addButton.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField nomField = new JTextField();
            JTextField coefficientField = new JTextField();
            panel.add(new JLabel("Nom:"));
            panel.add(nomField);
            panel.add(new JLabel("Coefficient:"));
            panel.add(coefficientField);

            int result = JOptionPane.showConfirmDialog(dialog, panel,
                    "Ajouter une taille", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String nom = nomField.getText().trim();
                String coefficientStr = coefficientField.getText().trim();

                if (nom.isEmpty() || coefficientStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Remplacer la virgule par un point pour le parsing
                    String coefficientStrNormalized = coefficientStr.replace(',', '.');
                    double coefficient = Double.parseDouble(coefficientStrNormalized);
                    if (coefficient < 0.1 || coefficient > 10.0) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Le coefficient doit être compris entre 0.1 et 10.0",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Taille taille = new Taille(0, nom, coefficient);
                    tailleDAO.create(taille);
                    tableModel.addRow(new Object[]{nom, String.format("%.2f", coefficient)});
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Le coefficient doit être un nombre valide (utilisez . ou , comme séparateur décimal)",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Veuillez sélectionner une taille à modifier",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String nom = (String) table.getValueAt(selectedRow, 0);
            Taille taille = tailleDAO.findByNom(nom);
            if (taille == null) {
                JOptionPane.showMessageDialog(dialog, "Impossible de récupérer les informations de la taille",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField nomField = new JTextField(taille.getNom());
            JTextField coefficientField = new JTextField(String.format("%.2f", taille.getCoefficient()));
            panel.add(new JLabel("Nom:"));
            panel.add(nomField);
            panel.add(new JLabel("Coefficient:"));
            panel.add(coefficientField);

            int result = JOptionPane.showConfirmDialog(dialog, panel,
                    "Modifier la taille", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String newNom = nomField.getText().trim();
                String coefficientStr = coefficientField.getText().trim();

                if (newNom.isEmpty() || coefficientStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Remplacer la virgule par un point pour le parsing
                    String coefficientStrNormalized = coefficientStr.replace(',', '.');
                    double coefficient = Double.parseDouble(coefficientStrNormalized);
                    if (coefficient < 0.1 || coefficient > 10.0) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Le coefficient doit être compris entre 0.1 et 10.0",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    taille.setNom(newNom);
                    taille.setCoefficient(coefficient);
                    tailleDAO.update(taille);
                    table.setValueAt(newNom, selectedRow, 0);
                    table.setValueAt(String.format("%.2f", coefficient), selectedRow, 1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Le coefficient doit être un nombre valide (utilisez . ou , comme séparateur décimal)",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Veuillez sélectionner une taille à supprimer",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String nom = (String) table.getValueAt(selectedRow, 0);
            Taille taille = tailleDAO.findByNom(nom);
            if (taille == null) {
                JOptionPane.showMessageDialog(dialog, "Impossible de récupérer les informations de la taille",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Êtes-vous sûr de vouloir supprimer la taille '" + nom + "' ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    tailleDAO.delete(taille.getId());
                    tableModel.removeRow(selectedRow);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Impossible de supprimer cette taille car elle est utilisée dans une commande déjà effectuée ou dans une pizza.\n" +
                            "Veuillez d'abord supprimer ou modifier les éléments associés.",
                            "Erreur de suppression",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
} 