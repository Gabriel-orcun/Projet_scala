# 🚀 Séance 6 : Mini-ETL Guidé - Analyse de Restaurants

## 🎯 Objectif

Créer votre premier **pipeline ETL complet** from scratch ! Vous allez :
- ✅ Parser des données JSON (restaurants)
- ✅ Nettoyer et valider les données
- ✅ Calculer des statistiques
- ✅ Générer un rapport JSON

**Ceci est votre répétition générale avant le projet final !**

## 📊 Les données

Vous avez 2 fichiers dans le dossier `data/` :

### `restaurants_clean.json` (10 restaurants)
Données parfaites pour commencer.

### `restaurants_dirty.json` (12 restaurants avec erreurs)
Données réalistes avec :
- Noms vides
- Ratings invalides (< 0 ou > 5)
- Prix hors limites
- Champs manquants
- Villes vides

## 📝 Structure d'un restaurant

```scala
case class Restaurant(
  id: Int,
  name: String,
  cuisine: String,
  rating: Double,       // Entre 0 et 5
  priceRange: Int,      // 1 (€) à 4 (€€€€)
  city: String,
  vegetarianOptions: Boolean
)
```

## 🎯 Mission : Créer un rapport d'analyse

Votre pipeline doit générer un fichier `results.json` contenant :

```json
{
  "statistics": {
    "totalRestaurants": 10,
    "averageRating": 4.44,
    "vegetarianFriendlyCount": 8
  },
  "topRated": [
    {"name": "Vegan Delight", "rating": 4.9},
    {"name": "Sushi Master", "rating": 4.8}
  ],
  "byCuisine": {
    "French": 2,
    "Japanese": 1,
    "Italian": 1
  },
  "byPriceRange": {
    "1": 2,
    "2": 4,
    "3": 2,
    "4": 2
  }
}
```

---

## 📚 Guide étape par étape

### Étape 1 : Créer les case classes

Créez le fichier `src/main/scala/Restaurant.scala` :

```scala
package miniEtl

// TODO: Créer la case class Restaurant avec tous les champs

// TODO: Créer RestaurantStats avec :
//   - totalRestaurants: Int
//   - averageRating: Double
//   - vegetarianFriendlyCount: Int

// TODO: Créer TopRestaurant avec :
//   - name: String
//   - rating: Double

// TODO: Créer AnalysisReport avec :
//   - statistics: RestaurantStats
//   - topRated: List[TopRestaurant]
//   - byCuisine: Map[String, Int]
//   - byPriceRange: Map[String, Int]
```

**💡 Conseil** : Vérifiez que les noms de champs correspondent exactement au JSON !

### Étape 2 : Lire le fichier JSON

Créez `src/main/scala/DataLoader.scala` :

```scala
package miniEtl

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import scala.io.Source
import scala.util.{Try, Success, Failure}

object DataLoader {

  /**
   * Lit un fichier JSON et parse les restaurants
   */
  def loadRestaurants(filename: String): Either[String, List[Restaurant]] = {
    // TODO: Utiliser Try pour lire le fichier
    //   1. Créer un Source.fromFile(filename)
    //   2. Lire le contenu avec source.mkString
    //   3. Fermer le fichier avec source.close() - IMPORTANT !
    //   4. Parser avec decode[List[Restaurant]](content)
    //   5. Gérer les erreurs avec pattern matching
    ???
  }
}
```

**💡 Indices** :
- Utilisez `decode[List[Restaurant]](jsonString)` pour parser
- Pensez à fermer le fichier avec `source.close()`
- Gérez 2 types d'erreurs : lecture de fichier ET parsing JSON

### Étape 3 : Valider les données

Créez `src/main/scala/DataValidator.scala` :

```scala
package miniEtl

object DataValidator {

  /**
   * Valide un restaurant selon les règles métier
   */
  def isValid(restaurant: Restaurant): Boolean = {
    // TODO: Vérifier que :
    //   - name est non vide
    //   - cuisine est non vide
    //   - rating est entre 0.0 et 5.0
    //   - priceRange est entre 1 et 4
    //   - city est non vide
    ???
  }

  /**
   * Filtre les restaurants valides
   */
  def filterValid(restaurants: List[Restaurant]): List[Restaurant] = {
    // TODO: Utiliser filter avec isValid
    ???
  }
}
```

