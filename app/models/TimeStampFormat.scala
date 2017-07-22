package models

import java.sql.Timestamp
import java.text.SimpleDateFormat

import play.api.libs.json.{Format, JsString, JsSuccess, JsValue}

/**
  * Created by dannymadell on 20/07/2017.
  */
trait TimeStampFormat {

  implicit object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val printFormat = new SimpleDateFormat("HH:mm:ss")

    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(new Timestamp(format.parse(str).getTime))
    }

    def writes(ts: Timestamp) = JsString(printFormat.format(ts))

  }

}