package miniEtl

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import scala.util.Try
import miniEtl.StatsCalculator.*

object ReportGenerator {
    def geratereport(players: List[Football], filename: String): AnalysisReport = {
        AnalysisReport(
            footballStats(filename),
            topScores(players),
            topTenAssisters(players),
            mostValuablePlayers(players),
            highestpPaidPlayers(players),
            playersByLeague(players),
            playersByPosition(players),
            averageAgeByPosition(players),
            averageGoalsByPosition(players),
            disciplineStatistics(players)
        )
    }

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