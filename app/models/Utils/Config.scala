package models.Utils

import play.api.libs.json.Json
import slick.driver.JdbcProfile

/**
  * Created by dannymadell on 06/08/2017.
  */
case class CategoryChild(category_id: Int, child_id: Int)

object CategoryChild extends ((Int,Int) => CategoryChild)

trait CategoryChildTable{
  protected val driver: JdbcProfile
  import driver.api._
  class Configs(tag: Tag) extends Table[CategoryChild](tag, "CATEGORY_CHILDREN") {

    def category_id = column[Int]("CATEGORY_ID")
    def child_id = column[Int]("CHILD_ID")
    def * = (category_id,child_id) <> (CategoryChild.tupled, CategoryChild.unapply _)

  }
}