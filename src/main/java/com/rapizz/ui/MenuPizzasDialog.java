package com.rapizz.ui;

import com.rapizz.dao.implementations.PizzaDAOImpl;
import com.rapizz.dao.interfaces.PizzaDAO;
import com.rapizz.model.Pizza;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuPizzasDialog extends JDialog {
    public MenuPizzasDialog(Frame parent) {
        super(parent, "Carte des Pizzas", true);
        setSize(450, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Carte des Pizzas");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        PizzaDAO pizzaDAO = new PizzaDAOImpl();
        List<Pizza> pizzas;
        try {
            pizzas = pizzaDAO.findAll();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des pizzas : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            pizzas = java.util.Collections.emptyList();
        }

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);

        for (Pizza pizza : pizzas) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel nom = new JLabel(pizza.getNom());
            nom.setFont(new Font("SansSerif", Font.BOLD, 16));
            nom.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(nom);

            JLabel prix = new JLabel(String.format("%.2f €", pizza.getPrixBase()));
            prix.setFont(new Font("SansSerif", Font.PLAIN, 15));
            prix.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(prix);

            List<String> ingredients;
            try {
                ingredients = pizzaDAO.findIngredientsByPizza(pizza.getId());
            } catch (Exception ex) {
                ingredients = java.util.Collections.singletonList("Erreur ingrédients");
            }
            String ingredientsStr = ingredients != null && !ingredients.isEmpty() ? String.join(", ", ingredients) : "Aucun ingrédient";
            JLabel ing = new JLabel(ingredientsStr);
            ing.setFont(new Font("SansSerif", Font.PLAIN, 12));
            ing.setForeground(new Color(80, 80, 80));
            ing.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
            ing.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(ing);

            menuPanel.add(card);
            menuPanel.add(Box.createVerticalStrut(6));
        }

        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Fermer");
        closeButton.addActionListener(e -> dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(closeButton);
        add(btnPanel, BorderLayout.SOUTH);
    }
} 