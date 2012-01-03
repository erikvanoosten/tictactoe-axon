package cqrs.workshop.tictactoe.command

import org.axonframework.commandhandling.annotation.CommandHandler
import org.axonframework.domain.StringAggregateIdentifier
import org.axonframework.repository.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import cqrs.workshop.tictactoe.api.Commands._

/**
 * Process game commands.
 */
@Component
class GameCommandHandler {
  private var gameRepository: Repository[Game] = _

  @CommandHandler
  def createGame(command: StartGameCommand) {
    gameRepository.add(new Game(/* TODO: ....... */))
  }

  def move(command: MoveCommand) {
    val game = gameRepository.load(new StringAggregateIdentifier(command.gameId))
    // TODO
  }

  @Autowired
  def setGameRepository(gameRepository: Repository[Game]) {
    this.gameRepository = gameRepository;
  }
}
