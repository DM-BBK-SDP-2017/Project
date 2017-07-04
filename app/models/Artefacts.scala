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
                    created: Timestamp)

object Artefact extends ((Int, String, Int, String, Timestamp) => Artefact) {

  implicit object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val printFormat = new SimpleDateFormat("HH:mm:ss")

    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(new Timestamp(format.parse(str).getTime))
    }

    def writes(ts: Timestamp) = JsString(printFormat.format(ts))
  }

  implicit val jsonReadWriteFormatTrait = Json.format[Artefact]
}



