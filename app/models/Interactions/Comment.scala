package models.Interactions

import java.sql.Timestamp
import java.text.SimpleDateFormat

import models.Interactions.Recommendation
import models.Utils.TimeStampFormat
import play.api.libs.json._
import slick.driver.JdbcProfile

/**
  * Created by dannymadell on 20/07/2017.
  */
case class Comment (
                      comment_id: Int,
                      from_user: Int,
                      artefact_id: Int,
                      comment_content: String,
                      comment_timestamp: Timestamp

                      )

object Comment extends ((Int, Int, Int, String, Timestamp) => Comment) with TimeStampFormat {
  implicit val jsonReadWriteFormatTrait = Json.format[Comment]
}


trait CommentTable {
  protected val driver: JdbcProfile
  import driver.api._
  class Comments(tag: Tag) extends Table[Comment](tag, "COMMENTS") {

    def comment_id = column[Int]("COMMENT_ID",O.PrimaryKey,O.AutoInc)
    def from_user = column[Int]("FROM_USER")
    def artefact_id = column[Int]("ARTEFACT_ID")
    def comment_content = column[String]("COMMENT_CONTENT")
    def comment_timestamp = column[Timestamp]("COMMENT_TIMESTAMP")
    def * = (comment_id,from_user,artefact_id,comment_content,comment_timestamp) <> (Comment.tupled, Comment.unapply _)

  }
}

