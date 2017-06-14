package models

import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.swing.text.html.HTML
import play.api.libs.json._
import slick.driver.JdbcProfile

case class Artefact(id:String, content:String, creator: String)


object Artefact extends ((String, String, String) => Artefact) {

  //implicit object HTMLFormat extends Format[String] {
  //  val format = new HTML()
  //  val printFormat = new HTML()
//
  //  def reads(json: JsValue) = {
  //    val str = json.as[String]
  //    JsSuccess(StringBuilder.newBuilder.append(json).toString)
  //  }
//
  //  def writes() = JsString(printFormat.format())
  //}

  implicit val jsonReadWriteFormatTrait = Json.format[Artefact]
}

