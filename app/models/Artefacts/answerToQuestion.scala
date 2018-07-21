package models.Intelligence.answerToQuestion

import java.sql.Timestamp

import models.Utils.TimeStampFormat
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.libs.json.Json
import slick.driver.JdbcProfile

/**
  * Created by dannymadell on 29/07/2017.
  */
case class answerToQuestion // answering a request for an artefact
(
  request_id: Int,
  from_user: Int,

  question: String,
  category: Int,

  status: String // pending, on hold, answered, ignored, rejected

)

object answerToQuestion extends ((Int,Int,String,Int,String) => answerToQuestion) {
  implicit val jsonReadWriteFormatTrait = Json.format[answerToQuestion]



}

trait answerToQuestionTable {
  protected val driver: JdbcProfile
  import driver.api._
  class answerToQuestions(tag: Tag) extends Table[answerToQuestion](tag, "ANSWER_TO_QUESTIONS") {

    def request_id = column[Int]("REQUEST_ID",O.PrimaryKey,O.AutoInc)
    def from_user = column[Int]("FROM_USER")
    def question = column[String]("QUESTION")
    def category = column[Int]("CATEGORY")
    def status = column[String]("STATUS")

    def * = (request_id,from_user,question,category,status) <> (answerToQuestion.tupled,answerToQuestion.unapply)
  }
}