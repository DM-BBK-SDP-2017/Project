package tables

import models.Interactions.{MessageTable, RecommendationTable}
import models.Users.{GroupTable, UserGroupTable}
import slick.lifted.TableQuery
import models.Users._

/**
  * Created by dannymadell on 20/07/2017.
  */
trait AllTables

extends BlogTable
with UserTable
with GroupTable
with UserGroupTable
with ArtefactTable
with ArtefactTagTable
with InteractionTable
with MessageTable
with RecommendationTable
{

  val users = TableQuery[Users]
  val dbArtefacts = TableQuery[Artefacts]
  val interactions = TableQuery[Interactions]
  val messages = TableQuery[Messages]
  val recommendations = TableQuery[Recommendations]
  val groups = TableQuery[Groups]
  val userGroups = TableQuery[UserGroups]

}