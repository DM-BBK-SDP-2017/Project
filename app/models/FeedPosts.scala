package models

import java.sql.Timestamp

import play.api.libs.json.Json

/**
  * Created by dannymadell on 20/07/2017.
  */
case class FeedPost
  ( id: Int,
    post_type:String,
    artefact_id: Int)

object FeedPost extends ((Int, String, Int) => FeedPost) {
  implicit val jsonReadWriteFormatTrait = Json.format[FeedPost]




}



