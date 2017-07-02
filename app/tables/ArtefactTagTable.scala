package tables

import java.sql.Timestamp

import models._
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape.proveShapeOf
/**
  * Created by dannymadell on 27/06/2017.
  */
trait ArtefactTagTable {

  protected val driver: JdbcProfile
  import driver.api._
  class ArtefactTags(tag: Tag) extends Table[ArtefactTag](tag, "ARTEFACT_TAGS") {

    def artefact_Tag_Id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def artefact_Tag = column[String]("CATEGORY_TAG")

    def creator_id = column[Int]("CREATOR_ID")

    def created = column[Timestamp]("CREATED_DATE")


    def * = (artefact_Tag_Id, artefact_Tag, creator_id, created) <> (ArtefactTag.tupled, ArtefactTag.unapply _)


  }
}
