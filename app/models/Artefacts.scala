package models

import java.sql.Timestamp

import models.Utils.TimeStampFormat
import play.api.libs.json._

case class Artefact(id: Int,
                    content:String,
                    creator: Int,
                    tags_ids_string: String,
                    category_id: Int,
                    created: Timestamp)

object Artefact extends ((Int, String, Int, String, Int,Timestamp) => Artefact) with TimeStampFormat {

  implicit val jsonReadWriteFormatTrait = Json.format[Artefact]
}



