package miniEtl

import io.circe._
import io.circe.parser._
import scala.io.Source

object Bonus {
// Fonction qui renvoie les 10 plus efficaces selon un ratio Buts/matchs joués
    def topTenEfficient(players: List[Player]): List[TopPlayer] = {
        players.map(x => TopPlayer(
            name= x.name,
            club= x.club,
            value= if (x.matchesPlayed==0) 0 else (x.goalsScored.toDouble / x.matchesPlayed),
            matches=   x.matchesPlayed
        )).sortBy(- _.value).take(10)
    }
// Fonction qui renvoie les 10 plus rentables selon un ratio Buts/prix
      def topTenQualityPrice(players: List[Player]): List[TopPlayer] = {
        players.map(x => TopPlayer(
            name= x.name,
            club= x.club,
            value= (x.goalsScored.toDouble / x.salary.getOrElse(1.0)),
            matches=   x.matchesPlayed
        )).sortBy(- _.value).take(10)
    }
// Fonction qui calcule l'âge moyen par ligue
        def calculateAverageAgeByLeague(players: List[Player]): Map[String,Double] ={
            players.
            groupBy(_.league)
            .map {
                case (li,playerList) => 
                    val totalAge = playerList.map(_.age).sum
                    val average = totalAge.toDouble / playerList.size
                    (li,average)

            }

        }
// Fonction qui calcule la moyenne de buts par ligue 
        def calculateAverageGoalsByLeague(players : List[Player]): Map[String,Double] = {
        players.
        groupBy(_.league)
        .map {
            case (li,playerList) => 
                val totalGoal = playerList.map(_.goalsScored).sum
                val totalMatches = playerList.map(_.matchesPlayed).sum
                val average = totalGoal.toDouble / totalMatches
                (li,average)

        }
    }



}