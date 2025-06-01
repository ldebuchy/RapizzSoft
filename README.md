# RaPizzSoft

RaPizzSoft est une application Java Swing permettant la gestion des commandes d'une pizzeria, avec une interface graphique et une connexion à une base de données MySQL.

## Prérequis
Avant de commencer, assurez-vous d'avoir installé :
- **Java 21** ou supérieur
- **Maven** pour la gestion des dépendances
- **MySQL** (ou MariaDB) et le serveur démarré

## Installation et configuration

1. **Cloner le dépôt**
   ```bash
   git clone https://github.com/ldebuchy/RapizzSoft.git
   cd RapizzSoft
   ```

2. **Créer la base de données**
   - Démarrez votre serveur MySQL.
   - Exécutez le script SQL pour créer la base et les tables :
     ```bash
     mysql -u <user> -p < misc/sql/create_database.sql
     ```
   - (Optionnel) Pour ajouter des données de test :
     ```bash
     mysql -u <user> -p rapizzdb < misc/sql/insert_dataset.sql
     ```

3. **Créer le fichier de configuration de la base**
   - Créez le fichier `src/main/resources/config/database.properties` avec le contenu suivant (adaptez les valeurs à votre configuration) :
     ```properties
     jdbc.url=jdbc:mysql://localhost:3306/rapizzdb
     jdbc.user=VotreUtilisateur
     jdbc.password=VotreMotDePasse
     ```

4. **Compiler le projet**
   - Utilisez Maven pour compiler et packager :
     ```bash
     mvn clean package
     ```

5. **Lancer l'application**
   - Exécutez le JAR généré :
     ```bash
     java -jar target/RapizzSoft-1.0-SNAPSHOT.jar
     ```
   - Assurez-vous que le fichier `database.properties` est bien configuré et que la base de données est accessible.

## Environnement technique
- **Java Swing** : interface graphique
- **JTable** : affichage des données tabulaires
- **JDBC** : connexion à la base de données
- **Pattern DAO** : organisation de l'accès aux données
- **Maven** : gestion des dépendances et compilation
- **MySQL** : base de données relationnelle

## Structure du projet
- `src/main/java/` : code source Java (architecture DAO)
- `src/main/resources/` : fichiers de configuration (dont `database.properties`)
- `misc/sql/` : scripts SQL pour la création et l'initialisation de la base de données
- `misc/looping/` : fichiers de la modélisation looping de la base de données

## Problèmes fréquents
- **Erreur de connexion à la base** : vérifiez le fichier `database.properties` et que le serveur MySQL est démarré.
- **Suppression impossible d'un élément** : si un message indique qu'un élément est lié à une commande déjà effectuée, il faut d'abord supprimer ou modifier les commandes associées.
