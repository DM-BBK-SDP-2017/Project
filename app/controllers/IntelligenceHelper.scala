package controllers

import java.time.Duration
import java.util.Calendar

import models.Artefact
import models.Intelligence.Intelligence
import play.Logger
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import tables.AllTables

import scala.util.{Failure, Success}

/**
  * Created by dannymadell on 23/07/2017.
  */
object IntelligenceHelper extends AllTables with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._



  // who knows what?
  // validation
  //


  def generateArtefactValidation(artefact: Artefact) = {
    // find three most knowledgeable people without too many validations in their queie

    //val user_validation = artefactValidations.filter(_.artefact_id == artefact.id)
  }

  def addIntelligence(artefact: Artefact, weighting: Double) = {

   val newIntel = Intelligence(
        0,        // intel_id
        -1,       // identified_by
        "CREATED_ARTEFACT",       // identifier_how
        artefact.creator,       // user_id
      artefact.category_id,       // knows_about_category
        weighting,        // knowledge_strength
        new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())        // identified_timestamp
    )

    db.run((intelligences += newIntel).asTry).map(
      res =>
        res match {
          case Success(x) => Logger.info(s"Writing intelligence $x successful");
          case Failure(x) => Logger.info(s"Writing intelligence $x failed");
        }


    )
  }

}


//intel_id: Int,
//identified_by: Int,
//identifier_how: String, // e.g. request etc.
//user_id: Int, // user with knowledge
//knows_about_category: Int,
//knowledge_strength: Double,// category
//identified_timestamp: Timestamp