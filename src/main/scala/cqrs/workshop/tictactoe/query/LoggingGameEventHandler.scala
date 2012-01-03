package cqrs.workshop.tictactoe.query

import org.axonframework.eventhandling.annotation.EventHandler
import org.springframework.stereotype.Component
import cqrs.workshop.tictactoe.api.Events._
import cqrs.workshop.tictactoe.command.Board

/**
 * Logs all game changes to sysout.
 *
 */
@Component
class LoggingGameEventHandler {

  @EventHandler
  def handleGameCreated(event: GameCreatedEvent): Unit =
    println("New game (game %s) %s (X) vs %s (O)".format(event.gameId, event.playerX, event.playerO))

  @EventHandler
  def handleGameMove(event: GameMoveEvent): Unit = {
    def state =
      if (event.winner == null) ""
      else if (event.winner == "draw") "(DRAW) "
      else "and WINS "

    println("(game %s) %s played %d %s-->\n%s".format(
      event.gameId, event.player, event.move, state, Board.formatBoard(event.positions)))
  }
}
