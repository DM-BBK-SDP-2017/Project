package controllers

import java.util.Date

import scala.concurrent.Future
import models._
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import slick.driver.JdbcProfile
import tables._
import java.sql.Timestamp
import java.util.Calendar

import models.Blog.timestampFormat
import play.api.cache._
import javax.inject.Inject

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import play.Logger
import play.twirl.api.Html

class Api @Inject() (cache: CacheApi)

  extends Controller
  with BlogTable
  with ArtefactTable
  with ArtefactTagTable
  with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  ///  ____  __     __    ___  ____
  /// (  _ \(  )   /  \  / __)/ ___)
  ///  ) _ (/ (_/\(  O )( (_ \\___ \
  /// (____/\____/ \__/  \___/(____/
  ///

  val blogs = TableQuery[Blogs]

  def getBlogs() = SecuredAction.async { implicit request =>

    val myblogs = for {
      c <- blogs.sortBy { x => x.when.desc } if c.users_id === request.user.id
    } yield (c)
    db.run(myblogs.result).map { res => {
      Ok(Json.toJson(res))
    }
    }
  }

  def postBlog = SecuredAction.async(parse.json) { implicit request =>

    val blog = request.body.as[Blog]

    val b = Blog(
      0,
      request.user.id,
      new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()),
      blog.what)

    db.run((blogs += b).asTry).map(res =>
      res match {
        case Success(res) => Ok(Json.toJson(b))
        case Failure(e) => {
          Logger.info(request.user.id.toString)
          Logger.error(s"Problem on insert, ${e.getMessage}")
          InternalServerError(s"Problem on insert, ${e.getMessage}")
        }
      }
    )
    //Future.successful(Ok("added"))
  }

  ///   __   ____  ____  ____  ____  __    ___  ____  ____
  ///  / _\ (  _ \(_  _)(  __)(  __)/ _\  / __)(_  _)/ ___)
  /// /    \ )   /  )(   ) _)  ) _)/    \( (__   )(  \___ \
  /// \_/\_/(__\_) (__) (____)(__) \_/\_/ \___) (__) (____/


  val artefacts = TableQuery[Artefacts]

  def getArtefacts() = SecuredAction.async { implicit request =>

    val getartefacts = for {
      c <- artefacts.sortBy { x => x.id.desc }
    } yield (c)

    db.run(getartefacts.result).map { res => {
      Ok(Json.toJson(res))
    }
    }
  }

  def postArtefact = SecuredAction.async(parse.json) { implicit request =>

    val artefact = request.body.as[Artefact]

    val b = Artefact(
      id = 0,
      content = artefact.content,
      creator = request.user.id,
      //categories_id = artefact.categories_id,
      //tags_id = artefact.tags_id,
      created = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))

    db.run((artefacts += b).asTry).map(res =>
      res match {
        case Success(res) => Ok(Json.toJson(b))
        case Failure(e) => {
          Logger.error(s"Problem on insert, ${e.getMessage}")
          InternalServerError(s"Problem on insert, ${e.getMessage}")
        }
      }
    )
    //Future.successful(Ok("added"))
  }


  // ARTEFACT_TAGS

  val artefact_tags = TableQuery[ArtefactTags]

  def getArtefactTags() = SecuredAction.async { implicit request =>

    val getartefacttags = for {
      c <- artefact_tags.sortBy { x => x.artefact_Tag_Id.desc }
    } yield (c)

    Logger.info(getartefacttags.toString)

    db.run(getartefacttags.result).map { res => {
      Ok(Json.toJson(res))
    }
    }
  }

  def getArtefactTagsForFeed() = SecuredAction.async { implicit request =>


    val getartefacttags = for {
      c <- artefact_tags.sortBy { x => x.artefact_Tag_Id.desc }
    } yield (c)


    //Json.arr() :+(getartefacttags)
    db.run(getartefacttags.result).map { res => {
      Ok(Json.arr() :+ (Json.toJson(res)))
    }
    }


  }

  def postArtefactTag = SecuredAction.async(parse.json) { implicit request =>

    val artefactTag = request.body.as[ArtefactTag]

    //   artefact_Tag_Id: Int,
    //   artefact_Tag: String,
    //   creator_id: Int,
    //   created: Timestamp)


    val b = ArtefactTag(
      artefact_Tag_Id = 0,
      artefact_Tag = artefactTag.artefact_Tag,
      creator_id = request.user.id,
      created = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))


      db.run((artefact_tags += b).asTry).map(res =>
        res match {
          case Success(res) => Ok(Json.toJson(b))
          case Failure(e) => {
            Logger.error(s"Problem on insert, ${e.getMessage}")
            InternalServerError(s"Problem on insert, ${e.getMessage}")
          }
        }
      )
      //Future.successful(Ok("added"))
    }

}