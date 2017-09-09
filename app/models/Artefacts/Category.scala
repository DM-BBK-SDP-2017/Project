package models.Artefacts

import java.lang.Exception
import java.sql.Timestamp
import java.util.Calendar

import com.google.inject.Singleton
import play.api.Logger
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.json._
import play.api.mvc.Result
import tables.AllTables

import scala.collection.mutable
import scala.concurrent.Future
import scala.util.control.Exception
import scala.util.{Failure, Success, Try}
import scala.util.parsing.json.JSON

//import models.Artefacts.Category.categoryFormatter.{categories, db}
import models.Users.Group
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile
import tables.AllTables

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.functional.syntax._

/**
  * Created by dannymadell on 29/07/2017.
  */
case class Category
(
  category_id: Int,
  category_name: String,
  parent: Int // e.g. to query children get all categories with that parent
) {


  def hasChildren(): Boolean = {
    Category.innerObj.getChildren(this).nonEmpty
  }
}






object Category extends ((Int,String,Int) => Category) {

  //implicit val jsonReadWriteFormatTrait = Json.format[Category]



  object innerObj extends AllTables with HasDatabaseConfig[JdbcProfile] {
    // implicit val jsonReadWriteFormatTrait = Json.format[Category]

    val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

    import driver.api._

    val allCategoriesQuery = for {
      c <- categories.sortBy { x => x.category_id.asc }
    } yield (c)

    val categoryResults: Seq[Category] = Await.result(db.run(allCategoriesQuery.result), Duration.Inf)

    implicit object categoryFormatter extends Format[Category] {


      //val something = categoryResults.filter(x => x.parent == 1).map(y => Json.toJson(y))//.filter(_.parent === category.parent)


      def writes(category: Category): JsValue = {
        Json.obj(
          "id" -> JsNumber(category.category_id),
          //"children" -> JsArray(IndexedSeq(JsObject(Seq(categoryResults.filter(_.parent === category.parent)))))
          "children" -> JsArray(categoryResults.filter(x => x.parent == category.category_id).map(child => Json.toJson(child)(this))))

      }

      // def reads(json: JsValue): JsResult[Category] = {
      //   val str = json.as[String]
      //   JsSuccess(Category(
      //     (json \ "category_id").get),
      //     ((json \ "category_name").get.toString()),
      //     ((json \ "parent").get))
      // }

      // implicit val categoryReads: Reads[Category] = (
      //   (JsPath \ "category_id").read[Int] and
      //     (JsPath \ "category_name").read[String] and
      //     (JsPath \ "parent").readNullable[Int]
      //   )(Category.apply _)

      // UNUSED
      def reads(json: JsValue): JsResult[Category] = {

        val str = json.as[String]
        //JsSuccess(Category(0,(json \ "name").get.toString(),0,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())))

        JsSuccess(Category(0, "", 0))
        // //
        // implicit val catReads: Reads[Category] = (
        //   (JsPath \ "category_id").read[Int],
        //   (JsString("")),
        //   (JsPath \ "parent").read[Int]
        // )
        // json.validate[catReads]


      }


    }







    def getCategoryPathsForNewArtefact(): JsValue = {

      implicit object categoryFormatter2 extends Format[(Int,String)] {

        def writes(x: (Int, String)): JsValue = {
          Json.obj(
            "id" -> JsNumber(x._1),
            "name" -> JsString(x._2)

          )
        }

        def reads(json: JsValue): JsResult[(Int,String)] = {

          val str = json.as[String]
          //JsSuccess(Category(0,(json \ "name").get.toString(),0,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())))

          JsSuccess((0,""))

        }
      }

      //[{"id":0, "name":"Paris"}, {"id":1, "name":"New York"}]

      val allCategories = categoryResults.toList

      var cats = mutable.ListBuffer[(Int,String)]()

      def flatten(ls: Seq[Any]): Seq[Any] = ls flatMap {
        case i: Seq[_] => flatten(i)
        case e => Seq(e)
      }



      def showCat(recCat: Category, str: String): Unit = {



        val recCatName = recCat.category_name
        val returnStr: String = str match {
          case "" => s"$recCatName"
          case _ => s"$str > $recCatName"
        }

        //Logger.info(recCatName)
        //Logger.info(returnStr)


        recCat.hasChildren match {
          case true => {
            cats = cats += (recCat.category_id -> returnStr)
            for (catInvert <- Category.innerObj.getChildren(recCat)) {
              showCat(catInvert, returnStr)
            }
          }
          case false => {
            cats = cats += (recCat.category_id -> returnStr)
          }

        }
      }



       // flatten(recCat.hasChildren match {
       //   case true =>
////
       //     flatten(Seq(returnStr,
////
       //       flatten(for(catInvert <- Category.innerObj.getChildren(recCat)) yield {
       //           showCat(catInvert, returnStr)}                )))
////
////
////
       //     case false => Seq(returnStr)
////
       //   })


      val head: Category = allCategories.head
      //Logger.info(showCat(head, head.category_name).toString())

      val arr = showCat(head, "")

//
      Logger.info(cats.size.toString)
      for (c <- cats) {Logger.info("HERE22" + c.toString())}
//
//

      Logger.info(Json.toJson(cats.toMap).toString())
     Json.toJson(cats.toMap)


    }

    def getChildren(category: Category): Seq[Category] = categoryResults.filter(_.parent == category.category_id)

    /*def updateCategories(json: JsValue): Future[Result] = {



        val children = json \\ "id"


        //]}]}5:"di"{,}4:"di"{[:"nerdlihc",3:"di"{,}2:"di"{,}1:"di"{[

        val pattern = "{[:\"nerdlihc\",(/d+)".r

        // val pattern(a, b) = pattern

        val str = json.toString()


        for (c <- children) {

          val id = c.as[Int]
          val parentId: Int = pattern.findFirstIn(str.slice(0, str.indexOf("\"id\":" + id)).reverse).get.drop(14).reverse.toInt
          val q = for {cat <- categories if cat.category_id === id} yield cat.parent
          val updateAction = q.update(parentId)


        }

        Ok("Categories updated")







     // implicit val reader: Reads[Category] = (
     //   (JsPath \ "id").read[Category] and
     //   (JsPath \ "children").readNullable[Seq[Category]]
     // )(Category.apply _)



    }

    */
  }






}

trait CategoryTable{
  protected val driver: JdbcProfile
  import driver.api._
  class Categories(tag: Tag) extends Table[Category](tag, "CATEGORIES") {

    def category_id = column[Int]("CATEGORY_ID",O.PrimaryKey,O.AutoInc)
    def category_name = column[String]("CATEGORY_NAME")
    def parent = column[Int]("PARENT")
    def * = (category_id,category_name,parent) <> (Category.tupled, Category.unapply _)

  }
}





/*
case class CategoryInverted
(
  category: Category,
  children: Seq[Category]
)
object CategoryInverted {

  //val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  //import driver.api._

  val allCategoriesQuery = for {
    c <- categories.sortBy { x => x.category_id }
  } yield (c)

  val categoryResults: Seq[Category] = Await.result(db.run(allCategoriesQuery.result), Duration.Inf)

  def getChildren(category: Category): Seq[Category] =

    categoryResults.filter(_.parent == category.category_id)
  //for (c <- categories) yield {
  //  CategoryInverted(c, categories.filter(_.parent == c.category_id))
  //}

}
*/
// [{"id":1},{"id":2,"children":[{"id":3},{"id":4},{"id":5,"children":[{"id":6},{"id":7},{"id":8}]},{"id":9},{"id":10}]},{"id":11},{"id":12}]