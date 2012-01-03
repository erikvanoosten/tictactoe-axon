package cqrs.workshop.tictactoe

import api.Commands._
import api.Events._
import command._
import java.lang.String
import org.junit._

import org.axonframework.test.FixtureConfiguration
import org.axonframework.test.Fixtures

class GameCommandHandlerTest {
  private var fixture: FixtureConfiguration = _

  @Before
  def setUp: Unit = {
    fixture = Fixtures.newGivenWhenThenFixture
    val commandHandler: GameCommandHandler = new GameCommandHandler
    commandHandler.setGameRepository(fixture.createGenericRepository(classOf[Game]))
    fixture.registerAnnotatedCommandHandler(commandHandler)
  }

  @Test
  def testGameStarted: Unit = {
    val gameId: String = fixture.getAggregateIdentifier.asString
    val abel = "Abel"
    val bob = "Bob"
    fixture.
      given().
      when(StartGameCommand(gameId, abel, bob)).
      expectEvents(GameCreatedEvent(gameId, abel, bob, "         "))
  }

  @Test
  def testMove: Unit = {
    val gameId: String = fixture.getAggregateIdentifier.asString
    val abel = "Abel"
    val bob = "Bob"
    fixture.
      given(GameCreatedEvent(gameId, abel, bob, "         ")).
      when(MoveCommand(gameId, abel, 5)).
      expectEvents(GameMoveEvent(gameId, abel, 5, "    X    ", null))
  }

  @Test
  def testWin: Unit = {
    val gameId: String = fixture.getAggregateIdentifier.asString
    val abel = "Abel"
    val bob = "Bob"
    fixture.
      given(
          GameCreatedEvent(gameId, abel, bob, "         "),
          GameMoveEvent(gameId, abel, 5, "    X    ", null),
          GameMoveEvent(gameId,  bob, 1, "O   X    ", null),
          GameMoveEvent(gameId, abel, 2, "OX  X    ", null),
          GameMoveEvent(gameId,  bob, 4, "OX OX    ", null)
      ).
      when(MoveCommand(gameId, abel, 8)).
      expectEvents(GameMoveEvent(gameId, abel, 8, "OX OX  X ", abel))
  }

}
