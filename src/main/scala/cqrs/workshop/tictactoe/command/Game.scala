package cqrs.workshop.tictactoe.command

import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot
import org.axonframework.eventhandling.annotation.EventHandler
import org.axonframework.domain.{StringAggregateIdentifier, AggregateIdentifier}

import cqrs.workshop.tictactoe.api.IllegalMoveException
import cqrs.workshop.tictactoe.api.Events._

/**
 * Aggregate root for a Tic Tac Toe Game.
 */
class Game(id: AggregateIdentifier) extends AbstractAnnotatedAggregateRoot(id) {
  private val gameId: String = id.asString()
  private var playerX: String = _
  private var playerO: String = _
  private var board: Board = _

  def this(gameId: String, playerX: String, playerO: String) {
    this(new StringAggregateIdentifier(gameId))
    if (playerX == playerO) throw new IllegalMoveException("Players may not have same name")
    apply(GameCreatedEvent(gameId, playerX, playerO, EmptyBoard.positions))
  }

  def move(player: String, move: Int) {
    // TODO: Add implementation:
    // 1. validate input
    // 2. determine state of the board after the move
    // 3. either apply appropriate new event, or throw IllegalMoveException
  }

  @EventHandler
  private def onCreate(event: GameCreatedEvent) {
    playerX = event.playerX
    playerO = event.playerO
    board = EmptyBoard
  }

  private def onMove(event: GameMoveEvent) {
    // TODO: .....
  }

}
