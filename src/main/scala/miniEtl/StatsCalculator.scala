package miniEtl

import io.circe._
import io.circe.parser._
import scala.io.Source

object StatsCalculator {

    def calculateEtlStats(filename : String) : EtlStats = {
        val source = Source.fromFile(filename)
        val content = source.mkString
        source.close()
        val totalEntries = decode[Json](content) match {
            case Right(json) => json.asArray.map(_.size).getOrElse(0)
            case Left(_) => 0
        }
         val players: List[Player] = DataLoader.loadPlayers(filename) match {
            case Right(list) => list          
            case Left(error) => 
                println(s"Erreur lors du chargement : $error")
                List()                          
        }  
        val validPlayers = DataValidator.filterValid(players)
        val finalPlayers = DataValidator.removeDuplicates(validPlayers)

        EtlStats(
            totalEntries = totalEntries,
            validEntries = finalPlayers.length,
            parsingErrors = totalEntries - players.length,
            duplicatesRemoved = validPlayers.length - finalPlayers.length
         )

    }

    def calculateTopScorers(players : List[Player]) : List[TopPlayer] = {
        players.sortBy(- _.goalsScored).take(10).map( x => TopPlayer(
            x.name,
            x.club,
            x.goalsScored.toDouble,
            x.matchesPlayed
        ))

    }

    def calculateTopAssisters(players : List[Player]): List[TopPlayer] = {
        players.sortBy(- _.assists).take(10).map( x => TopPlayer(
            x.name,
            x.club,
            x.assists.toDouble,
            x.matchesPlayed
        ))

    }
    def calculateMostValuablePlayers(players : List[Player]): List[TopPlayer] = {

        players.sortBy(- _.marketValue.getOrElse(0)).take(10).map( x => TopPlayer(
            x.name,
            x.club,
            x.marketValue.getOrElse(0).toDouble,
            x.matchesPlayed
        ))

    }
    def calculateHighestPaidPlayers(players: List[Player]): List[TopPlayer] = {
        players.sortBy(- _.salary.getOrElse(0.0)).take(10).map( x => TopPlayer(
            x.name,
            x.club,
            x.salary.getOrElse(0.0),
            x.matchesPlayed
        ))
    }

    def countPlayersByLeague(players: List[Player]): Map[String,Int] = {
        players.
        groupBy(_.league)
        .map {
            case (k,v) => (k , v.size)
        }

    }

    def countPlayersByPosition(players : List[Player]): Map[String,Int] = {
        players.
        groupBy(_.position)
        .map {
            case (k,v) => (k , v.size)
        }
    }

    def calculateAverageAgeByPosition(players: List[Player]): Map[String,Double] ={
        players.
        groupBy(_.position)
        .map {
            case (pos,playerList) => 
                val totalAge = playerList.map(_.age).sum
                val average = totalAge.toDouble / playerList.size
                (pos,average)

        }

    }
    def calculateAverageGoalsByPosition(players : List[Player]): Map[String,Double] = {
        players.
        groupBy(_.position)
        .map {
            case (pos,playerList) => 
                val totalGoal = playerList.map(_.goalsScored).sum
                val average = totalGoal.toDouble / playerList.size
                (pos,average)

        }
    }
    def calculateDisciplineStats(players: List[Player]): DisciplineStats = {

    val penaltiesByPosition: List[(String, Int)] =
        players
        .groupBy(_.position)
        .map { case (pos, list) =>
            val yellow = list.map(_.yellowCards).sum
            val red    = list.map(_.redCards).sum
            val totalPenalty = yellow + (red * 2)
            (pos, totalPenalty)
        }
        .toList
        .sortBy(- _._2) 

    val leastDisciplined = penaltiesByPosition.last._1
    val mostDisciplined  = penaltiesByPosition.head._1

    DisciplineStats(
        totalYellowCards = players.map(_.yellowCards).sum,
        totalRedCards    = players.map(_.redCards).sum,
        mostDisciplined,
        leastDisciplined
    )
    }
    
}