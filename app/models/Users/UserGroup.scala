package models.Users

import java.sql.Timestamp

import models.TimeStampFormat
import play.api.libs.json.Json
import slick.driver.JdbcProfile

/**
  * Created by dannymadell on 21/07/2017.
  */
case class UserGroup (

                    id: Int,
                    group_id: String,
                    joined_timestamp: Timestamp,
                    added_by_user: Int

                   )

object UserGroup extends ((Int,String,Timestamp,Int) => UserGroup) with TimeStampFormat {
  implicit val jsonReadWriteFormatTrait = Json.format[UserGroup]
}

trait UserGroupTable {
  protected val driver: JdbcProfile
  import driver.api._
  class UserGroups(tag: Tag) extends Table[UserGroup](tag, "USER_GROUPS") {

    def id = column[Int]("USER_ID")
    def group_id = column[String]("GROUP_ID")
    def joined_timestamp = column[Timestamp]("JOINED_TIMESTAMP")
    def added_by_user = column[Int]("ADDED_BY_USER")


    def * = (id,group_id,joined_timestamp,added_by_user) <> (UserGroup.tupled, UserGroup.unapply _)

  }
}

