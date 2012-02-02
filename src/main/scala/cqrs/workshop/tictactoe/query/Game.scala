package cqrs.workshop.tictactoe.query

import javax.persistence._
import reflect.BeanProperty

/**
 * Simple JPA representation of a Game.
 *
 */
@Entity
@Table(name = "Game")
class Game() {

  def this(gameId: String, playerX: String, playerO: String, positions: String, winner: String) {
    this ()
    this.gameId = gameId
    this.playerX = playerX
    this.playerO = playerO
    this.positions = positions
    this.winner = winner
  }

  @Id @BeanProperty var gameId: String = _
  @Basic @BeanProperty var playerX: String = _
  @Basic @BeanProperty var playerO: String = _
  @Basic @BeanProperty var positions: String = _
  @Basic @BeanProperty var winner: String = _
}
