package cqrs.workshop.tictactoe.api

object Commands {

  case class StartGameCommand(
    gameId: String,
    playerX: String,
    playerO: String
  )

  case class MoveCommand(
    gameId: String,
    player: String,
    move: Int
  )

}
