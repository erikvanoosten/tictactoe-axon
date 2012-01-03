package cqrs.workshop.tictactoe.query

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import scala.collection.JavaConversions._
import org.springframework.stereotype.{Repository, Component}
import org.springframework.transaction.annotation.Transactional

/**
 * Query repository for {@link Game}s, JPA implementation.
 *
 */
@Component("gameQueryRepository")
@Repository
@Transactional(readOnly = true)
class JpaGameQueryRepository extends GameQueryRepository {

  @PersistenceContext
  private var entityManager: EntityManager = _

  def findGames(): Seq[Game] = {
    entityManager.createQuery("SELECT g FROM Game g").getResultList().asInstanceOf[java.util.List[Game]].toSeq
  }

}
