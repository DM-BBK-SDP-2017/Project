package tables

import java.sql.Timestamp
import javax.swing.text.html.HTML

import models.{Artefact, Blog, User}
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape.proveShapeOf



trait ArtefactTable {
  protected val driver: JdbcProfile
  import driver.api._
  class Artefacts(tag: Tag) extends Table[Artefact](tag, "ARTEFACTS") {

    def id = column[Int]("ID",O.PrimaryKey,O.AutoInc)
    def content = column[String]("CONTENT")
    def creator_id = column[Int]("CREATOR_ID")
    //def categories_id = column[Int]("CATEGORIES_ID")
    //def tags_id = column[String]("TAGS_ID")
    def created = column[Timestamp]("CREATED")


    def * = (id,content,creator_id, created) <> (Artefact.tupled, Artefact.unapply _)
   //def * = (id,content,creator_id, categories_id, tags_id, created) <> ( {tuple: (Int,String, Int, Int, Int, Timestamp) => Artefact(id,content,creator_id, categories_id, tags_id, created) },
   //  Artefact.unapply _)
  }
}

