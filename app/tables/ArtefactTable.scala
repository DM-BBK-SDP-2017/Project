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

    def id = column[String]("ID",O.PrimaryKey,O.AutoInc)
    def content = column[String]("CONTENT")
    def creator = column[String]("CREATOR")

    def * = (id,content,creator) <> (Artefact.tupled, Artefact.unapply _)
  }
}

