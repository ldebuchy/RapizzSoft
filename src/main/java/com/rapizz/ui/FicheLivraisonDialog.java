package com.rapizz.ui;

import com.rapizz.model.Livraison;
import com.rapizz.utils.PrinterUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FicheLivraisonDialog extends JDialog {
    private JPanel contentPanel;

    public FicheLivraisonDialog(Frame parent, Livraison livraison) {
        super(parent, "Fiche de Livraison", true);
        setSize(420, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(16, 16, 16, 16),
            BorderFactory.createLineBorder(new Color(180, 180, 180), 2)
        ));

        // Titre centré
        JLabel titleLabel = new JLabel("FICHE DE LIVRAISON", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Ligne de séparation
        JSeparator separator = new JSeparator();
        contentPanel.add(separator, BorderLayout.AFTER_LAST_LINE);

        // Grille des infos
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        addRow(gridPanel, gbc, "Livreur :", livraison.getLivreur().getNom() + " " + livraison.getLivreur().getPrenom());
        addRow(gridPanel, gbc, "Véhicule :", livraison.getVehicule().getType() + " (" + livraison.getVehicule().getModele() + ")");
        addRow(gridPanel, gbc, "Client :", livraison.getClient().getNom() + " " + livraison.getClient().getPrenom());
        addRow(gridPanel, gbc, "Date commande :", sdf.format(livraison.getDateCommande()));
        addRow(gridPanel, gbc, "Retard :", livraison.isRetard() ? "Oui" : "Non");
        addRow(gridPanel, gbc, "Pizza :", livraison.getPizza().getNom());
        addRow(gridPanel, gbc, "Prix de base :", String.format("%.2f €", livraison.getPizza().getPrixBase()));

        contentPanel.add(gridPanel, BorderLayout.CENTER);

        // Date d'impression en bas
        JLabel datePrintLabel = new JLabel("Imprimé le : " + sdf.format(new Date()), SwingConstants.RIGHT);
        datePrintLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        datePrintLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        contentPanel.add(datePrintLabel, BorderLayout.SOUTH);

        // Panel boutons
        JPanel btnPanel = new JPanel();
        JButton printBtn = new JButton("Imprimer");
        JButton exportBtn = new JButton("Exporter");
        JButton closeBtn = new JButton("Fermer");
        btnPanel.add(printBtn);
        btnPanel.add(exportBtn);
        btnPanel.add(closeBtn);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(contentPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        closeBtn.addActionListener(e -> dispose());
        printBtn.addActionListener(e -> {
            PrinterUtil.printFicheLivraison(contentPanel, "Fiche de Livraison");
        });
        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Fiche de Livraison\n");
                sb.append("-------------------\n");
                sb.append("Livreur : ").append(livraison.getLivreur().getNom()).append(" ").append(livraison.getLivreur().getPrenom()).append("\n");
                sb.append("Véhicule : ").append(livraison.getVehicule().getType()).append(" (").append(livraison.getVehicule().getModele()).append(")\n");
                sb.append("Client : ").append(livraison.getClient().getNom()).append(" ").append(livraison.getClient().getPrenom()).append("\n");
                sb.append("Date commande : ").append(sdf.format(livraison.getDateCommande())).append("\n");
                sb.append("Retard : ").append(livraison.isRetard() ? "Oui" : "Non").append("\n");
                sb.append("Pizza : ").append(livraison.getPizza().getNom()).append("\n");
                sb.append("Prix de base : ").append(String.format("%.2f €", livraison.getPizza().getPrixBase())).append("\n");
                sb.append("Imprimé le : ").append(sdf.format(new Date())).append("\n");
                StringSelection selection = new StringSelection(sb.toString());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
                JOptionPane.showMessageDialog(FicheLivraisonDialog.this, "Fiche copiée dans le presse-papier.\nVous pouvez la coller dans un document pour impression.", "Exporté", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, String label, String value) {
        JLabel l1 = new JLabel(label);
        l1.setFont(new Font("Arial", Font.BOLD, 15));
        JLabel l2 = new JLabel(value);
        l2.setFont(new Font("Arial", Font.PLAIN, 15));
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panel.add(l1, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(l2, gbc);
        gbc.gridy++;
    }
} 