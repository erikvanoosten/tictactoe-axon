package cqrs.workshop.tictactoe.command

import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot
import org.axonframework.eventhandling.annotation.EventHandler
import org.axonframework.domain.{StringAggregateIdentifier, AggregateIdentifier}

import cqrs.workshop.tictactoe.api.IllegalMoveException
import cqrs.workshop.tictactoe.api.Events._

/**
 * Aggregate root for a Tic Tac Toe Game.
 * 
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
    board.move(playerFor(player), move) match {
      case Left(errMsg) => throw new IllegalMoveException(errMsg)
      case Right(WinningBoard(winner, positions)) => apply(GameMoveEvent(gameId, player, move, positions, nameFor(winner)))
      case Right(DrawBoard(positions)) => apply(GameMoveEvent(gameId, player, move, positions, "draw"))
      case Right(PlayingBoard(_, positions)) => apply(GameMoveEvent(gameId, player, move, positions, null))
    }
  }

  @EventHandler
  private def onCreate(event: GameCreatedEvent) {
    playerX = event.playerX
    playerO = event.playerO
    board = EmptyBoard
  }

  @EventHandler
  private def onMove(event: GameMoveEvent) {
    board = board.move(playerFor(event.player), event.move) match {
      case Left(errMsg) => new IllegalBoard(board.positions, errMsg)
      case Right(newBoard) => newBoard
    }
  }

  private def playerFor(playerName: String): Player =
    if (playerX == playerName) X
    else if (playerO == playerName) O
    else throw new IllegalMoveException("Unknown player " + playerName)

  private def nameFor(player: Player) = player match {
    case X => playerX
    case O => playerO
  }

  case class IllegalBoard(positions: String, lastErr: String) extends Board {
    def move(p: Player, move: Int) = Left(lastErr)
    def nextPlayer = None
    def finished = true
    def winner = None
  }

}
