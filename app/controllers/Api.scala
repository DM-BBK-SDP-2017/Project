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
import javax.swing.text.html.HTML


import models.User

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import play.Logger
import play.twirl.api.Html

class Api @Inject() (cache: CacheApi) extends Controller with BlogTable with ArtefactTable with HasDatabaseConfig[JdbcProfile] {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  val blogs = TableQuery[Blogs]

  def getBlogs() = SecuredAction.async { implicit request =>

    val myblogs = for {
      c <- blogs.sortBy { x => x.when.desc } if c.user === request.user.nickname
    } yield (c)

    db.run(myblogs.result).map { res =>
      {
        Ok(Json.toJson(res))
      }
    }
  }

  def postBlog = SecuredAction.async(parse.json) { implicit request =>

    val blog = request.body.as[Blog]

    val b = Blog("",""+request.user.id, request.user.nickname, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()), blog.what)

    db.run( (blogs += b).asTry ).map( res =>
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

  val artefacts = TableQuery[Artefacts]

  def getArtefacts() = SecuredAction.async { implicit request =>

    val getartefacts = for {
      c <- artefacts.sortBy { x => x.id.desc }
    } yield (c)

    db.run(getartefacts.result).map { res =>
    {
      Ok(Json.toJson(res))
    }
    }
  }

  def postArtefact = SecuredAction.async(parse.json) { implicit request =>

    val artefact = request.body.as[Artefact]

    //val b = Artefact(id = "text", content = new HTML,creator = request.user)
    val b = Artefact("",""+request.user.id, artefact.content)

    db.run( (artefacts += b).asTry ).map( res =>
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