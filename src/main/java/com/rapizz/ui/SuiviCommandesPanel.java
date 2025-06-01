package com.rapizz.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.rapizz.dao.implementations.ClientDAOImpl;
import com.rapizz.dao.implementations.LivraisonDAOImpl;
import com.rapizz.dao.implementations.LivreurDAOImpl;
import com.rapizz.dao.interfaces.ClientDAO;
import com.rapizz.dao.interfaces.LivraisonDAO;
import com.rapizz.dao.interfaces.LivreurDAO;
import com.rapizz.model.Client;
import com.rapizz.model.Livraison;
import com.rapizz.utils.CommandeObserver;
import com.rapizz.utils.DatabaseConnection;


import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

public class SuiviCommandesPanel extends JPanel {
    JTable table;
    LivraisonDAO livraisonDAO;
    private ClientDAO clientDAO;
    private LivreurDAO livreurDAO;
    private DefaultTableModel tableModel;
    private SimpleDateFormat dateFormat;
    private Timer refreshTimer;

    public SuiviCommandesPanel() {
        livraisonDAO = new LivraisonDAOImpl();
        clientDAO = new ClientDAOImpl();
        livreurDAO = new LivreurDAOImpl();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        setLayout(new BorderLayout());

        // Création du modèle de table
        String[] columns = {"n°", "Client", "Pizza", "Taille", "Livreur", "Véhicule", "Date Commande", "Statut"};
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
        JButton marquerLivreeButton = new JButton("Confirmer la livraison");
        JButton annulerButton = new JButton("Annuler la commande");
        JButton ficheButton = new JButton("Fiche de livraison");
        buttonPanel.add(ficheButton);
        buttonPanel.add(marquerLivreeButton);
        buttonPanel.add(annulerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ajout des écouteurs d'événements
        marquerLivreeButton.addActionListener(e -> marquerCommeLivree());
        annulerButton.addActionListener(e -> annulerLivraison());
        ficheButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une commande pour voir sa fiche",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int id = (int) table.getValueAt(selectedRow, 0);
            Livraison l = livraisonDAO.read(id);
            FicheLivraisonDialog dialog = new FicheLivraisonDialog((Frame) SwingUtilities.getWindowAncestor(this), l);
            dialog.setVisible(true);
        });

        // Configuration du timer de rafraîchissement automatique
        refreshTimer = new Timer(30000, e -> refreshTable()); // Rafraîchissement toutes les 30 secondes
        refreshTimer.start();

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
            if (isShowing()) {
                refreshTable();
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Livraison> livraisons = livraisonDAO.findLivraisonsEnCours();
        for (Livraison l : livraisons) {
            tableModel.addRow(new Object[]{
                l.getId(),
                l.getClient().getNom() + " " + l.getClient().getPrenom(),
                l.getPizza().getNom(),
                l.getTaille(),
                l.getLivreur().getNom() + " " + l.getLivreur().getPrenom(),
                l.getVehicule().getModele(),
                dateFormat.format(l.getDateCommande()),
                l.getStatusLivraison()
            });
        }
        adjustColumnWidths();
    }

