package miniEtl

object DataValidator {

  def isValid(player: Football): Boolean = {
    player.id > 0 &&
    player.name.trim.nonEmpty &&
    player.nationality.trim.nonEmpty &&
    player.position.trim.nonEmpty &&
    player.club.trim.nonEmpty &&
    player.league.trim.nonEmpty &&
    player.age > 0 && player.age < 50 &&
    player.goalsScored >= 0 &&
    player.assists >= 0 &&
    player.matchesPlayed >= 0 &&
    player.marketValue.isDefined &&
    player.salary.isDefined
  }

  def filterValid(players: List[Football]): List[Football] = {
    players.filter(isValid)
  }


  def removeDuplicates(players: List[Football]): List[Football] = {
    players.distinctBy(_.id)

  }
}
