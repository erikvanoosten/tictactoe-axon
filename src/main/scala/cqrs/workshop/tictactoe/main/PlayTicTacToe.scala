package cqrs.workshop.tictactoe.main

import org.springframework.context.support.ClassPathXmlApplicationContext
import org.axonframework.commandhandling.{GenericCommandMessage, CommandBus}

import cqrs.workshop.tictactoe.api.Commands._
import cqrs.workshop.tictactoe.command.Board
import cqrs.workshop.tictactoe.query.{Game, GameQueryRepository}
import org.springframework.context.ConfigurableApplicationContext
import GenericCommandMessage.asCommandMessage

/**
 * Example game of Tic Tac Toe.
 * 
 */
object PlayTicTacToe {

  def main(args: Array[String]) {
    val appCtx: ConfigurableApplicationContext = new ClassPathXmlApplicationContext(
      "META-INF/spring/application-context.xml",
      "META-INF/spring/database-context.xml")
    val queryRepository: GameQueryRepository = appCtx.getBean(classOf[GameQueryRepository])
    val commandBus: CommandBus = appCtx.getBean(classOf[CommandBus])

    val abel = "Abel"
    val bob = "Bob"
    val clark = "Clark"
    val dave = "Dave"
    commandBus.dispatch(asCommandMessage(StartGameCommand("1", abel, bob)))
    commandBus.dispatch(asCommandMessage(MoveCommand("1", abel, 2)))
    commandBus.dispatch(asCommandMessage(MoveCommand("1", bob, 3)))
    commandBus.dispatch(asCommandMessage(StartGameCommand("2", clark, dave)))
    commandBus.dispatch(asCommandMessage(MoveCommand("2", clark, 6)))
    commandBus.dispatch(asCommandMessage(MoveCommand("2", dave, 5)))
    commandBus.dispatch(asCommandMessage(MoveCommand("1", abel, 4)))
    commandBus.dispatch(asCommandMessage(MoveCommand("2", clark, 3)))
    commandBus.dispatch(asCommandMessage(MoveCommand("2", dave, 4)))
    commandBus.dispatch(asCommandMessage(MoveCommand("1", bob, 5)))
    commandBus.dispatch(asCommandMessage(MoveCommand("2", clark, 9)))

    // Display result
    val games = queryRepository.findGames()
    for (game <- games) {
      println("Game %s, %s%s vs %s%s with board:\n%s".format(
        game.gameId,
        game.playerX, statusPlayer(game, game.playerX),
        game.playerO, statusPlayer(game, game.playerO),
        Board.formatBoard(game.positions)))
    }

    appCtx.close()

    def statusPlayer(game: Game, player: String): String =
      if (game.winner == null) ""
      else if (game.winner == player) " (winner)"
      else " (loser)"
  }

}
