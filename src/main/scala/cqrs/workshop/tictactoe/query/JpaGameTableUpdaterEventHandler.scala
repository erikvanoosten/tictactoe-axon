package cqrs.workshop.tictactoe.query

import org.axonframework.eventhandling.annotation.EventHandler
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import cqrs.workshop.tictactoe.api.Events._
import org.springframework.stereotype.Component

/**
 * Flattens events to a JPA entity and persists it to a simple table.
 *
 */
@Component
class JpaGameTableUpdaterEventHandler {

  @PersistenceContext
  private var entityManager: EntityManager = _

  @EventHandler
  def handleGameCreated(event: GameCreatedEvent) {
    val game = new Game(event.gameId, event.playerX, event.playerO, event.positions, null)
    entityManager.persist(game)
  }

  @EventHandler
  def handleGameMove(event: GameMoveEvent) {
    val game = entityManager.find(classOf[Game], event.gameId)
    game.positions = event.positions
    game.winner = event.winner
    entityManager.persist(game)
  }

}
