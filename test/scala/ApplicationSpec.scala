import org.apache.commons.codec.digest.DigestUtils
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.test.Helpers._
import play.api.test._
import play.test.{WithApplication, WithServer}
import play.api.libs.ws._
import play.api.test._

class ApplicationIntegrationSpec extends PlaySpecification {

  "Application" should {
    "be reachable" in new WithServer {
      val response = await(WS.url("http://localhost:" + port).get()) //1

      response.status must equalTo(OK) //2
      response.body must contain("Semaphore Community Library") //3
    }
  }
}