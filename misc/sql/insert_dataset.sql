-- Utilisation de la base de données
USE rapizzdb;

-- Insertion des clients
INSERT INTO CLIENT (id_client, nom, prenom, telephone, solde) VALUES (1, 'Riou', 'Frédéric', '+33 (0)1 43 32 18 19', 67.55);
INSERT INTO CLIENT (id_client, nom, prenom, telephone, solde) VALUES (2, 'Briand', 'Arthur', '+33 (0)1 13 38 90 83', 12.25);
INSERT INTO CLIENT (id_client, nom, prenom, telephone, solde) VALUES (3, 'Auger', 'Manon', '02 37 94 02 65', 34.75);
INSERT INTO CLIENT (id_client, nom, prenom, telephone, solde) VALUES (4, 'Lopes', 'Honoré', '+33 5 51 16 15 59', 30.09);
INSERT INTO CLIENT (id_client, nom, prenom, telephone, solde) VALUES (5, 'Aubry', 'Susan', '+33 (0)2 78 16 18 49', 76.28);

-- Insertion des livreurs
INSERT INTO LIVREUR (id_livreur, nom, prenom) VALUES (1, 'Benali', 'Omar');
INSERT INTO LIVREUR (id_livreur, nom, prenom) VALUES (2, 'Nguyen', 'Théo');
INSERT INTO LIVREUR (id_livreur, nom, prenom) VALUES (3, 'Silva', 'Marco');

-- Insertion des véhicules de livraison
INSERT INTO VEHICULE (id_vehicule, modele, type) VALUES (1, 'Peugeot 208', 'voiture');
INSERT INTO VEHICULE (id_vehicule, modele, type) VALUES (2, 'Toyota Yaris', 'voiture');
INSERT INTO VEHICULE (id_vehicule, modele, type) VALUES (3, 'Honda PCX', 'moto');
INSERT INTO VEHICULE (id_vehicule, modele, type) VALUES (4, 'Yamaha Tracer 700', 'moto');

-- Insertion des tailles de pizza avec leurs coefficients de prix
INSERT INTO TAILLE (id_taille, nom, coeff_prix) VALUES (1, 'naine', 0.67);
INSERT INTO TAILLE (id_taille, nom, coeff_prix) VALUES (2, 'humaine', 1.0);
INSERT INTO TAILLE (id_taille, nom, coeff_prix) VALUES (3, 'ogresse', 1.33);

-- Insertion des pizzas avec leurs prix de base
INSERT INTO PIZZA (id_pizza, nom, prix_base) VALUES (1, 'Margherita', 8.0);
INSERT INTO PIZZA (id_pizza, nom, prix_base) VALUES (2, 'Reine', 9.5);
INSERT INTO PIZZA (id_pizza, nom, prix_base) VALUES (3, '4 Fromages', 10.0);
INSERT INTO PIZZA (id_pizza, nom, prix_base) VALUES (4, 'Calzone', 11.0);
INSERT INTO PIZZA (id_pizza, nom, prix_base) VALUES (5, 'Chèvre Miel', 10.5);
INSERT INTO PIZZA (id_pizza, nom, prix_base) VALUES (6, 'Hawaïenne', 9.5);

-- Insertion des ingrédients disponibles
INSERT INTO INGREDIENT (id_ingredient, nom) VALUES (1, 'Tomate');
INSERT INTO INGREDIENT (id_ingredient, nom) VALUES (2, 'Mozzarella');
INSERT INTO INGREDIENT (id_ingredient, nom) VALUES (3, 'Jambon');
INSERT INTO INGREDIENT (id_ingredient, nom) VALUES (4, 'Champignons');
INSERT INTO INGREDIENT (id_ingredient, nom) VALUES (5, 'Chèvre');
INSERT INTO INGREDIENT (id_ingredient, nom) VALUES (6, 'Gorgonzola');
INSERT INTO INGREDIENT (id_ingredient, nom) VALUES (7, 'Miel');
INSERT INTO INGREDIENT (id_ingredient, nom) VALUES (8, 'Ananas');
INSERT INTO INGREDIENT (id_ingredient, nom) VALUES (9, 'Parmesan');

-- Composition des pizzas (association pizza-ingrédient)
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (1, 1);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (1, 2);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (2, 1);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (2, 2);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (2, 3);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (2, 4);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (3, 1);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (3, 2);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (3, 5);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (3, 6);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (3, 9);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (4, 1);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (4, 2);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (4, 3);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (4, 4);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (5, 1);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (5, 2);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (5, 5);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (5, 7);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (6, 1);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (6, 2);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (6, 3);
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES (6, 8);

-- Historique des livraisons
-- Format: id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (1, 2024-05-19, False, False, 6.37, 1, 1, 1, 1, 2);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (2, 2024-05-01, True, True, 0.0, 2, 2, 5, 2, 3);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (3, 2024-05-01, True, True, 0.0, 3, 2, 4, 1, 2);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (4, 2024-05-11, True, True, 0.0, 1, 2, 1, 2, 3);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (5, 2024-05-20, False, False, 10.64, 4, 3, 1, 3, 1);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (6, 2024-05-13, True, True, 0.0, 3, 2, 5, 3, 2);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (7, 2024-05-03, True, True, 0.0, 1, 2, 2, 1, 1);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (8, 2024-05-13, False, False, 6.7, 3, 3, 4, 1, 3);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (9, 2024-05-09, True, True, 0.0, 2, 3, 5, 3, 2);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (10, 2024-05-06, False, False, 13.3, 2, 2, 4, 3, 3);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (11, 2024-05-02, True, True, 0.0, 4, 2, 1, 2, 1);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (12, 2024-05-07, False, False, 11.0, 4, 3, 2, 2, 4);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (13, 2024-05-05, True, True, 0.0, 3, 3, 5, 3, 4);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (14, 2024-05-19, False, True, 0.0, 2, 1, 3, 3, 4);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (15, 2024-05-02, True, True, 0.0, 2, 3, 2, 3, 4);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (16, 2024-05-20, True, True, 0.0, 4, 2, 4, 3, 3);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (17, 2024-05-18, True, True, 0.0, 3, 3, 1, 3, 3);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (18, 2024-05-04, False, False, 6.7, 4, 1, 4, 1, 3);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (19, 2024-05-06, True, True, 0.0, 2, 3, 3, 1, 3);
INSERT INTO LIVRAISON (id_livraison, date_livraison, retard, gratuite, prix_facture, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES (20, 2024-05-06, True, True, 0.0, 4, 2, 5, 1, 1);