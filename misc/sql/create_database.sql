-- Création de la base de données
CREATE DATABASE rapizzdb;

-- Utilisation de la base de données
USE rapizzdb;

-- Table des clients
CREATE TABLE CLIENT (
    id_client INT AUTO_INCREMENT,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    adresse VARCHAR(100),
    telephone VARCHAR(20),
    solde DECIMAL(6,2),
    nb_pizzas_achetees INT DEFAULT 0,
    PRIMARY KEY(id_client)
);

-- Table des pizzas
CREATE TABLE PIZZA (
    id_pizza INT AUTO_INCREMENT,
    nom VARCHAR(50),
    prix_base DECIMAL(5,2),
    PRIMARY KEY(id_pizza)
);

-- Table des ingrédients
CREATE TABLE INGREDIENT (
    id_ingredient INT AUTO_INCREMENT,
    nom VARCHAR(50),
    PRIMARY KEY(id_ingredient)
);

-- Table des tailles de pizzas
CREATE TABLE TAILLE (
    id_taille INT AUTO_INCREMENT,
    nom VARCHAR(20),
    coeff_prix DECIMAL(3,2),
    PRIMARY KEY(id_taille)
);

-- Table des véhicules
CREATE TABLE VEHICULE (
    id_vehicule INT AUTO_INCREMENT,
    modele VARCHAR(50),
    type VARCHAR(20),
    PRIMARY KEY(id_vehicule)
);

-- Table des livreurs
CREATE TABLE LIVREUR (
    id_livreur INT AUTO_INCREMENT,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    telephone VARCHAR(20),
    nb_retards INT DEFAULT 0,
    PRIMARY KEY(id_livreur)
);

-- Table des livraisons
CREATE TABLE LIVRAISON (
    id_livraison INT AUTO_INCREMENT,
    date_commande DATETIME,
    prix DECIMAL(5,2),
    status_livraison VARCHAR(50),
    date_livraison DATETIME,
    facturation BOOLEAN,
    note VARCHAR(50),
    retard BOOLEAN DEFAULT FALSE,
    id_vehicule INT,
    id_livreur INT,
    id_client INT,
    id_taille INT,
    id_pizza INT,
    PRIMARY KEY(id_livraison),
    FOREIGN KEY(id_vehicule) REFERENCES VEHICULE(id_vehicule),
    FOREIGN KEY(id_livreur) REFERENCES LIVREUR(id_livreur),
    FOREIGN KEY(id_client) REFERENCES CLIENT(id_client),
    FOREIGN KEY(id_taille) REFERENCES TAILLE(id_taille),
    FOREIGN KEY(id_pizza) REFERENCES PIZZA(id_pizza)
);

-- Table de composition des pizzas
CREATE TABLE COMPO_PIZZA (
    id_pizza INT,
    id_ingredient INT,
    PRIMARY KEY(id_pizza, id_ingredient),
    FOREIGN KEY(id_pizza) REFERENCES PIZZA(id_pizza),
    FOREIGN KEY(id_ingredient) REFERENCES INGREDIENT(id_ingredient)
);

-- Trigger pour mettre à jour le nombre de retards des livreurs
DELIMITER //
CREATE TRIGGER after_livraison_update
AFTER UPDATE ON LIVRAISON
FOR EACH ROW
BEGIN
    IF NEW.retard = TRUE AND (OLD.retard IS NULL OR OLD.retard = FALSE) THEN
        UPDATE LIVREUR 
        SET nb_retards = nb_retards + 1
        WHERE id_livreur = NEW.id_livreur;
    END IF;
END//
DELIMITER ;
