package models

import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.swing.text.html.HTML

import play.api.libs.json._
import slick.driver.JdbcProfile
import slick.lifted.TableQuery

case class Artefact(id: Int,
                    content:String,
                    creator: Int,
                    tags_ids_string: String,
                    category_id: Int,
                    created: Timestamp)

object Artefact extends ((Int, String, Int, String, Int,Timestamp) => Artefact) with TimeStampFormat {

  implicit val jsonReadWriteFormatTrait = Json.format[Artefact]
}



