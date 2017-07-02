package models

import java.sql.Timestamp
import java.text.SimpleDateFormat

import play.api.libs.json._

/**
  * Created by dannymadell on 27/06/2017.
  */
case class ArtefactTag(artefact_Tag_Id: Int,
   artefact_Tag: String,
   creator_id: Int,
   created: Timestamp)



  object ArtefactTag extends ((Int, String, Int, Timestamp) => ArtefactTag) {

    implicit object timestampFormat extends Format[Timestamp] {
      val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
      val printFormat = new SimpleDateFormat("HH:mm:ss")

      def reads(json: JsValue) = {
        val str = json.as[String]
        JsSuccess(new Timestamp(format.parse(str).getTime))
      }

      def writes(ts: Timestamp) = JsString(printFormat.format(ts))
    }

    implicit val jsonReadWriteFormatTrait = Json.format[ArtefactTag]
  }




