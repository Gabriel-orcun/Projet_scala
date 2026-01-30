package miniEtl

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import scala.util.Try
import miniEtl.StatsCalculator.*
import miniEtl.Bonus.*

object ReportGenerator {
  // Fonction qui produit notre rapport en instanciant un analysis report en sortie
    def generateReport(players: List[Player], filename: String): AnalysisReport = {
        AnalysisReport(
            etlStats = calculateEtlStats(filename),
            topTenScorers = calculateTopScorers(players),
            topTenAssisters = calculateTopAssisters(players),
            mostValuablePlayers = calculateMostValuablePlayers(players),
            highestPaidPlayers = calculateHighestPaidPlayers(players),
            playersByLeague = countPlayersByLeague(players),
            playersByPosition = countPlayersByPosition(players),
            averageAgeByPosition = calculateAverageAgeByPosition(players),
            averageGoalsByPosition = calculateAverageGoalsByPosition(players),
            disciplineStats = calculateDisciplineStats(players),
            topTenEfficient = topTenEfficient(players),
            topTenQualityPrice = topTenQualityPrice(players),
            averageAgeByLeague = calculateAverageAgeByLeague(players),
            averageGoalsByLeague = calculateAverageGoalsByLeague(players)
        )
    }
// Classe qui va créer le rapport et l'écrire
    def writeReport(report: AnalysisReport, filename: String): Either[String, Unit] = {
    Try {
      val json = report.asJson.spaces2
      Files.write(
        Paths.get(filename),
        json.getBytes(StandardCharsets.UTF_8)
      )
    }
      .toEither
      .left.map(_.getMessage)
      .map(_ => ())
  }
}