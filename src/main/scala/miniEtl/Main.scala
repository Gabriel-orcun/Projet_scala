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
      players <- DataLoader.loadFootball(filename)
      _ = println(s"${players.length} joueurs charges")
      
      validPlayers = DataValidator.filterValid(players)
      _ = println(s"${validPlayers.length} joueurs valides")
      
      finalPlayers = DataValidator.removeDuplicates(validPlayers)
      _ = println(s"${finalPlayers.length} joueurs finaux (${validPlayers.length - finalPlayers.length} doublons supprimes)")
      
      report = ReportGenerator.geratereport(finalPlayers, filename)
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
        sb.append(f"- Entrees totales lues      : ${report.statistics.tot_players}\n")
        sb.append(f"- Entrees valides           : ${report.statistics.tot_players_valid}\n")
        sb.append(f"- Erreurs de parsing        : ${report.statistics.parsing_errors}\n")
        sb.append(f"- Doublons supprimes        : ${report.statistics.duplicated_removed}\n\n")
        
        sb.append("TOP 10 - BUTEURS\n")
        sb.append("-------------------\n")
        report.top_ten_Scores.zipWithIndex.foreach { case (player, idx) =>
          sb.append(f"${idx + 1}. ${player.name}%-25s : ${player.value.toInt} buts en ${player.matches} matchs\n")
        }
        sb.append("\n")

        sb.append("TOP 10 - PASSEURS\n")
        sb.append("---------------------\n")
        report.top_ten_assisters.zipWithIndex.foreach { case (player, idx) =>
          sb.append(f"${idx + 1}. ${player.name}%-25s : ${player.value.toInt} passes en ${player.matches} matchs\n")
        }
        sb.append("\n")

        sb.append("TOP 10 - VALEUR MARCHANDE\n")
        sb.append("-----------------------------\n")
        report.most_valuable_players.zipWithIndex.foreach { case (player, idx) =>
          sb.append(f"${idx + 1}. ${player.name}%-25s : ${player.value.toInt} M EUR\n")
        }
        sb.append("\n")

        sb.append("TOP 10 - SALAIRES\n")
        sb.append("--------------------\n")
        report.highest_paid_players.zipWithIndex.foreach { case (player, idx) =>
          sb.append(f"${idx + 1}. ${player.name}%-25s : ${player.value}%.2f M EUR/an\n")
        }
        sb.append("\n")

        sb.append("REPARTITION PAR LIGUE\n")
        sb.append("-------------------------\n")
        report.players_by_league.toList.sortBy(-_._2).foreach { case (league, count) =>
          sb.append(f"- ${league}%-25s : $count joueurs\n")
        }
        sb.append("\n")

        sb.append("REPARTITION PAR POSTE\n")
        sb.append("------------------------\n")
        report.players_by_position.toList.sortBy(-_._2).foreach { case (position, count) =>
          sb.append(f"- ${position}%-25s : $count joueurs\n")
        }
        sb.append("\n")

        sb.append("MOYENNES PAR POSTE\n")
        sb.append("----------------------\n")
        sb.append("AGE MOYEN :\n")
        report.average_age_by_position.toList.sortBy(-_._2).foreach { case (position, avg) =>
          sb.append(f"- ${position}%-25s : $avg%.1f ans\n")
        }
        sb.append("\n")

        sb.append("BUTS PAR MATCH (moyenne) :\n")
        report.average_goals_by_position.toList.sortBy(-_._2).foreach { case (position, avg) =>
          sb.append(f"- ${position}%-25s : $avg%.2f buts\n")
        }
        sb.append("\n")

        sb.append("DISCIPLINE\n")
        sb.append("--------------\n")
        sb.append(f"- Total cartons jaunes      : ${report.discipline_statistics.total_yellow_cards}\n")
        sb.append(f"- Total cartons rouges      : ${report.discipline_statistics.total_red_cards}\n")
        sb.append(f"- Poste le plus discipline  : ${report.discipline_statistics.most_disciplined_position}\n")
        sb.append(f"- Poste le moins discipline : ${report.discipline_statistics.least_disciplined_position}\n\n")
        
        val endTime = System.currentTimeMillis()
        val duration = (endTime - startTime) / 1000.0
        val entriesPerSecond = if (duration > 0) report.statistics.tot_players / duration else 0.0
        
        sb.append("PERFORMANCE\n")
        sb.append("---------------\n")
        sb.append(f"- Temps de traitement       : $duration%.3f secondes\n")
        sb.append(f"- Entrees/seconde           : $entriesPerSecond%.0f\n")
        
        sb.toString()

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