    private void adjustColumnWidths() {
        // Ajuster la largeur des colonnes en fonction du contenu
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Largeur minimale
            switch (column) {
                case 0: // n°
                    width = 40;
                    break;
                case 1: // Client
                    width = 150;
                    break;
                case 2: // Pizza
                    width = 120;
                    break;
                case 3: // Taille
                    width = 80;
                    break;
                case 4: // Livreur
                    width = 120;
                    break;
                case 5: // Véhicule
                    width = 100;
                    break;
                case 6: // Date Commande
                    width = 120;
                    break;
                case 7: // Statut
                    width = 100;
                    break;
            }
            table.getColumnModel().getColumn(column).setPreferredWidth(width);
        }
    }

    private void marquerCommeLivree() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande à marquer comme livrée",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        Livraison livraison = livraisonDAO.read(id);
        if (livraison == null) {
            JOptionPane.showMessageDialog(this, "Impossible de récupérer les informations de la commande",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifier si c'est la 10ème pizza du client
        int nbPizzasLivrees = livraisonDAO.getNombrePizzasAchetees(livraison.getClient().getId());
        boolean estGratuite = nbPizzasLivrees == 9;

        // Créer le panel de confirmation
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        // Date et heure de livraison
        JLabel dateLabel = new JLabel("Date et heure de livraison :");
        panel.add(dateLabel);
        
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy HH:mm");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date());
        panel.add(dateSpinner);

        // Case à cocher pour la facturation
        JCheckBox facturationCheckBox = new JCheckBox("Facturer la livraison");
        facturationCheckBox.setSelected(true);
        panel.add(new JLabel(""));
        panel.add(facturationCheckBox);

        // Afficher si c'est une pizza gratuite
        if (estGratuite) {
            JLabel gratuiteLabel = new JLabel("Cette pizza est gratuite (10ème commande)");
            gratuiteLabel.setForeground(new Color(0, 128, 0)); // Vert
            panel.add(gratuiteLabel);
            panel.add(new JLabel(""));
        }

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Confirmer la livraison", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Date dateLivraison = (Date) dateSpinner.getValue();
                boolean facturation = facturationCheckBox.isSelected() && !estGratuite;

                // Calculer le temps de livraison
                long diffMinutes = (dateLivraison.getTime() - livraison.getDateCommande().getTime()) / (60 * 1000);
                String tempsLivraison = diffMinutes + " minutes";

                // Vérifier si la livraison est en retard (plus de 30 minutes)
                // TODO: Rendre ce délai modifiable
                boolean retard = diffMinutes > 30;
                if (retard && facturation) {
                    String messageRetard = "La livraison a pris " + tempsLivraison + ".\n";
                    messageRetard += "Le client sera remboursé de " + String.format("%.2f €", livraison.getPrix()) + "\n";
                    messageRetard += "Voulez-vous continuer ?";
                    
                    int retardConfirm = JOptionPane.showConfirmDialog(this,
                        messageRetard,
                        "Livraison en retard",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (retardConfirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                    facturation = false;
                }

                // Si on ne facture pas la livraison
                // TODO: Ajouter un champ pour expliquer pourquoi
                if (!facturationCheckBox.isSelected() && !estGratuite) {
                    String messageFacture = "Le client ne sera pas facturé pour cette livraison et remboursé de ";
                    messageFacture += String.format("%.2f €", livraison.getPrix()) + "\n";
                    messageFacture += "Voulez-vous continuer ?";
                    
                    int factureConfirm = JOptionPane.showConfirmDialog(this,
                        messageFacture,
                        "Confirmation de non-facturation",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (factureConfirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                    facturation = false;
                }

                // Mettre à jour la livraison
                String sql = "UPDATE LIVRAISON SET status_livraison = 'Livrée', date_livraison = ?, " +
                           "retard = ?, facturation = ? WHERE id_livraison = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setTimestamp(1, new java.sql.Timestamp(dateLivraison.getTime()));
                    stmt.setBoolean(2, retard);
                    stmt.setBoolean(3, facturation);
                    stmt.setInt(4, id);
                    stmt.executeUpdate();

                    // Récupérer la livraison mise à jour
                    livraison = livraisonDAO.read(id);
                    if (livraison != null) {
                        // Si c'est un retard, incrémenter le nombre de retards du livreur
                        if (retard) {
                            livreurDAO.incrementerRetards(livraison.getLivreur().getId());
                        }
                        
                        // Mettre à jour le compteur de pizzas du client
                        Client client = livraison.getClient();
                        client.setNbPizzasAchetees(client.getNbPizzasAchetees() + 1);
                        clientDAO.update(client);
                        
                        // Si ce n'est pas une pizza gratuite et qu'elle doit être facturée
                        if (!estGratuite && facturation) {
                            // Vérifier si c'est maintenant la 10ème pizza
                            if (client.getNbPizzasAchetees() == 10) {
                                JOptionPane.showMessageDialog(this,
                                    "Félicitations ! C'est la 10ème pizza de " + client.getNom() + " " + client.getPrenom() + 
                                    "\nLa prochaine pizza sera gratuite !",
                                    "10ème pizza",
                                    JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            // Rembourser le client si ce n'est pas une pizza gratuite
                            if (!estGratuite) {
                                client.setSolde(client.getSolde() + livraison.getPrix());
                                JOptionPane.showMessageDialog(this,
                                    "Le client " + client.getNom() + " " + client.getPrenom() + 
                                    " a été remboursé de " + String.format("%.2f €", livraison.getPrix()) + 
                                    "\nNouveau solde : " + String.format("%.2f €", client.getSolde()),
                                    "Remboursement effectué",
                                    JOptionPane.INFORMATION_MESSAGE);
                                clientDAO.update(client);
                            }
                        }
                    }
                }

                // Rafraîchir les données
                refreshTable();
                
                // Notifier les observateurs qu'une livraison a été marquée comme livrée
                CommandeObserver.getInstance().notifyObservers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour de la commande: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void annulerLivraison() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande à annuler",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        Livraison livraison = livraisonDAO.read(id);
        if (livraison == null) {
            JOptionPane.showMessageDialog(this, "Impossible de récupérer les informations de la commande",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir annuler cette commande ?\n" +
                "Le client sera remboursé de " + String.format("%.2f €", livraison.getPrix()),
                "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                livraisonDAO.annulerLivraison(id);
                
                // Afficher le message de remboursement
                Client client = livraison.getClient();
                JOptionPane.showMessageDialog(this,
                    "Le client " + client.getNom() + " " + client.getPrenom() + 
                    " a été remboursé de " + String.format("%.2f €", livraison.getPrix()) + 
                    "\nNouveau solde : " + String.format("%.2f €", client.getSolde()),
                    "Remboursement effectué",
                    JOptionPane.INFORMATION_MESSAGE);
                
                refreshTable();
                // Notifier les observateurs qu'une livraison a été annulée
                CommandeObserver.getInstance().notifyObservers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'annulation de la commande: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        refreshTimer.stop();
    }
} 