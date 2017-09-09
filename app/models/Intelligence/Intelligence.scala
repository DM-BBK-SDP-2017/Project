package models.Intelligence

import java.sql.Timestamp

import models.TimeStampFormat
import play.api.libs.json.Json
import slick.driver.JdbcProfile

/**
  * Created by dannymadell on 23/07/2017.
  */
case class Intelligence // determined by authoring an artefact, being validated or recommended as knowing
(
intel_id: Int,
identified_by: Int,
identifier_how: String, // e.g. request etc.
user_id: Int, // user with knowledge
knows_about_category: Int,
knowledge_strength: Double,// category
identified_timestamp: Timestamp

)

object Intelligence extends ((Int,Int,String,Int,Int,Double,Timestamp) => Intelligence) with TimeStampFormat{
  implicit val jsonReadWriteFormatTrait = Json.format[Intelligence]

}

trait IntelligenceTable{
  protected val driver: JdbcProfile
  import driver.api._
  class Intelligences(tag: Tag) extends Table[Intelligence](tag, "INTELLIGENCES") {

    def intelligence_id= column[Int]("INTELLIGENCE_ID",O.PrimaryKey,O.AutoInc)
    def identified_by= column[Int]("IDENTIFIED_BY")
    def identifier_how= column[String]("IDENTIFIED_HOW")
    def user_id= column[Int]("USER_ID")
    def knows_about_category= column[Int]("KNOWS_ABOUT_CATEGORY")
    def knowledge_strength= column[Double]("KNOWLEDGE_STRENGTH")
    def identified_timestamp = column[Timestamp]("IDENTIFIED_TIMESTAMP")
    def * = (intelligence_id,identified_by,identifier_how,user_id,knows_about_category,knowledge_strength,identified_timestamp) <> (Intelligence.tupled, Intelligence.unapply _)

  }
}
