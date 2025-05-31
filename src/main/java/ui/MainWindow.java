package ui;

import dao.ClientDAO;
import dao.Connexion;
import model.Client;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("RaPizz - Clients");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            Connection conn = Connexion.getConnection();
            ClientDAO clientDAO = new ClientDAO(conn);
            List<Client> clients = clientDAO.getAllClients();

            String[][] data = new String[clients.size()][4];
            String[] cols = {"Nom", "Prénom", "Téléphone", "Solde"};
            for (int i = 0; i < clients.size(); i++) {
                Client c = clients.get(i);
                data[i][0] = c.getNom();
                data[i][1] = c.getPrenom();
                data[i][2] = c.getTelephone();
                data[i][3] = String.valueOf(c.getSolde());
            }
            JTable table = new JTable(data, cols);
            add(new JScrollPane(table), BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            setLayout(new BorderLayout());
            setSize(400, 200);
            setTitle("Erreur de connexion");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            JLabel errorLabel = new JLabel("Erreur de connexion à la base de données.", SwingConstants.CENTER);
            add(errorLabel, BorderLayout.CENTER);
        }
    }
}