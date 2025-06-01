package com.rapizz;

import javax.swing.SwingUtilities;
import com.rapizz.ui.MainWindow;
import com.rapizz.utils.DatabaseConnection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Programme principal de RaPizzSoft
 * TODO: Ajouter un écran de chargement
 * TODO: Sauvegarder les préférences
 * TODO: Proposer de configurer les paramètres de la base de données
 */
public class Main {
    public static void main(String[] args) {
        // On lance l'interface graphique
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainWindow fenetre = new MainWindow();
                
                // Quand on ferme la fenêtre
                fenetre.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        // On ferme la connexion à la base de données
                        DatabaseConnection.closeDataSource();
                        System.exit(0);
                    }
                });
                
                // On affiche la fenêtre
                fenetre.setVisible(true);
            }
        });
    }
} 
