package models.Interactions

import java.sql.Timestamp

import models.Utils.TimeStampFormat
import play.api.libs.json.Json
import slick.driver.JdbcProfile

/**
  * Created by dannymadell on 20/07/2017.
  */
case class Recommendation
(id: Int,
 artefact_id: Int,
recommended_by: Int,
recommended_for: Int,
 recommendation_score: Double,
 recommended_timestamp: Timestamp)



object Recommendation extends ((Int, Int, Int, Int, Double, Timestamp) => Recommendation) with TimeStampFormat {
  implicit val jsonReadWriteFormatTrait = Json.format[Recommendation]

}


trait RecommendationTable {
  protected val driver: JdbcProfile
  import driver.api._
  class Recommendations(tag: Tag) extends Table[Recommendation](tag, "RECOMMENDATIONS") {

    def id = column[Int]("ID",O.PrimaryKey,O.AutoInc)
    def artefact_id = column[Int]("ARTEFACT_ID")
    def recommended_by = column[Int]("RECOMMENDED_BY")
    def recommended_for = column[Int]("RECOMMENDED_FOR")
    def recommendation_score = column[Double]("RECOMMENDATION_SCORE")
    def recommended_timestamp = column[Timestamp]("RECOMMENDED_TIMESTAMP")


    def * = (id,artefact_id,recommended_by,recommended_for,recommendation_score,recommended_timestamp) <> (Recommendation.tupled, Recommendation.unapply _)

  }
}