**💡 Astuce** : Utilisez `&&` pour combiner les conditions

### Étape 4 : Calculer les statistiques

Créez `src/main/scala/StatsCalculator.scala` :

```scala
package miniEtl

object StatsCalculator {

  /**
   * Calcule les statistiques générales
   */
  def calculateStats(restaurants: List[Restaurant]): RestaurantStats = {
    // TODO: Calculer :
    //   - total : taille de la liste
    //   - avgRating : somme des ratings / nombre de restaurants
    //   - vegCount : compter ceux avec vegetarianOptions = true
    // ATTENTION : gérer le cas liste vide pour éviter division par 0 !
    ???
  }

  /**
   * Top N restaurants par note
   */
  def topRated(restaurants: List[Restaurant], n: Int = 3): List[TopRestaurant] = {
    // TODO: 
    //   1. Trier par rating décroissant (utiliser sortBy avec -)
    //   2. Prendre les n premiers (take)
    //   3. Mapper vers TopRestaurant
    ???
  }

  /**
   * Compte par type de cuisine
   */
  def countByCuisine(restaurants: List[Restaurant]): Map[String, Int] = {
    // TODO: 
    //   1. Grouper par cuisine (groupBy)
    //   2. Compter la taille de chaque groupe (map)
    ???
  }

  /**
   * Compte par gamme de prix
   */
  def countByPriceRange(restaurants: List[Restaurant]): Map[String, Int] = {
    // TODO: Comme countByCuisine mais grouper par priceRange
    // ATTENTION : convertir priceRange en String pour la Map
    ???
  }
}
```

**💡 Rappels HOFs** :
- `groupBy(_.field)` : Regroupe par un champ
- `sortBy(-_.field)` : Trie par ordre décroissant
- `map(r => TopRestaurant(r.name, r.rating))` : Transforme

### Étape 5 : Générer le rapport

Créez `src/main/scala/ReportGenerator.scala` :

```scala
package miniEtl

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import scala.util.Try

object ReportGenerator {

  /**
   * Génère le rapport complet
   */
  def generateReport(restaurants: List[Restaurant]): AnalysisReport = {
    // TODO: Créer un AnalysisReport en utilisant StatsCalculator
    //   - statistics = StatsCalculator.calculateStats(...)
    //   - topRated = StatsCalculator.topRated(..., 3)
    //   - byCuisine = StatsCalculator.countByCuisine(...)
    //   - byPriceRange = StatsCalculator.countByPriceRange(...)
    ???
  }

  /**
   * Écrit le rapport en JSON
   */
  def writeReport(report: AnalysisReport, filename: String): Either[String, Unit] = {
    // TODO: 
    //   1. Convertir en JSON : report.asJson.spaces2
    //   2. Écrire dans le fichier avec Files.write
    //   3. Gérer les erreurs avec Try
    ???
  }
}
```

**💡 Astuce** : 
- `report.asJson.spaces2` : Convertit en JSON avec indentation
- `Files.write(Paths.get(filename), json.getBytes(StandardCharsets.UTF_8))` : Écrit

### Étape 6 : Assembler le pipeline

Créez `src/main/scala/Main.scala` :

