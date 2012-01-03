package cqrs.workshop.tictactoe

import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

import org.junit.Assert.assertNotNull
import org.junit.Test

class SpringApplicationContextTest {
  @Test
  def testSpringContextStarts: Unit = {
    val appCtx: ApplicationContext = new ClassPathXmlApplicationContext(
      "META-INF/spring/application-context.xml",
      "META-INF/spring/database-context.xml"
    )
    assertNotNull(appCtx.getBean("gameRepository"))
  }
}
