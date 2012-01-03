package cqrs.workshop.tictactoe.command

/**
 * An implementation of Tic Tac Toe.
 *
 * Loosely based on http://torquingwetstrainers.wordpress.com/2010/04/28/tic-tac-toe-scala/
 *
 * @author Erik van Oosten
 */
import java.io.{BufferedReader, InputStreamReader}

/**
 * Main class for testing purposes.
 */
object TicTacToe {

  private val input = new BufferedReader(new InputStreamReader(System.in))

  def main(args: Array[String]) {
    var progress: Progress = Continue(X)

    var board: Board = EmptyBoard
    while (true) {
      println
      println(Board.printForInput(board.positions))
      println
      print(progress)

      progress match {
        case Continue(player) => {
          val move = readAnswer
          val newBoard = board.move(player, move)
          newBoard match {
            case Left(errMsg) => println(errMsg)
            case Right(newBoard) =>
              board = newBoard
              progress = board match {
                case WinningBoard(winner, _) => Win(winner)
                case DrawBoard(_) => Draw
                case _ => Continue(player.next)
              }
          }
        }
        case _ => return
      }
    }
  }

  def readAnswer: Int = {
    try {
      input.readLine().toInt
    } catch {
      case nfe: NumberFormatException => 0
    }
  }

  abstract class Progress
  case class Win(player: Player) extends Progress {
    override def toString() = "" + player + " Wins!\n"
  }
  case object Draw extends Progress {
    override def toString() = "It's a Draw!\n"
  }
  case class Continue(player: Player) extends Progress {
    override def toString() = "Select a square, " + player + ": "
  }
}

/**
 * An immutable TicTacToe board.
 */
trait Board {
  /** @return the new board (Right), or an error message (Left) when the move is illegal */
  def move(p: Player, move: Int): Either[String, Board]

  def nextPlayer: Option[Player]
  def finished: Boolean
  def winner: Option[Player]
  /** A 9 character string with " ": empty position, "X": played by X, "O": played by O. */
  def positions: String
  override def toString = Board.formatBoard(positions)
}

case class WinningBoard(winningPlayer: Player, positions: String) extends Board {
  assert(positions.matches("[ XO]{9}"), "Not a valid board " + positions)
  def move(p: Player, move: Int): Either[String, Board] = Left("Already won by "+ winningPlayer)
  val nextPlayer = None
  val finished = true
  val winner = Some(winningPlayer)
}

case class DrawBoard(positions: String) extends Board {
  assert(positions.matches("[XO]{9}"), "Not a valid draw board " + positions)
  def move(p: Player, move: Int): Either[String, Board] = Left("Its already a draw")
  val nextPlayer = None
  val finished = true
  val winner = None
}

case class PlayingBoard(next: Player, positions: String) extends Board {
  assert(positions.matches("[ XO]{9}"), "Not a valid board " + positions)

  def move(p: Player, move: Int): Either[String, Board] = {
    val winningBoards = Set(
      "WWW      ", "   WWW   ", "      WWW",
      "W  W  W  ", " W  W  W ", "  W  W  W",
      "W   W   W", "  W W W  "
    )

    // TODO: write cleverer 'draw' detection
    def isDraw(positions: String): Boolean = !positions.contains(" ")

    def isWin(positions: String): Boolean =
      winningBoards.contains(positions.replace(p.toChar, 'W').replace(p.next.toChar, ' '))

    if (move < 1 || move > 9) return Left("Illegal position "+ move)
    if (positions(move - 1) != ' ') return Left("Position already taken!")
    if (p != next) return Left("Its not %s's turn" format p.toChar)

    val newPositions = positions.patch(move - 1, p.toString, 1)
    if (isDraw(newPositions))
      Right(DrawBoard(newPositions))
    else if (isWin(newPositions))
      Right(WinningBoard(p, newPositions))
    else
      Right(PlayingBoard(p.next, newPositions))
  }

  def nextPlayer = Some(next)
  val finished = false
  val winner = None
}

object EmptyBoard extends Board {
  def move(p: Player, move: Int): Either[String, Board] = {
    if (move < 1 || move > 9) return Left("Illegal position "+ move)
    if (p != X) return Left("X always starts first")
    Right(new PlayingBoard(O, positions.patch(move - 1, X.toString, 1)))
  }
  val nextPlayer = Some(X)
  val finished = false
  val winner = None
  val positions = " " * 9
}

abstract class Player {
  def toChar: Char
  def next: Player
  override def toString = toChar.toString
}
case object X extends Player {
  val toChar = 'X'
  val next = O
}
case object O extends Player {
  val toChar = 'O'
  val next = X
}

/**
 * Companion object for formatting positions strings.
 */
object Board {
  /**
   * Formats positions of a board for nice printing.
   */
  def formatBoard(positions: String): String =
    " " + positions.zip(
      Seq(" | ", " | ", " \n---+---+---\n ", " | ", " | ", " \n---+---+---\n ", " | ", " | ", " ")).flatMap(
      p => p._1+p._2).mkString

  /**
   * Formats positions of a board for nice printing, where empty spaces are replaces by their numeric position.
   */
  def printForInput(positions: String): String =
    positions.zipWithIndex.map(
      p => if (p._1 == ' ') "(" + (p._2 + 1) + ")" else " " + p._1 + " "
    ).zip(
      Seq("|","|","\n---+---+---\n","|","|","\n---+---+---\n","|","|","")
    ).flatMap(
      p => p._1 + p._2
    ).mkString
}
