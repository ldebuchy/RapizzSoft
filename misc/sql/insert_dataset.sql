-- Utilisation de la base de données
USE rapizzdb;

-- Insertion des clients
INSERT INTO CLIENT (id_client, nom, prenom, adresse, telephone, solde, nb_pizzas_achetees) VALUES 
(1, 'Riette', 'Frédéric', '15 rue de la Paix, Paris', '+33 (0)7 43 32 18 19', 67.55, 7),
(2, 'Briand', 'Arthur', '8 avenue des Champs-Élysées, Paris', '+33 (0)1 13 38 90 83', 12.25, 2),
(3, 'Auger', 'Manon', '25 rue du Commerce, Lyon', '02 37 94 02 65', 34.75, 1),
(4, 'Lopes', 'Honoré', '3 boulevard Victor Hugo, Marseille', '+33 5 51 16 15 59', 30.09, 1),
(5, 'Aubry', 'Susan', '12 rue de la République, Lille', '+33 (0)2 76 16 18 49', 11.80, 2),
(6, 'Leroy', 'Lucas', '13 rue de la Dictature, Brest', '+33 (0)2 79 72 98 39', 51.25, 2),
(7, 'Aubert', 'Hugo', '2 rue de la Liberté, Paris', '+33 (0)6 78 11 68 79', 109.45, 2),
(8, 'Xavier', 'Maxime', '25 rue des Pyrénées, Toulouse', '+33 (0)9 78 66 88 42', 76.28, 2),
(9, 'Hébert', 'Camille', '14 rue des gaufrettes, Nantes', '+33 (0)9 78 14 15 53', 59.45, 2);


-- Insertion des véhicules de livraison
INSERT INTO VEHICULE (id_vehicule, modele, type) VALUES 
(1, 'Peugeot 208', 'voiture'),
(2, 'Toyota Yaris', 'voiture'),
(3, 'Yamaha Tracer 700', 'moto'),
(4, 'Kawasaki ZX-10R', 'moto'),
(5, 'Suzuki GSX-R1000', 'moto');

-- Insertion des livreurs
INSERT INTO LIVREUR (id_livreur, nom, prenom, telephone, nb_retards) VALUES
(1, 'Sanchez', 'Rick', '+33 (0)6 12 34 56 78', 2),
(2, 'Ribbo', 'Théo', '+33 (0)6 23 45 67 89', 1),
(3, 'Caudron', 'Marco', '+33 (0)6 34 56 78 90', 0),
(4, 'Dalando', 'Orlando', '+33 (0)6 45 67 89 01', 0),
(5, 'Ledure', 'Pierre', '+33 (0)6 56 78 90 12', 0);

-- Insertion des tailles de pizza avec leurs coefficients de prix
INSERT INTO TAILLE (id_taille, nom, coeff_prix) VALUES 
(1, 'Naine', 0.67),
(2, 'Humaine', 1.0),
(3, 'Ogresse', 1.33);

-- Insertion des pizzas avec leurs prix de base
INSERT INTO PIZZA (id_pizza, nom, prix_base) VALUES 
(1, 'Margherita', 8.0),
(2, 'Reine', 9.5),
(3, '4 Fromages', 10.0),
(4, 'Calzone', 11.0),
(5, 'Chèvre Miel', 10.5),
(6, 'Hawaïenne', 9.5);

-- Insertion des ingrédients disponibles
INSERT INTO INGREDIENT (id_ingredient, nom) VALUES 
(1, 'Tomate'),
(2, 'Mozzarella'),
(3, 'Jambon'),
(4, 'Champignons'),
(5, 'Chèvre'),
(6, 'Gorgonzola'),
(7, 'Miel'),
(8, 'Ananas'),
(9, 'Parmesan');

-- Composition des pizzas (association pizza-ingrédient)
INSERT INTO COMPO_PIZZA (id_pizza, id_ingredient) VALUES 
(1, 1), (1, 2),
(2, 1), (2, 2), (2, 3), (2, 4),
(3, 1), (3, 2), (3, 5), (3, 6), (3, 9),
(4, 1), (4, 2), (4, 3), (4, 4),
(5, 1), (5, 2), (5, 5), (5, 7),
(6, 1), (6, 2), (6, 3), (6, 8);

-- Historique des livraisons
INSERT INTO LIVRAISON (id_livraison, date_commande, prix, status_livraison, date_livraison, facturation, note, retard, id_vehicule, id_livreur, id_client, id_taille, id_pizza) VALUES 
(1, '2025-01-15 12:15:00', 10.67, 'Livrée', '2024-03-15 12:35:00', TRUE, '', FALSE, 1, 1, 1, 2, 1),
(2, '2025-02-15 19:30:00', 12.50, 'Livrée', NULL, TRUE, NULL, FALSE, 2, 1, 2, 1, 2),
(3, '2025-03-16 12:45:00', 0.00, 'Livrée', '2024-03-16 13:05:00', FALSE, '', FALSE, 3, 3, 3, 3, 3),
(4, '2025-04-16 19:15:00', 0.00, 'Livrée', '2024-03-16 20:00:00', FALSE, '', TRUE, 4, 1, 4, 2, 4),
(5, '2025-05-17 12:30:00', 9.50, 'En cours', NULL, TRUE, NULL, FALSE, 1, 2, 5, 1, 5),
(6, '2025-05-17 19:45:00', 11.00, 'Livrée', '2024-03-17 20:05:00', TRUE, '', FALSE, 2, 3, 1, 3, 6),
(7, '2025-06-1 12:20:00', 14.75, 'Livrée', '2024-03-18 12:40:00', TRUE, '', FALSE, 3, 1, 2, 2, 1);