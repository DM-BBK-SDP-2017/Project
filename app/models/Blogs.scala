package models

import java.sql.Timestamp

import org.joda.time.DateTime
import play.api._
import play.api.Play.current
import play.api.libs.functional.syntax._
import play.api.libs.json._
import java.text.SimpleDateFormat

import play.api.cache._
import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.Controller
import slick.driver.JdbcProfile
import slick.lifted.{Query, TableQuery}
import tables.BlogTable

import scala.concurrent.Future
import scala.concurrent.duration.Duration

case class Blog(
                 id:Int,
                 users_id:Int,
                 when: Timestamp,
                 what: String)


object Blog extends ((Int, Int, Timestamp,String) => Blog) {


  implicit object timestampFormat extends Format[Timestamp] {
  val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
  val printFormat = new SimpleDateFormat("HH:mm:ss")
  
  def reads(json: JsValue) = {
    val str = json.as[String]
    JsSuccess(new Timestamp(format.parse(str).getTime))
  }
  
  def writes(ts: Timestamp) = JsString(printFormat.format(ts))
}

  implicit val jsonReadWriteFormatTrait = Json.format[Blog]
}

/*

@Singleton()
class BlogRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends Controller with BlogTable with HasDatabaseConfigProvider[JdbcProfile] {

  //val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val q = TableQuery[Blogs]
//
//
//
  //val aList = q.sortBy(_.id)
  //val blogs = db.run(q.result)//.result(Duration(5, "seconds"))
  // blogs.

  import driver.api._


  def getAll(): Future[List[Blog]] = db.run {
    q.to[List].result
  }

  val get = db.run(q.to[List].result)
 // val blogs = TableQuery[Blogs]
//
 // val myblogs = for {
 //   c <- blogs.sortBy { x => x.when }
 // } yield (c)
 // db.run(myblogs.result)


}
*/