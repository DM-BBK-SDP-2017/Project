package tables

import java.sql.Timestamp

import models.Blog
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape.proveShapeOf



trait BlogTable {
  protected val driver: JdbcProfile
  import driver.api._
  class Blogs(tag: Tag) extends Table[Blog](tag, "BLOGS") {
    
    def id = column[Int]("ID",O.PrimaryKey,O.AutoInc)
    def users_id = column[Int]("USERS_ID")
    def when = column[Timestamp]("WHEN")
    def what = column[String]("WHAT")

    def * = (id,users_id,when,what) <> (Blog.tupled, Blog.unapply _)
  }
}

