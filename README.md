# Projet Mini-ETL Football

Ce projet implémente un mini pipeline ETL (Extract, Transform, Load) en Scala pour traiter et analyser les données des joueurs de football à partir de fichiers JSON. Il suit les principes de la programmation fonctionnelle pour la robustesse et la clarté.

## 🚀 Fonctionnalités

- **Analyse JSON**: Charge les données à partir de fichiers JSON en utilisant Circe.
- **Validation des données**: Nettoie et valide les données, en gérant les erreurs avec élégance.
- **Analyse statistique**: Calcule diverses statistiques comme les meilleurs buteurs, la répartition des joueurs et les moyennes.
- **Approche fonctionnelle**: Utilise des structures de données immuables, `Either` pour la gestion des erreurs, et la composition de fonctions.
- **Rapports**: Génère un rapport d'analyse détaillé au format JSON et affiche un résumé dans la console.

## 📂 Structure du Projet

Le projet est organisé en plusieurs modules, chacun ayant une responsabilité spécifique :

```
src/main/scala/miniEtl/
├── Football.scala          # Modèles de données (case classes Player, EtlStats, etc.)
├── DataLoader.scala        # Chargement et analyse des données JSON
├── DataValidator.scala     # Validation et nettoyage des données
├── StatsCalculator.scala   # Calculs statistiques
├── ReportGenerator.scala   # Génération de rapports
└── Main.scala              # Point d'entrée principal de l'application
```

---

## 🛠️ Plongée Technique

### 1. Modèles de Données (`Football.scala`)

Ce fichier définit les structures de données utilisées dans toute l'application.

