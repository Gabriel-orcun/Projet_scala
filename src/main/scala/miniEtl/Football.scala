package miniEtl

case class Player(
  id:               Int,
  name:             String,
  age:              Int,
  nationality:      String,
  position:         String,
  club:             String,
  league:           String,
  goalsScored:      Int,
  assists:          Int,
  matchesPlayed:    Int,
  yellowCards:      Int,
  redCards:         Int,
  marketValue:      Option[Int],
  salary:           Option[Double]
)

case class EtlStats(
    totalEntries:         Int,
    validEntries:         Int,
    parsingErrors:        Int,
    duplicatesRemoved:    Int
)

case class TopPlayer(
    name:       String,
    club:       String,
    value:      Double,
    matches:    Int
)

case class DisciplineStats(
    totalYellowCards:         Int,
    totalRedCards:            Int,
    mostDisciplinedPosition:  String,
    leastDisciplinedPosition: String
)

case class AnalysisReport(
    etlStats:                   EtlStats,
    topTenScorers:              List[TopPlayer],
    topTenAssisters:            List[TopPlayer],
    mostValuablePlayers:        List[TopPlayer],
    highestPaidPlayers:         List[TopPlayer],
    playersByLeague:            Map[String,Int],
    playersByPosition:          Map[String,Int],
    averageAgeByPosition:       Map[String,Double],
    averageGoalsByPosition:     Map[String,Double],
    disciplineStats:            DisciplineStats
)

