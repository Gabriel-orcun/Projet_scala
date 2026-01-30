package miniEtl

import java.io.{File, PrintWriter}

object Main extends App {

  val dataSets = List("data/data_clean.json", "data/data_dirty.json", "data/data_large.json")

  dataSets.foreach { filename =>
    println(s"===============================================")
    println(s"   TRAITEMENT DU FICHIER: $filename")
    println(s"===============================================")

    val startTime = System.currentTimeMillis()

    val result = for {
      players <- DataLoader.loadPlayers(filename)
      _ = println(s"${players.length} joueurs charges")
      
      validPlayers = DataValidator.filterValid(players)
      _ = println(s"${validPlayers.length} joueurs valides")
      
      finalPlayers = DataValidator.removeDuplicates(validPlayers)
      _ = println(s"${finalPlayers.length} joueurs finaux (${validPlayers.length - finalPlayers.length} doublons supprimes)")
      
      report = ReportGenerator.generateReport(finalPlayers, filename)
      _ = println("Rapport genere")
      
      jsonOutputFilename = filename.replace("data/", "report-").replace(".json", ".json")
      _ <- ReportGenerator.writeReport(report, jsonOutputFilename)
      _ = println(s"Rapport JSON ecrit dans $jsonOutputFilename")
      
    } yield report


    val reportContent = result match {
      case Right(report) =>
        val sb = new StringBuilder()
        sb.append("===============================================\n")
        sb.append(s"   RAPPORT D'ANALYSE - $filename\n")
        sb.append("===============================================\n\n")

        sb.append("STATISTIQUES DE PARSING\n")
        sb.append("---------------------------\n")
        sb.append(f"- Entrees totales lues      : ${report.etlStats.totalEntries}\n")
        sb.append(f"- Entrees valides           : ${report.etlStats.validEntries}\n")
        sb.append(f"- Erreurs de parsing        : ${report.etlStats.parsingErrors}\n")
        sb.append(f"- Doublons supprimes        : ${report.etlStats.duplicatesRemoved}\n\n")
        
        sb.append("TOP 10 - BUTEURS\n")
        sb.append("-------------------\n")
        report.topTenScorers.zipWithIndex.foreach { case (player, idx) =>
          sb.append(f"${idx + 1}. ${player.name}%-25s : ${player.value.toInt} buts en ${player.matches} matchs\n")
        }
        sb.append("\n")

        sb.append("TOP 10 - PASSEURS\n")
        sb.append("---------------------\n")
        report.topTenAssisters.zipWithIndex.foreach { case (player, idx) =>
          sb.append(f"${idx + 1}. ${player.name}%-25s : ${player.value.toInt} passes en ${player.matches} matchs\n")
        }
        sb.append("\n")

        sb.append("TOP 10 - VALEUR MARCHANDE\n")
        sb.append("-----------------------------\n")
        report.mostValuablePlayers.zipWithIndex.foreach { case (player, idx) =>
          sb.append(f"${idx + 1}. ${player.name}%-25s : ${player.value.toInt} M EUR\n")
        }
        sb.append("\n")

        sb.append("TOP 10 - SALAIRES\n")
        sb.append("--------------------\n")
        report.highestPaidPlayers.zipWithIndex.foreach { case (player, idx) =>
          sb.append(f"${idx + 1}. ${player.name}%-25s : ${player.value}%.2f M EUR/an\n")
        }
        sb.append("\n")

        sb.append("REPARTITION PAR LIGUE\n")
        sb.append("-------------------------\n")
        report.playersByLeague.toList.sortBy(-_._2).foreach { case (league, count) =>
          sb.append(f"- ${league}%-25s : $count joueurs\n")
        }
        sb.append("\n")

        sb.append("REPARTITION PAR POSTE\n")
        sb.append("------------------------\n")
        report.playersByPosition.toList.sortBy(-_._2).foreach { case (position, count) =>
          sb.append(f"- ${position}%-25s : $count joueurs\n")
        }
        sb.append("\n")

        sb.append("MOYENNES PAR POSTE\n")
        sb.append("----------------------\n")
        sb.append("AGE MOYEN :\n")
        report.averageAgeByPosition.toList.sortBy(-_._2).foreach { case (position, avg) =>
          sb.append(f"- ${position}%-25s : $avg%.1f ans\n")
        }
        sb.append("\n")

        sb.append("BUTS PAR MATCH (moyenne) :\n")
        report.averageGoalsByPosition.toList.sortBy(-_._2).foreach { case (position, avg) =>
          sb.append(f"- ${position}%-25s : $avg%.2f buts/match\n")
        }
        sb.append("\n")

        sb.append("STATISTIQUES DISCIPLINAIRES\n")
        sb.append("-----------------------------\n")
        sb.append(f"- Total cartons jaunes      : ${report.disciplineStats.totalYellowCards}\n")
        sb.append(f"- Total cartons rouges      : ${report.disciplineStats.totalRedCards}\n")
        sb.append(f"- Poste le plus discipline  : ${report.disciplineStats.mostDisciplinedPosition}\n")
        sb.append(f"- Poste le moins discipline : ${report.disciplineStats.leastDisciplinedPosition}\n\n")

        val endTime = System.currentTimeMillis()
        val duration = (endTime - startTime) / 1000.0
        sb.append(f"Temps de traitement total : $duration%.2f secondes\n")
        sb.toString

      case Left(error) =>
        s"ERREUR lors du traitement de $filename: $error"
    }

    val reportOutputFilename = filename.replace("data/", "rapport-").replace(".json", ".txt")
    val writer = new PrintWriter(new File(reportOutputFilename))
    writer.write(reportContent)
    writer.close()
    println(s"Rapport texte ecrit dans $reportOutputFilename\n")
  }
}
