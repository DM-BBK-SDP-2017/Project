package models.Users

import java.sql.Timestamp
import java.util.Calendar

import models.TimeStampFormat
import play.api.Logger
import play.api.libs.json._
import slick.driver.JdbcProfile

/**
  * Created by dannymadell on 21/07/2017.
  */
case class Group (

                   id: Int,
                     group_name: String,
                     created_by: Int,
                     created_timestamp: Timestamp

                   )

object Group extends ((Int, String, Int, Timestamp) => Group) with TimeStampFormat {

  //implicit val jsonReadWriteFormatTrait = Json.format[Group]


    implicit object groupFormatter extends Format[Group] {

      def writes(group: Group): JsValue = {
        val groupSeq = Seq(
          "id" -> JsNumber(group.id),
          "name" -> JsString(group.group_name)
        )
        JsObject(groupSeq)
      }

      def reads(json: JsValue): JsResult[Group] = {


        Logger.info(json.toString())

        //val str = json.as[Group]
        JsSuccess(Group(0,(json \ "group_name").get.toString(),0,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())))
      }



  }


}

trait GroupTable {
  protected val driver: JdbcProfile
  import driver.api._
  class Groups(tag: Tag) extends Table[Group](tag, "GROUPS") {

    def id = column[Int]("ID",O.PrimaryKey,O.AutoInc)
    def group_name = column[String]("GROUP_NAME")
    def created_by = column[Int]("CREATED_BY")
    def created_timestamp = column[Timestamp]("CREATED_TIMESTAMP")


    def * = (id,group_name,created_by,created_timestamp) <> (Group.tupled, Group.unapply _)

  }
}