```scala
package miniEtl

object Main extends App {

  println("Mini-ETL : Analyse de Restaurants\n")

  // TODO: Créer le pipeline ETL avec for-comprehension
  val result = for {
    // TODO: 1. Charger les restaurants avec DataLoader
    restaurants <- ??? // DataLoader.loadRestaurants("data/restaurants_dirty.json")
    _ = println(s"${restaurants.length} restaurants chargés")
    
    // TODO: 2. Valider et filtrer avec DataValidator
    validRestaurants = ??? // DataValidator.filterValid(...)
    _ = println(s"${validRestaurants.length} restaurants valides")
    
    // TODO: 3. Générer le rapport avec ReportGenerator
    report = ??? // ReportGenerator.generateReport(...)
    _ = println(s"Rapport généré")
    
    // TODO: 4. Écrire le rapport
    _ <- ??? // ReportGenerator.writeReport(report, "results.json")
    _ = println(s"Rapport écrit dans results.json")
    
  } yield report

  // TODO: Pattern matching sur result
  //   - Si Right(report) : Afficher les statistiques
  //   - Si Left(error) : Afficher l'erreur
  result match {
    case Right(report) =>
      println("\n STATISTIQUES")
      println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
      // TODO: Afficher report.statistics.totalRestaurants
      // TODO: Afficher report.statistics.averageRating (avec f"${...}%.2f")
      // TODO: Afficher report.statistics.vegetarianFriendlyCount
      
      println("\n TOP 3 RESTAURANTS")
      println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
      // TODO: Afficher report.topRated avec zipWithIndex
      
      println("\n PAR TYPE DE CUISINE")
      println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
      // TODO: Afficher report.byCuisine (trier par count décroissant)
      
      println("\n PAR GAMME DE PRIX")
      println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
      // TODO: Afficher report.byPriceRange
      
      println("\n Pipeline ETL terminé avec succès !")
      
    case Left(error) =>
      // TODO: Afficher l'erreur et quitter
      ???
  }
}
```

**💡 Rappels** :
- Dans for-comprehension : `=` pour calculs, `<-` pour Either
- `_` pour les effets de bord (println)
- `f"${variable}%.2f"` pour formatter avec 2 décimales

---

## 🚀 Lancer le projet

### 1. Compiler

```bash
sbt compile
```

Si ça compile, vous êtes sur la bonne voie !

### 2. Exécuter

```bash
sbt run
```

### 3. Vérifier le résultat

Le fichier `results.json` doit être créé à la racine du projet.

---

## ✅ Checklist

- [ ] Tous les fichiers sont créés
- [ ] Le projet compile sans erreur
- [ ] Le programme s'exécute et affiche les statistiques
- [ ] Le fichier `results.json` est généré
- [ ] Les données invalides sont bien filtrées (5 rejetés sur 12)

## 🚨 Points d'attention

1. **Imports Circe** : N'oubliez pas dans chaque fichier :
   ```scala
   import io.circe._
   import io.circe.generic.auto._
   import io.circe.parser._
   import io.circe.syntax._
   ```

2. **Fermeture des fichiers** : Toujours `source.close()`

3. **Gestion des listes vides** : 
   ```scala
   if (restaurants.nonEmpty) somme / total else 0.0
   ```

4. **For-comprehension** : 
   - `restaurants <- load()` : extrait de Either
   - `filtered = validate()` : simple assignation
   - `_ = println()` : effet de bord

## 💡 Conseils

- **Testez petit** : Compilez après chaque fichier
- **Commencez simple** : Testez avec `restaurants_clean.json` d'abord
- **Lisez les erreurs** : Circe donne des messages très détaillés
- **Console interactive** : Testez vos fonctions avec `sbt console`
- **Demandez de l'aide** : C'est pour ça que le professeur est là !

## 🎯 Résultat attendu

Votre programme doit afficher quelque chose comme :

```
🚀 Mini-ETL : Analyse de Restaurants

✅ 12 restaurants chargés
✅ 7 restaurants valides (5 rejetés)
✅ Rapport généré
✅ Rapport écrit dans results.json

📊 STATISTIQUES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Total restaurants     : 7
Note moyenne          : 4.61/5
Options végétariennes : 7

🏆 TOP 3 RESTAURANTS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
1. Vegan Delight (4.9/5)
2. Sushi Master (4.8/5)
3. La Brasserie (4.7/5)

🍽️  PAR TYPE DE CUISINE
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Japanese: 2
French: 2
Chinese: 1
Indian: 1
Vegan: 1

💰 PAR GAMME DE PRIX
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
€€ (2): 4 restaurants
€€€ (3): 2 restaurants
€€€€ (4): 1 restaurants

✅ Pipeline ETL terminé avec succès !
```

---

**Bon courage ! C'est votre dernière préparation avant le projet final ! 🚀**
