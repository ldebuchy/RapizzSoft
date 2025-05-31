# RaPizzSoft

RaPizzSoft est une application Java Swing permettant la gestion des commandes d'une pizzeria, avec une interface graphique et une connexion à une base de données MySQL.

## Prérequis
Avant de commencer, assurez-vous d'avoir installé :
- **Java 21** ou supérieur
- **MySQL** (ou MariaDB) et le serveur démarré

## Installation et configuration
1. **Cloner le dépôt**
2. **Créer la base de données**
   - Assurez-vous que le serveur MySQL est démarré avant de poursuivre.
   - Utilisez le script SQL `misc/sql/create_database.sql` pour créer la base et les tables.
3. **Configurer la connexion**
   - Créez le fichier `src/main/resources/config.properties` et ajoutez-y les informations de connexion à votre base de données MySQL :
     ```properties
     jdbc.url=jdbc:mysql://localhost:3306/rapizzdb
     jdbc.user=USERNAME
     jdbc.password=PASSWORD
     ```
     
## Environnement technique
- **Java Swing** : interface graphique
- **JTable** : affichage des données tabulaires
- **JDBC** : connexion à la base de données
- **Pattern DAO** : organisation de l'accès aux données
- **Maven** : gestion des dépendances et compilation
- **MySQL** : base de données relationnelle

## Structure du projet
- `src/main/java/` : code source Java (architecture DAO)
- `src/main/resources/` : fichiers de configuration (dont `config.properties`)
- `misc/sql/` : scripts SQL pour la création et l'initialisation de la base de données
- `misc/looping/` : fichiers de la modélisation looping de la base de données
