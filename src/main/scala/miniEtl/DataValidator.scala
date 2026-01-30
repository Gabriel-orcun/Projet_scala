package miniEtl

object DataValidator {
// Fonction qui permet de normaliser les erreurs de nom dans notre data
  def normalisePosition(pos: String): String = {
    pos match {
      case "ATT" | "Attacker" => "Forward"
      case "DEF"  => "Defender"
      case _ => pos
      
    }
    
  }

 
// Fonction qui permet de dire si un joueur respecte ou non les règles métier
  def isValid(player: Player): Boolean = {
    player.name.trim.nonEmpty &&
    player.nationality.trim.nonEmpty &&
    player.position.trim.nonEmpty &&
    player.club.trim.nonEmpty &&
    player.league.trim.nonEmpty &&
    player.age >= 16 && player.age <= 45 &&
    player.goalsScored >= 0 &&
    player.assists >= 0 &&
    player.matchesPlayed > 0
  }
// Fonction qui filtre et garde uniquement les joueurs qui valident les règles métier
  def filterValid(players: List[Player]): List[Player] = {
    players
    .map(player => player.copy(position = normalisePosition(player.position)))
    .filter(isValid)
  }

// Fonction qui supprime tous les doublons
  def removeDuplicates(players: List[Player]): List[Player] = {
    players.distinctBy(_.id)

  }
}
