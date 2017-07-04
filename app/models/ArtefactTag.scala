package models

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar

import play.Logger
import play.api.libs.json._

/**
  * Created by dannymadell on 27/06/2017.
  */
case class ArtefactTag(artefact_Tag_Id: Int,
   artefact_Tag: String,
   creator_id: Int,
   created: Timestamp)

//1971-01-01T00:00:00.000

  object ArtefactTag extends ((Int, String, Int, Timestamp) => ArtefactTag) {

    implicit object timestampFormat extends Format[Timestamp] {
      val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
      val printFormat = new SimpleDateFormat("HH:mm:ss")
      val regex = """\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d"""

      def reads(json: JsValue) = {
        val str = json.as[String]
        Logger.info(s"Value $str ArtefactTag date format")
        Thread.sleep(2000)
        JsSuccess(new Timestamp(format.parse(str).getTime))
       /*
         if ((str.matches(regex))) {
           Logger.info(s"Value $str incorrect date format")
           JsSuccess(new Timestamp(format.parse(str).getTime))

        } else {
           Logger.info(s"Value $str incorrect date format")
           JsSuccess(new Timestamp(format.parse(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString).getTime))
         }
         */

      }

      def writes(ts: Timestamp) = JsString(printFormat.format(ts))
    }

    implicit val jsonReadWriteFormatTrait = Json.format[ArtefactTag]
  }




