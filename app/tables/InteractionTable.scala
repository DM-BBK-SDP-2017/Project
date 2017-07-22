package tables

import java.sql.Timestamp

import models.Interactions.Interaction
import models._
import tables._
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape.proveShapeOf

trait InteractionTable
  extends ArtefactTable
  with UserTable {

  protected val driver: JdbcProfile

  import driver.api._

  class Interactions(tag: Tag) extends Table[Interaction](tag, "INTERACTIONS") {


    val artefacts = TableQuery[Artefacts]
    val users = TableQuery[Users]

    def artefact_id = column[Int]("ARTEFACT_ID")

    def user_id = column[Int]("USER_ID")

    def interaction_type = column[String]("INTERACTION_TYPE")

    def interaction_timestamp = column[Timestamp]("INTERACTION_TIMESTAMP")

    def * = (artefact_id, user_id, interaction_type, interaction_timestamp) <> (Interaction.tupled, Interaction.unapply _)
  }


}

