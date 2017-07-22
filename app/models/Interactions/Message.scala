package models.Interactions

import java.sql.Timestamp
import java.text.SimpleDateFormat

import models.Interactions.Recommendation
import models.TimeStampFormat
import play.api.libs.json._
import slick.driver.JdbcProfile

/**
  * Created by dannymadell on 20/07/2017.
  */
case class Message (
                      message_id: Int,
                      message_type: String,
                      from_user: Int,
                      to_user: Int,
                      artefact_id: Int,
                      message: String,
                      timestamp: Timestamp

                      )

object Message extends ((Int, String, Int, Int, Int, String, Timestamp) => Message) with TimeStampFormat {
  implicit val jsonReadWriteFormatTrait = Json.format[Message]
}


trait MessageTable {
  protected val driver: JdbcProfile
  import driver.api._
  class Messages(tag: Tag) extends Table[Message](tag, "MESSAGES") {

    def message_id = column[Int]("MESSAGE_ID",O.PrimaryKey,O.AutoInc)
    def message_type = column[String]("MESSAGE_TYPE")
    def from_user = column[Int]("FROM_USER")
    def to_user = column[Int]("TO_USER")
    def artefact_id = column[Int]("ARTEFACT_ID")
    def message = column[String]("MESSAGE")
    def timestamp = column[Timestamp]("TIMESTAMP")


     def * = (message_id,message_type,from_user,to_user,artefact_id,message,timestamp) <> (Message.tupled, Message.unapply _)

  }
}

