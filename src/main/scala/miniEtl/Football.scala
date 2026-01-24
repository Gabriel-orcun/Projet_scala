package miniEtl

case class Football(
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

case class FootballStats(
    tot_players:            Int,
    tot_players_valid:      Int,
    parsing_errors:         Int,
    duplicated_removed:     Int

)

case class TopPlayer(
    name:       String,
    club:       String,
    value:      Double,
    matches:    Int
)

/*
case class TopTenScored(
    top_10_Scored:      List[TopPlayer]
)

case class TopTenAssisters(
    top10Assisters:         List[TopPlayer]
)

case class MostValuablePlayers(
    mostValuablePlayers:    List[TopPlayer]
    )

case class HighestPaidPlayers(
    highest_paid_players:   List[TopPlayer]
    )

case class PlayersByLeague(
    league:     String,
    number:     Int
)

case class PlayersByPosition(
   position:    String,
   number:      Int
)

case class AverageAgeByPosition(
    Position:       String,
    average_age:    Double
)

case class AverageGoalsByPosition(
    position:       String,
    average_goal:   Double
)
*/

case class DisciplineStatistics(
    total_yellow_cards:         Int,
    total_red_cards:            Int,
    most_disciplined_position:  String,
    least_disciplined_position: String
)

case class AnalysisReport(
    statistics:                 FootballStats,
    top_ten_Scores:             List[TopPlayer],
    top_ten_assisters:          List[TopPlayer],
    most_valuable_players:      List[TopPlayer],
    highest_paid_players:       List[TopPlayer],
    players_by_league:          Map[String,Int],
    players_by_position:        Map[String,Int],
    average_age_by_position:    Map[String,Double],
    average_goals_by_position:  Map[String,Double],
    discipline_statistics:      DisciplineStatistics
)

