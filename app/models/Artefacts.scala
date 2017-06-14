package models

import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.swing.text.html.HTML
import play.api.libs.json._
import slick.driver.JdbcProfile

case class Artefact(id: Int,
                    content:String,
                    creator: String,
                    categories_id: Int,
                    tags_id: Int,
                    created: Timestamp)


object Artefact extends ((Int, String, String, Int, Int, Timestamp) => Artefact) {

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

