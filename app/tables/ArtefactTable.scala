package tables

import java.sql.Timestamp
import javax.swing.text.html.HTML

import models.{Artefact, Blog}
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape.proveShapeOf
import slick.lifted.TableQuery

trait ArtefactTable {
  protected val driver: JdbcProfile
    import driver.api._
  class Artefacts(tag: Tag) extends Table[Artefact](tag, "ARTEFACTS") {

    def id = column[Int]("ID",O.PrimaryKey,O.AutoInc)
    def content = column[String]("CONTENT")
    def creator_id = column[Int]("CREATOR_ID")
    def category_id = column[Int]("CATEGORIES_ID")
    def tags_ids_string = column[String]("TAGS_ID")
    def created = column[Timestamp]("CREATED")


    def * = (id,content,creator_id, tags_ids_string, category_id, created) <> (Artefact.tupled, Artefact.unapply _)
   //def * = (id,content,creator_id, categories_id, tags_id, created) <> ( {tuple: (Int,String, Int, Int, Int, Timestamp) => Artefact(id,content,creator_id, categories_id, tags_id, created) },
   //  Artefact.unapply _)
  }
}

