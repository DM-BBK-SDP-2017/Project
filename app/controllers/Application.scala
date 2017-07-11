package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import models.{Blog, User}
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

class Application @Inject() (cache: CacheApi) extends Controller with tables.UserTable with HasDatabaseConfig[JdbcProfile] {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  val users = TableQuery[Users]

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

        if ( ! auth.isEmpty )
          Ok(views.html.feed(null, loginForm, auth.get.user))
        else
          Ok(views.html.feed(null, loginForm, null))
      }.getOrElse{
        Ok(views.html.feed(null, loginForm, null))
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

      Redirect(routes.Application.artefacts()).withSession(
          "user" -> id)

  }

  val logout = Action {
    Redirect(routes.Application.list()).withNewSession
  }
}
