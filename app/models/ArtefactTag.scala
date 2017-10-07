package models

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar

import models.Utils.TimeStampFormat
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

  object ArtefactTag extends ((Int, String, Int, Timestamp) => ArtefactTag) with TimeStampFormat {

    implicit val jsonReadWriteFormatTrait = Json.format[ArtefactTag]
  }