- **`Player`**: Représente un seul joueur avec tous ses attributs. `Option[T]` est utilisé pour les champs qui peuvent être manquants dans les données source (`marketValue`, `salary`), évitant ainsi les `NullPointerException`.
- **`EtlStats`**: Capture les statistiques sur le processus de chargement et de validation des données (par exemple, nombre total de joueurs, erreurs d'analyse, doublons supprimés).
- **`TopPlayer`**: Une structure générique pour représenter un joueur dans une liste de top 10 (par exemple, meilleur buteur, le plus précieux).
- **`DisciplineStats`**: Contient les statistiques relatives à la discipline des joueurs (cartons jaunes/rouges).
- **`AnalysisReport`**: La structure de rapport principale qui regroupe toutes les statistiques générées en un seul objet pour une exportation facile.

### 2. Chargement des Données (`DataLoader.scala`)

Ce module est responsable de la lecture et de l'analyse du fichier JSON d'entrée.

- **`loadPlayers(filename: String): Either[String, List[Player]]`**:
  1.  Lit le contenu du fichier donné. Il utilise `Try` pour gérer les erreurs d'E/S potentielles (par exemple, fichier non trouvé).
  2.  Analyse le contenu de la chaîne en un objet JSON à l'aide de la bibliothèque Circe.
  3.  Décode le tableau JSON en une `List[Player]`. Les entrées qui ne sont pas conformes à la case class `Player` sont ignorées en toute sécurité.
  4.  Retourne `Right(List[Player])` en cas de succès ou `Left(String)` avec un message d'erreur si une étape échoue. Cette approche fonctionnelle de la gestion des erreurs évite les exceptions et rend le flux de données plus prévisible.

### 3. Validation des Données (`DataValidator.scala`)

Ce module nettoie et valide les données chargées.

- **`normalisePosition(pos: String): String`**: Standardise les postes des joueurs (par exemple, mappe "ATT" et "Attacker" à "Forward"). Cela garantit la cohérence pour l'analyse statistique.
- **`isValid(player: Player): Boolean`**: Vérifie si un enregistrement de joueur respecte les règles métier requises (par exemple, le nom n'est pas vide, l'âge est dans une fourchette réaliste, les statistiques sont non négatives).
- **`filterValid(players: List[Player]): List[Player]`**: Une fonction pipeline qui normalise d'abord le poste de chaque joueur, puis filtre les joueurs qui ne passent pas la vérification `isValid`.
- **`removeDuplicates(players: List[Player]): List[Player]`**: Supprime les enregistrements de joueurs en double en fonction de leur `id` unique.

### 4. Calculs Statistiques (`StatsCalculator.scala`)

Ce module contient toutes les fonctions pour effectuer une analyse statistique sur les données propres. Chaque fonction prend une `List[Player]` et retourne une métrique ou une liste spécifique.

- **`calculateEtlStats(filename: String): EtlStats`**: Calcule les métadonnées sur le processus ETL lui-même, telles que le nombre d'erreurs d'analyse et de doublons supprimés.
- **`calculateTopScorers(players: List[Player]): List[TopPlayer]`**: Trie les joueurs par leur `goalsScored` par ordre décroissant et retourne les 10 meilleurs.
- **`calculateTopAssisters`, `calculateMostValuablePlayers`, `calculateHighestPaidPlayers`**: Fonctions similaires qui classent les joueurs en fonction des passes décisives, de la valeur marchande et du salaire. Elles gèrent les types `Option` en toute sécurité en utilisant `getOrElse(0)`.
- **`countPlayersByLeague(players: List[Player]): Map[String, Int]`**: Regroupe les joueurs par leur ligue et compte le nombre de joueurs dans chacune.
- **`countPlayersByPosition`**: Fait de même pour les postes des joueurs.
- **`calculateAverageAgeByPosition`, `calculateAverageGoalsByPosition`**: Calculent l'âge moyen et les buts moyens marqués pour chaque poste.
- **`calculateDisciplineStats(players: List[Player]): DisciplineStats`**: Calcule les statistiques liées à la discipline, telles que le nombre total de cartons et les postes les plus/moins disciplinés sur la base d'un système de pénalités (un carton rouge vaut deux cartons jaunes).

### 5. Génération de Rapport (`ReportGenerator.scala`)

Ce module orchestre la création et la sortie du rapport d'analyse final.

- **`generateReport(players: List[Player], filename: String): AnalysisReport`**: Cette fonction agit comme un agrégateur. Elle appelle toutes les fonctions nécessaires de `StatsCalculator` et assemble les résultats en un seul objet `AnalysisReport`.
- **`writeReport(report: AnalysisReport, filename: String): Either[String, Unit]`**:
  1.  Sérialise l'objet `AnalysisReport` en une chaîne JSON formatée à l'aide de Circe.
  2.  Écrit la chaîne JSON dans le fichier de sortie spécifié (`results.json`).
  3.  Comme `DataLoader`, il retourne un `Either` pour gérer fonctionnellement les erreurs d'écriture de fichier potentielles.

### 6. Application Principale (`Main.scala`)

C'est le point d'entrée de l'application. Il orchestre l'ensemble du pipeline ETL.

- Il utilise une `for-comprehension` pour enchaîner les différentes étapes du processus ETL :
  1.  **Charger** les joueurs depuis le fichier (`DataLoader.loadPlayers`).
  2.  **Valider** et **nettoyer** les données (`DataValidator.filterValid`, `DataValidator.removeDuplicates`).
  3.  **Générer** le rapport (`ReportGenerator.generateReport`).
  4.  **Écrire** le rapport dans un fichier (`ReportGenerator.writeReport`).
- La `for-comprehension` fournit un moyen propre et lisible de gérer la monade `Either`. Si une étape retourne un `Left`, toute la chaîne est court-circuitée et l'erreur est propagée.
- Si le pipeline se termine avec succès, il affiche un résumé formaté du rapport dans la console.
- Il mesure et affiche également le temps d'exécution total et la vitesse de traitement (entrées par seconde).

## ⚙️ Comment Exécuter

1.  **Compiler le code**:
    ```sh
    sbt compile
    ```
2.  **Exécuter l'application**:
    ```sh
    sbt run
    ```
    L'application traitera `data/data_clean.json` par défaut. Vous pouvez changer le fichier d'entrée dans `src/main/scala/miniEtl/Main.scala`. La sortie sera affichée dans la console, et un fichier `results.json` détaillé sera généré dans le répertoire racine du projet.
