package models.Intelligence

import java.sql.Timestamp

import models.TimeStampFormat
import play.api.libs.json.Json
import slick.driver.JdbcProfile

/**
  * Created by dannymadell on 23/07/2017.
  */
case class ArtefactValidation
(
  validation_id: Int,
  artefact_id: Int,
  user_validating: Int,
  status: String, // pending, on hold, answered, ignored
  last_in_feed_timestamp: Timestamp,
  number_of_times_in_feed: Int // if ignore 3 times remove
)

object ArtefactValidation extends ((Int, Int, Int,String, Timestamp, Int) => ArtefactValidation) with TimeStampFormat {

  implicit val jsonReadWriteFormatTrait = Json.format[ArtefactValidation]

}

trait ArtefactValidationTable {
  protected val driver: JdbcProfile
  import driver.api._
  class ArtefactValidations(tag: Tag) extends Table[ArtefactValidation](tag, "ARTEFACT_VALIDATIONS") {

    def validation_id = column[Int]("VALIDATION_ID",O.PrimaryKey,O.AutoInc)
    def artefact_id = column[Int]("ARTEFACT_ID")
    def user_validating = column[Int]("USER_VALIDATING")
    def status = column[String]("STATUS")
    def last_in_feed_timestamp = column[Timestamp]("LAST_IN_FEED_TIMESTAMP")
    def number_of_times_in_feed = column[Int]("NUMBER_OF_TIMES_IN_FEED")
    def * = (validation_id,artefact_id,user_validating,status,last_in_feed_timestamp,number_of_times_in_feed) <> (ArtefactValidation.tupled, ArtefactValidation.unapply _)

  }
}


