error id: file://<WORKSPACE>/src/main/scala/miniEtl/Football.scala:
file://<WORKSPACE>/src/main/scala/miniEtl/Football.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 357
uri: file://<WORKSPACE>/src/main/scala/miniEtl/Football.scala
text:
```scala
package miniEtl

case class Football(
  id: Int,
  name: String,
  age: Int,
  nationality: String,
  position: String,
  club: String,
  league: String,
  goalsScored: Int,
  assists: Int,
  matchesPlayed: Int,
  yellowCards: Int,
  redCards: Int,
  marketValue: Option[Int],
  salary: Option[Double] 
)
case class FootballInt(
  id: Option[Int],
  name: [@@String],
  age: [Int],
  nationality: [String],
  position: [String],
  club: [String],
  league: [String],
  goalsScored: [Int],
  assists: [Int],
  matchesPlayed: [Int],
  yellowCards: [Int],
  redCards: [Int],
  marketValue: Option[Int],
  salary: Option[Double] 
)

case class FootballStats(
    tot_players: Int,
    tot_players_valid: Int,
    parsing_errors: Int,
    duplicated_removed: Int

)

case class TopScores(
    name: String,
    club: String,
    goals: Int,
    matches: Int
)

case class TopTenAssisters(
    top10Assisters: List[Football]
)
case class MostValuablePlayers(
    mostValuablePlayers: List[Football])
case class HighestPaidPlayers(
    highest_paid_players: List[Football])

case class PlayersByLeague(
    premierLeague: Int,
    laLiga: Int,
    serieA: Int,
    bundesliga: Int,
    ligue1: Int
)

case class PlayersByPosition(
    goalkeeper: Int,
    defender: Int,
    midfielder: Int,
    forward: Int
)

case class AverageAgeByPosition(
    Goalkeeper: String,
    Defender: String,
    Midfielder: String,
    Forward: String
)

case class AverageGoalsByPosition(
    forward: Int,
    midfielder: Int,
    defender: Int,
    goalkeeper: Int
)

case class DisciplineStatistics(
    total_yellow_cards: Int,
    total_red_cards: Int,
    most_disciplined_position: String,
    least_disciplined_position: String
)

case class AnalysisReport(
    statistics: FootballStats,
    top_ten_Scores: List[TopScores],
    top_ten_assisters: List[Football],
    most_valuable_players: List[Football],
    highest_paid_players: List[Football],
    players_by_league: PlayersByLeague,
    players_by_position: PlayersByPosition,
    average_age_by_position: AverageAgeByPosition,
    average_goals_by_position: AverageGoalsByPosition,
    discipline_statistics: DisciplineStatistics
)


```


#### Short summary: 

empty definition using pc, found symbol in pc: 