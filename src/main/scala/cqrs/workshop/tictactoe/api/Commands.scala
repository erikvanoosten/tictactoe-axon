package cqrs.workshop.tictactoe.api

object Commands {

  case class StartGameCommand(
    gameId: String,
    playerX: String,
    playerO: String
  )

  // TODO: add MoveCommand

}
