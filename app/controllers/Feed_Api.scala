package controllers

import javax.inject.Inject

import models.Intelligence.answerToQuestion.answerToQuestion
import models.Users.User
import play.api.Play
import play.api.cache._
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.Action
import slick.driver.JdbcProfile

import scala.util.{Failure, Success, Try}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.duration._
import play.api.libs.json.{JsValue, Json}
import java.util.Calendar

import models.Blog.timestampFormat
import models.Artefacts.Category
import models.Intelligence.{IntelWeightings, Intelligence}
import models.Interactions.{Comment, Interaction, Message, Recommendation}
import models.Users.Group
import play.Logger
import javax.inject.Inject

import play.Logger.info
import play.api.cache._
import play.api.mvc.Result

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import play.api.data.format.Formats._

import scala.None



class Feed_Api @Inject() (cache: CacheApi) extends controllers_all {

  //val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  val answerToQuestionForm = Form(
    mapping(
      "request_id" -> number,
      "from_user" -> number,
      "question" -> text,
      "category" -> number,
      "status" -> text)(answerToQuestion.apply)(answerToQuestion.unapply))


  def answerToQuestionResponse = Action(parse.form(answerToQuestionForm)) {
    implicit request =>
      val answerToQuestionData = request.body


      val user_id: Int =
        request.session.get("user").map { u =>
          val auth = cache.get[User](u)
          auth.get.id
        }.get

      val newAnswerToQuestion = answerToQuestion(
        0,
        user_id,
        answerToQuestionData.question,
        answerToQuestionData.category,
        "New"
      )

      Logger.info(newAnswerToQuestion.toString())

      val query = answerToQuestions += newAnswerToQuestion


      val queryResult = db.run(query.asTry).map(res =>
        res match {
          case Success(res) => Redirect(routes.Application.feed()).flashing("success" -> "Your question has been submitted!")
          case Failure(res) => Redirect(routes.Application.feed()).flashing("Failed " -> "There's been an error, please try again or contact your administrator")
        })

      Await.result(queryResult, 5 seconds)


  }


  val somethingKnownAbout = Form(
    mapping(
      "intel_id" -> number,
      "identified_by" -> number,
      "identifier_how" -> text,
      "user_id" -> number,
      "knows_about_category" -> number,
      "knowledge_strength" -> of(doubleFormat),
      "identified_timestamp" -> ignored(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())))(Intelligence.apply)(Intelligence.unapply))


  /*
  *  For both self submitted AND other submitted intelligences
  * */
  def somethingIKnowAboutFormResponse = Action(parse.form(somethingKnownAbout)) {

    implicit request =>

      val formData = request.body
      Logger.info("formData: " + formData.toString)

      val identified_by: Int =
        request.session.get("user").map { u =>
          val auth = cache.get[User](u)
          auth.get.id
        }.get


      val newIntelligence = formData.user_id match {
        case -1 => {
          Logger.info("SELF")

          Intelligence(
            0,
            identified_by,
            "selfsubmitted",
            identified_by,
            formData.knows_about_category,
            FROM_CATEGORY_KNOWLEDGE_RESPONSE_SELF,
            formData.identified_timestamp
          )


        }
        case someoneElse: Int => {
          Logger.info("SOMEONEELSE")
          Intelligence(
            0,
            identified_by,
            "othersubmitted",
            someoneElse,
            formData.knows_about_category,
            FROM_CATEGORY_KNOWLEDGE_RESPONSE_OTHER,
            formData.identified_timestamp
          )
        }
      }

      Logger.info("new intel: " + newIntelligence.toString)


      val q = intelligences.filter { intel => intel.knows_about_category === newIntelligence.knows_about_category && intel.identified_by === newIntelligence.identified_by && intel.user_id === newIntelligence.user_id }

      val k: Seq[Intelligence] = Await.result(db.run(q.result), 5 seconds)

      Logger.info(k.toString)

      if (k.nonEmpty) {

        db.run(q.delete)

      }

      val query = intelligences += newIntelligence

      val query2 = db.run(query.asTry).map { res2 =>
        res2 match {
          case Success(a) => Redirect(routes.Application.feed()).flashing("success" -> "Thank you!")
          case Failure(a) => Redirect(routes.Application.feed()).flashing("failure" -> "There's been an error, please try again or contact your administrator")
        }

      }


      Await.result(query2, 5 seconds)


    //Redirect(routes.Application.feed()).flashing("success" -> "Contact saved!")

  }

  def somethingSomeoneElseKnowsAboutFormResponse = Action(parse.form(somethingKnownAbout)) {
    implicit request =>

      val formData = request.body
      //Logger.info("formData: " + formData.toString)

      val identified_by: Int =
        request.session.get("user").map { u =>
          val auth = cache.get[User](u)
          auth.get.id
        }.get


      val newIntelligence = Intelligence(
        0,
        identified_by,
        "othersubmitted",
        formData.user_id,
        formData.knows_about_category,
        FROM_CATEGORY_KNOWLEDGE_RESPONSE_OTHER,
        formData.identified_timestamp
      )


      val q = intelligences.filter { intel => intel.knows_about_category === newIntelligence.knows_about_category && intel.identified_by === newIntelligence.identified_by && intel.user_id === newIntelligence.user_id }

      val k: Seq[Intelligence] = Await.result(db.run(q.result), 5 seconds)

      Logger.info(k.toString)

      if (k.nonEmpty) {

        db.run(q.delete)

      }

      val query = intelligences += newIntelligence

      val query2 = db.run(query.asTry).map { res2 =>
        res2 match {
          case Success(a) => Redirect(routes.Application.feed()).flashing("success" -> "Thank you!")
          case Failure(a) => Redirect(routes.Application.feed()).flashing("failure" -> "There's been an error, please try again or contact your administrator")
        }

      }


      Await.result(query2, 5 seconds)
  }

  /*
      if (k) {

        //update existing

        Redirect(routes.Application.feed()).flashing("success" -> "Contact saved!")

      } else {

        val query = intelligences += newIntelligence

        val query2 = db.run(query.asTry).map { res2 =>
          res2 match {
            case Success(a) => Redirect(routes.Application.feed()).flashing("success" -> "Contact saved!")
            case Failure(a) => Redirect(routes.Application.feed()).flashing("failure" -> "Contact not saved!")
          }
          Await.result(query2, 5 seconds)
        }






      val qresult = db.run(q.asTry).map { res =>

        res match {
          case Success(a) => {
            val query = intelligences += newIntelligence
            val query2 = db.run(query.asTry).map { res2 =>
              res2 match {
                case Success(a) => Redirect(routes.Application.feed()).flashing("success" -> "Contact saved!")
                case Failure(a) => Redirect(routes.Application.feed()).flashing("failure" -> "Contact not saved!")
              }
              Await.result(query2, 5 seconds)

            }
          }
          case Failure(a) => Redirect(routes.Application.feed()).flashing("success" -> "Contact saved!")
        }


      }

        Await.result(qresult, 5 seconds)

      if (k == false) {
      val query = intelligences += newIntelligence
      Await.result(db.run(q), Duration.Inf)
        Redirect(routes.Application.feed()).flashing("success" -> "Contact saved!")
    } else {
        Redirect(routes.Application.feed()).flashing("success" -> "Contact saved!")
      }


*/



  val suggestNewCategoryForm = Form(
    mapping(
      "category_id" -> number,
      "category_name" -> text,
      "parent" -> number)(Category.apply)(Category.unapply))



  def suggestNewCategoryFormResponse = Action(parse.form(suggestNewCategoryForm)) {

    implicit request =>

      val newCategorySuggestedRequestBody = request.body

      val newCategoryRequested = Category(
        newCategorySuggestedRequestBody.category_id,
        newCategorySuggestedRequestBody.category_name,
        newCategorySuggestedRequestBody.parent
      )

      val user_id: Int =
        request.session.get("user").map { u =>
          val auth = cache.get[User](u)
          auth.get.id
        }.get



      Logger.info(newCategoryRequested.toString())

      val query = categories += newCategoryRequested


      val queryResult = db.run(query.asTry).map(res =>
        res match {
          case Success(res) => Redirect(routes.Application.feed()).flashing("success" -> "Your category has been submitted!")
          case Failure(res) => Redirect(routes.Application.feed()).flashing("Failed " -> "There's been an error, please try again or contact your administrator")
        })

      Await.result(queryResult, 5 seconds)


  }



  val usefulForGroupForm = Form(
    mapping(
      "category_id" -> number,
      "category_name" -> text,
      "parent" -> number)(Category.apply)(Category.unapply))

  }














