# ℹ️  Séance 6 : Mini-ETL Guidé

## 🎯 Mission

Créer un pipeline ETL complet pour analyser des restaurants !

**Extract** → **Transform** → **Load**

## 📊 Données

- `data/restaurants_clean.json` : 10 restaurants parfaits (pour débuter)
- `data/restaurants_dirty.json` : 12 restaurants avec erreurs (pour tester)

## 🚀 Comment démarrer

### 1. Lire le README.md

Le README contient **TOUTES les instructions** étape par étape.

### 2. Créer les fichiers dans l'ordre

1. `Restaurant.scala` - Case classes
2. `DataLoader.scala` - Lire le JSON
3. `DataValidator.scala` - Valider les données
4. `StatsCalculator.scala` - Calculer les stats
5. `ReportGenerator.scala` - Générer le rapport
6. `Main.scala` - Pipeline principal

### 3. Tester au fur et à mesure

```bash
# Compiler
sbt compile

# Exécuter
sbt run
```

## ✅ Résultat attendu

À la fin, votre programme doit :
- ✅ Charger les restaurants depuis JSON
- ✅ Valider et filtrer les données
- ✅ Calculer des statistiques
- ✅ Afficher les résultats dans la console
- ✅ Créer le fichier `results.json`

## 💡 Conseils

1. **Suivez l'ordre** : Chaque fichier dépend du précédent
2. **Testez souvent** : Compilez après chaque fichier
3. **Lisez les erreurs** : Les messages sont informatifs
4. **Commencez simple** : Testez avec `restaurants_clean.json` d'abord
5. **Demandez de l'aide** : C'est pour ça que le professeur est là !

## 🚨 Points d'attention

- ⚠️ N'oubliez pas les imports Circe
- ⚠️ Fermez les fichiers avec `source.close()`
- ⚠️ Gérez les listes vides (division par zéro)
- ⚠️ Testez avec les deux fichiers JSON

## ⏱️ Durée estimée

- Phase 1 (Structure + parsing) : 1h
- Phase 2 (Calculs + rapport) : 1h
- Phase 3 (Pipeline principal) : 1h
- Phase 4 (Tests + amélioration) : 30min

**Total : 3h30**

---

**C'est parti ! 🚀**













