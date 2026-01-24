package miniEtl

object Main extends App {
  
  println("===============================================")
  println("   RAPPORT D'ANALYSE - JOUEURS DE FOOTBALL")
  println("===============================================")
  
  val startTime = System.currentTimeMillis()
  val filename = "data/data_dirty.json"
  
  val result = for {
    players <- DataLoader.loadFootball(filename)
    _ = println(s"${players.length} joueurs chargés")
    
    validPlayers = DataValidator.filterValid(players)
    _ = println(s"${validPlayers.length} joueurs valides")
    
    finalPlayers = DataValidator.removeDuplicates(validPlayers)
    _ = println(s"${finalPlayers.length} joueurs finaux (${validPlayers.length - finalPlayers.length} doublons supprimés)")
    
    report = ReportGenerator.geratereport(finalPlayers, filename)
    _ = println("Rapport généré")
    
    _ <- ReportGenerator.writeReport(report, "results.json")
    _ = println("Rapport écrit dans results.json")
    
  } yield report
  
  result match {
    case Right(report) =>
      println("\nSTATISTIQUES DE PARSING")
      println("---------------------------")
      println(f"- Entrées totales lues      : ${report.statistics.tot_players}")
      println(f"- Entrées valides           : ${report.statistics.tot_players_valid}")
      println(f"- Erreurs de parsing        : ${report.statistics.parsing_errors}")
      println(f"- Doublons supprimés        : ${report.statistics.duplicated_removed}")
      
      println("\nTOP 10 - BUTEURS")
      println("-------------------")
      report.top_ten_Scores.zipWithIndex.foreach { case (player, idx) =>
        println(f"${idx + 1}. ${player.name}%-25s : ${player.value.toInt} buts en ${player.matches} matchs")
      }
      
      println("\nTOP 10 - PASSEURS")
      println("---------------------")
      report.top_ten_assisters.zipWithIndex.foreach { case (player, idx) =>
        println(f"${idx + 1}. ${player.name}%-25s : ${player.value.toInt} passes en ${player.matches} matchs")
      }
      
      println("\nTOP 10 - VALEUR MARCHANDE")
      println("-----------------------------")
      report.most_valuable_players.zipWithIndex.foreach { case (player, idx) =>
        println(f"${idx + 1}. ${player.name}%-25s : ${player.value.toInt} M€")
      }
      
      println("\nTOP 10 - SALAIRES")
      println("--------------------")
      report.highest_paid_players.zipWithIndex.foreach { case (player, idx) =>
        println(f"${idx + 1}. ${player.name}%-25s : ${player.value}%.2f M€/an")
      }
      
      println("\nREPARTITION PAR LIGUE")
      println("-------------------------")
      report.players_by_league.toList.sortBy(-_._2).foreach { case (league, count) =>
        println(f"- ${league}%-25s : $count joueurs")
      }
      
      println("\nREPARTITION PAR POSTE")
      println("------------------------")
      report.players_by_position.toList.sortBy(-_._2).foreach { case (position, count) =>
        println(f"- ${position}%-25s : $count joueurs")
      }
      
      println("\nMOYENNES PAR POSTE")
      println("----------------------")
      println("AGE MOYEN :")
      report.average_age_by_position.toList.sortBy(-_._2).foreach { case (position, avg) =>
        println(f"- ${position}%-25s : $avg%.1f ans")
      }
      
      println("\nBUTS PAR MATCH (moyenne) :")
      report.average_goals_by_position.toList.sortBy(-_._2).foreach { case (position, avg) =>
        println(f"- ${position}%-25s : $avg%.2f buts")
      }
      
      println("\nDISCIPLINE")
      println("--------------")
      println(f"- Total cartons jaunes      : ${report.discipline_statistics.total_yellow_cards}")
      println(f"- Total cartons rouges      : ${report.discipline_statistics.total_red_cards}")
      println(f"- Poste le plus discipliné  : ${report.discipline_statistics.most_disciplined_position}")
      println(f"- Poste le moins discipliné : ${report.discipline_statistics.least_disciplined_position}")
      
      val endTime = System.currentTimeMillis()
      val duration = (endTime - startTime) / 1000.0
      val entriesPerSecond = if (duration > 0) report.statistics.tot_players / duration else 0.0
      
      println("\nPERFORMANCE")
      println("---------------")
      println(f"- Temps de traitement       : $duration%.3f secondes")
      println(f"- Entrées/seconde           : $entriesPerSecond%.0f")
      
      println("\n===============================================")
      
    case Left(error) =>
      println(s"\nERREUR: $error")
      sys.exit(1)
  }
}
