package miniEtl
// Classe principale réalisée à partir des données du data set clean.
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
// Classe pour récupérer les statistiques concernant notre ETL
case class EtlStats(
    totalEntries:         Int,
    validEntries:         Int,
    parsingErrors:        Int,
    duplicatesRemoved:    Int
)
// Classe utilisée plusieurs fois pour représenter les résultats de nos différentes fonctions
// Parti pris avec la valeur double car la classe va être utilisée pour le prix qui 
//est un Double mais aussi pour topTenAssisters et topTenScorers qui ont un int. 
// Pour éviter de créer une autre classe avec uniquement le champ value en int
// on représentera nos int en doubles
case class TopPlayer(
    name:       String,
    club:       String,
    value:      Double,
    matches:    Int
)
// Classe qui représente les statistiques des cartons avec les positions les plus et moins disciplinées
case class DisciplineStats(
    totalYellowCards:         Int,
    totalRedCards:            Int,
    mostDisciplinedPosition:  String,
    leastDisciplinedPosition: String
)
// Classe permettant de représenter notre rapport d'analyse
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

