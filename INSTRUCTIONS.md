# 📝 Instructions de démarrage

## 🎯 Votre mission

Créer un pipeline ETL pour analyser des restaurants !

## 📚 Ordre de création des fichiers

Créez les fichiers dans `src/main/scala/` dans cet ordre :

### 1. Restaurant.scala
Commencez ici ! C'est le plus simple.
```scala
package miniEtl

case class Restaurant(
  // TODO: Ajouter les champs selon le README
)

case class RestaurantStats(
  // TODO: Ajouter les champs
)

case class TopRestaurant(
  // TODO: Ajouter les champs
)

case class AnalysisReport(
  // TODO: Ajouter les champs
)
```

### 2. DataLoader.scala
Lire et parser le JSON.
```scala
package miniEtl

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import scala.io.Source
import scala.util.{Try, Success, Failure}

object DataLoader {
  def loadRestaurants(filename: String): Either[String, List[Restaurant]] = {
    // TODO: Implémenter selon le README
    ???
  }
}
```

### 3. DataValidator.scala
Valider les données.
```scala
package miniEtl

object DataValidator {
  def isValid(restaurant: Restaurant): Boolean = {
    // TODO: Vérifier toutes les contraintes
    ???
  }

  def filterValid(restaurants: List[Restaurant]): List[Restaurant] = {
    // TODO: Filtrer avec isValid
    ???
  }
}
```

### 4. StatsCalculator.scala
Calculer les statistiques.
```scala
package miniEtl

object StatsCalculator {
  def calculateStats(restaurants: List[Restaurant]): RestaurantStats = {
    // TODO: Calculer total, moyenne, végétariens
    ???
  }

  def topRated(restaurants: List[Restaurant], n: Int = 3): List[TopRestaurant] = {
    // TODO: Trier par rating et prendre les N premiers
    ???
  }

  def countByCuisine(restaurants: List[Restaurant]): Map[String, Int] = {
    // TODO: Grouper par cuisine
    ???
  }

  def countByPriceRange(restaurants: List[Restaurant]): Map[String, Int] = {
    // TODO: Grouper par priceRange
    ???
  }
}
```

### 5. ReportGenerator.scala
Générer le rapport.
```scala
package miniEtl

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import scala.util.Try

object ReportGenerator {
  def generateReport(restaurants: List[Restaurant]): AnalysisReport = {
    // TODO: Créer le rapport avec StatsCalculator
    ???
  }

  def writeReport(report: AnalysisReport, filename: String): Either[String, Unit] = {
    // TODO: Convertir en JSON et écrire dans le fichier
    ???
  }
}
```

### 6. Main.scala
Assembler le pipeline.
```scala
package miniEtl

object Main extends App {
  println("Mini-ETL : Analyse de Restaurants\n")

  val result = for {
    // TODO: 1. Charger les restaurants
    // TODO: 2. Valider et filtrer
    // TODO: 3. Générer le rapport
    // TODO: 4. Écrire le fichier
  } yield report

  // TODO: Pattern matching sur result et afficher les statistiques
}
```

## ✅ Vérification

Après chaque fichier, testez :
```bash
sbt compile
```

Quand tout est fait :
```bash
sbt run
```

## 🎯 Résultat final

Vous devez voir :
- ✅ Les logs de progression
- ✅ Les statistiques affichées
- ✅ Le fichier `results.json` créé

---

**Suivez le README.md pour les détails de chaque fonction ! 📚**
