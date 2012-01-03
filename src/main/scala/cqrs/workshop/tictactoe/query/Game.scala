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

  @Id @BeanProperty private var gameId: String = _
  @Basic @BeanProperty private var playerX: String = _
  @Basic @BeanProperty private var playerO: String = _
  @Basic @BeanProperty private var positions: String = _
  @Basic @BeanProperty private var winner: String = _
}
