package tables

import models.Artefacts.CategoryTable
import models.Intelligence.{ArtefactValidation, ArtefactValidationTable, IntelligenceTable}
import models.Interactions.{CommentTable, MessageTable, RecommendationTable}
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
  with CommentTable
  with ArtefactValidationTable
  with IntelligenceTable
  with CategoryTable
{

  val users = TableQuery[Users]
  val dbArtefacts = TableQuery[Artefacts]
  val interactions = TableQuery[Interactions]
  val messages = TableQuery[Messages]
  val recommendations = TableQuery[Recommendations]
  val groups = TableQuery[Groups]
  val userGroups = TableQuery[UserGroups]
  val comments = TableQuery[Comments]
  val artefactValidations = TableQuery[ArtefactValidations]
  val intelligences = TableQuery[Intelligences]
  val categories = TableQuery[Categories]

}