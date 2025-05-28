-- Création de la base de données
CREATE DATABASE rapizzdb;

-- Utilisation de la base de données
USE rapizzdb;

-- Table des clients
CREATE TABLE CLIENT (
    id_client INT PRIMARY KEY,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    telephone VARCHAR(20),
    solde DECIMAL(6,2)
);

-- Table des livreurs
CREATE TABLE LIVREUR (
    id_livreur INT PRIMARY KEY,
    nom VARCHAR(50),
    prenom VARCHAR(50)
);

-- Table des véhicules
CREATE TABLE VEHICULE (
    id_vehicule INT PRIMARY KEY,
    modele VARCHAR(50),
    type VARCHAR(20)
);

-- Table des tailles de pizzas
CREATE TABLE TAILLE (
    id_taille INT PRIMARY KEY,
    nom VARCHAR(20),
    coeff_prix DECIMAL(3,2)
);

-- Table des pizzas
CREATE TABLE PIZZA (
    id_pizza INT PRIMARY KEY,
    nom VARCHAR(50),
    prix_base DECIMAL(5,2)
);

-- Table des ingrédients
CREATE TABLE INGREDIENT (
    id_ingredient INT PRIMARY KEY,
    nom VARCHAR(50)
);

-- Table de composition des pizzas (association n-n)
CREATE TABLE COMPO_PIZZA (
    id_pizza INT,
    id_ingredient INT,
    PRIMARY KEY(id_pizza, id_ingredient),
    FOREIGN KEY(id_pizza) REFERENCES PIZZA(id_pizza),
    FOREIGN KEY(id_ingredient) REFERENCES INGREDIENT(id_ingredient)
);

-- Table des livraisons
CREATE TABLE LIVRAISON (
    id_livraison INT PRIMARY KEY,
    date_livraison DATE,
    retard BOOLEAN,
    gratuite BOOLEAN,
    prix_facture DECIMAL(5,2),
    id_vehicule INT,
    id_livreur INT,
    id_client INT,
    id_taille INT,
    id_pizza INT,
    FOREIGN KEY(id_vehicule) REFERENCES VEHICULE(id_vehicule),
    FOREIGN KEY(id_livreur) REFERENCES LIVREUR(id_livreur),
    FOREIGN KEY(id_client) REFERENCES CLIENT(id_client),
    FOREIGN KEY(id_taille) REFERENCES TAILLE(id_taille),
    FOREIGN KEY(id_pizza) REFERENCES PIZZA(id_pizza)
);
