package models.Interactions

  import java.sql.Timestamp
  import java.text.SimpleDateFormat

  import models.Utils.TimeStampFormat
  import play.api.libs.json._

case class Interaction (
                 artefact_id:Int,
                 user_id:Int,
                 interaction_type: String,
                 interaction_timestamp: Timestamp)


object Interaction extends ((Int, Int, String,Timestamp) => Interaction) with TimeStampFormat {

 //def apply(artefact_id:Int,user_id:Int,interaction_type:String,interaction_timestamp: Timestamp):Interaction = {
 //  val newTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
 //  Interaction(artefact_id,user_id,interaction_type,newTimestamp)
 //}


 //def unpick(i: Interaction): Option[ (Int,Int,String,Timestamp)]  = {

 //  Some(i.artefact_id, i.user_id, i.interaction_type,i.interaction_timestamp)

 //}

  implicit val jsonReadWriteFormatTrait = Json.format[Interaction]


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
