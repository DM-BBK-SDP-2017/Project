package models.Intelligence.answerToQuestion

import java.sql.Timestamp

import models.Utils.TimeStampFormat
import play.api.libs.json.Json
import slick.driver.JdbcProfile

/**
  * Created by dannymadell on 11/09/2017.
  */
case class answerToQuestionInteraction
(
  request_id: Int,
  asked_to_user: Int,
  interaction_timestamp: Timestamp,
  interaction_type: String // IGNORED,ANSWERED,LATER,CAN'T HELP,ASK PERSON X

)

object answerToQuestionInteraction extends ((Int,Int,Timestamp,String) => answerToQuestionInteraction) with TimeStampFormat {
  implicit val jsonReadWriteFormatTrait = Json.format[answerToQuestionInteraction]

}

trait answerToQuestionInteractionTable {
  protected val driver: JdbcProfile
  import driver.api._
  class answerToQuestionInteractions(tag: Tag) extends Table[answerToQuestionInteraction](tag, "ANSWER_TO_QUESTION_INTERACTIONS") {

   def request_id = column[Int]("REQUEST_ID")
   def asked_to_user = column[Int]("ASKED_TO_USER")
   def interaction_timestamp = column[Timestamp]("INTERACTION_TIMESTAMP")
   def interaction_type = column[String]("INTERACTION")

    def * = (request_id, asked_to_user,interaction_timestamp,interaction_type) <> (answerToQuestionInteraction.tupled,answerToQuestionInteraction.unapply)

  }

  }
