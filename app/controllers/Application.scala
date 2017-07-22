package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import models.{Blog, Users}
import models.Users.User

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.db.slick.HasDatabaseConfig
import javax.inject.Inject

import models._
import tables._
import play.api.cache._

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
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

import models.Interactions.Interaction
import models.Users.User

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import play.Logger
import play.twirl.api.Html
import slick.backend.DatabasePublisher

import scala.concurrent.duration.Duration


class Application @Inject() (cache: CacheApi)
  extends Controller
    with AllTables
    with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  //val users = TableQuery[Users]
  //val dbArtefacts = TableQuery[Artefacts]
  //val interactions = TableQuery[Interactions]


  val loginForm = Form(

    mapping(
      "id" -> number,
      "user" -> text,
      "password" -> text,
      "nickname" -> text)(User.apply)(User.unpick _))




  //classOf[SomeClass].getMethod("someMethod", classOf[String]).invoke(this, "Some arg")




  def index = Action {


    Redirect(routes.Application.list())
  }

  def list = Action { implicit request =>

    request.session.get("user").map { u =>

      val auth = cache.get[User](u)
      
      if ( ! auth.isEmpty ) 
         Ok(views.html.list(null, loginForm, auth.get.user))
      else
         Ok(views.html.list(null, loginForm, null))
    }.getOrElse{
      Ok(views.html.list(null, loginForm, null))
    }
  }





  def feed = Action {

    implicit request =>

      request.session.get("user").map { u =>

        val auth = cache.get[User](u)





        //val myArtefacts = db.run(artefacts.)

        if ( ! auth.isEmpty ) {


          val getartefacts = for {
            c <- dbArtefacts.sortBy { x => x.id.desc }
          } yield (c)

          val returnResults = Await.result(db.run(getartefacts.result), Duration.Inf)


          Ok(views.html.feed(returnResults, loginForm, auth.get.user, Forms.interactionForm))
        } else
          Ok(views.html.feed(null, loginForm, null, null))
      }.getOrElse{
        Ok(views.html.feed(null, loginForm, null, null))
      }

  }

  def admin = Action { implicit request =>

    request.session.get("user").map { u =>

      val auth = cache.get[User](u)





      if ( ! auth.isEmpty )
        Ok(views.html.admin(loginForm, auth.get.user))
      else
        Ok(views.html.admin(loginForm, null))
    }.getOrElse{
      Ok(views.html.admin(loginForm, null))
    }
  }

  def artefacts = Action { implicit request =>

    request.session.get("user").map { u =>

    val auth = cache.get[User](u)





    if ( ! auth.isEmpty )
      Ok(views.html.artefacts(null, loginForm, auth.get.user))
    else
      Ok(views.html.artefacts(null, loginForm, null))
  }.getOrElse{
    Ok(views.html.artefacts(null, loginForm, null))
  }
}

  val login = Action(parse.form(loginForm)) {
    implicit request =>

      val loginData = request.body

      val q = users.filter { u => u.user === loginData.user && u.password === loginData.password }

//      val q = users.filter { u => u.user === "admin" && u.password === "admin" }


      var id:String = ""
      Await.result(db.run(q.result), Duration.Inf).map { u =>

        id = java.util.UUID.randomUUID().toString

        cache.set(id, u)
      }

      Redirect(routes.Application.feed()).withSession(
          "user" -> id)

  }

  val logout = Action {
    Redirect(routes.Application.list()).withNewSession
  }
}
