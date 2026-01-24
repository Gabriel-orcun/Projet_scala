package miniEtl

import io.circe._
import io.circe.parser._
import scala.io.Source

object StatsCalculator {

    def footballStats(filename : String) : FootballStats = {
        val source = Source.fromFile(filename)
        val content = source.mkString
        source.close()
        val totalPlayers = decode[Json](content) match {
            case Right(json) => json.asArray.map(_.size).getOrElse(0)
            case Left(_) => 0
        }
         val players: List[Football] = DataLoader.loadFootball(filename) match {
            case Right(list) => list          
            case Left(error) => 
                println(s"Erreur lors du chargement : $error")
                List()                          
        }  
        val listValide = DataValidator.filterValid(players)
        val listFinale = DataValidator.removeDuplicates(listValide)

        FootballStats(
        tot_players = totalPlayers,
        tot_players_valid = listFinale.length,
        parsing_errors = totalPlayers - players.length,
        duplicated_removed = listValide.length - listFinale.length
         )

    }

    def topScores(players : List[Football]) : List[TopPlayer] = {
        players.sortBy(- _.goalsScored).take(10).map( x => TopPlayer(
            x.name,
            x.club,
            x.goalsScored.toDouble,
            x.matchesPlayed
        ))

    }

    def topTenAssisters(players : List[Football]): List[TopPlayer] = {
        players.sortBy(- _.assists).take(10).map( x => TopPlayer(
            x.name,
            x.club,
            x.assists.toDouble,
            x.matchesPlayed
        ))

    }
    def mostValuablePlayers(players : List[Football]): List[TopPlayer] = {

        players.sortBy(- _.marketValue.getOrElse(0)).take(10).map( x => TopPlayer(
            x.name,
            x.club,
            x.marketValue.getOrElse(0).toDouble,
            x.matchesPlayed
        ))

    }
    def highestpPaidPlayers(players: List[Football]): List[TopPlayer] = {
        players.sortBy(- _.salary.getOrElse(0.0)).take(10).map( x => TopPlayer(
            x.name,
            x.club,
            x.salary.getOrElse(0.0),
            x.matchesPlayed
        ))
    }

    def playersByLeague (players: List[Football]): Map[String,Int] = {
        players.
        groupBy(_.league)
        .map {
            case (k,v) => (k , v.size)
        }

    }

    def playersByPosition(players : List[Football]): Map[String,Int] = {
        players.
        groupBy(_.position)
        .map {
            case (k,v) => (k , v.size)
        }
    }

    def averageAgeByPosition(players: List[Football]): Map[String,Double] ={
        players.
        groupBy(_.position)
        .map {
            case (pos,playlist) => 
                val totalAge = playlist.map(_.age).sum
                val average = totalAge.toDouble / playlist.size
                (pos,average)

        }

    }
    def averageGoalsByPosition(players : List[Football]): Map[String,Double] = {
        players.
        groupBy(_.position)
        .map {
            case (pos,playlist) => 
                val totalGoal = playlist.map(_.goalsScored).sum
                val average = totalGoal.toDouble / playlist.size
                (pos,average)

        }
    }
    def disciplineStatistics(players: List[Football]): DisciplineStatistics = {

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

    val least_disciplined_position = penaltiesByPosition.last._1
    val most_disciplined_position  = penaltiesByPosition.head._1

    DisciplineStatistics(
        total_yellow_cards = players.map(_.yellowCards).sum,
        total_red_cards    = players.map(_.redCards).sum,
        most_disciplined_position,
        least_disciplined_position
    )
    }
    
}