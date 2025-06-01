-- Utilisation de la base de données
USE rapizzdb;

-- Désactiver temporairement les vérifications de clés étrangères
SET FOREIGN_KEY_CHECKS = 0;

-- Vider les tables dans l'ordre inverse des dépendances
TRUNCATE TABLE LIVRAISON;
TRUNCATE TABLE COMPO_PIZZA;
TRUNCATE TABLE LIVREUR;
TRUNCATE TABLE VEHICULE;
TRUNCATE TABLE PIZZA;
TRUNCATE TABLE INGREDIENT;
TRUNCATE TABLE TAILLE;
TRUNCATE TABLE CLIENT;

-- Réactiver les vérifications de clés étrangères
SET FOREIGN_KEY_CHECKS = 1; 