package cqrs.workshop.tictactoe.query

/**
 * Query repository for {@link Game}s.
 *
 */
trait GameQueryRepository {
  def findGames(): Seq[Game]
}
