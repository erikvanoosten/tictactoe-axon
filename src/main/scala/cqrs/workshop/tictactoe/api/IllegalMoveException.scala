package cqrs.workshop.tictactoe.api

/**
 * Exception for illegal moves.
 *
 */
class IllegalMoveException(msg: String) extends RuntimeException(msg)
