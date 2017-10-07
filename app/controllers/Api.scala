package controllers

import models._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import java.util.Calendar

import models.Blog.timestampFormat
import models.Artefacts.Category
import models.Intelligence.IntelWeightings
import models.Interactions.{Comment, Interaction, Message, Recommendation}
import models.Users.{Group, User}

import scala.util.Success
import scala.util.Failure
import play.Logger
import javax.inject.Inject

import play.api.cache._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class Api @Inject() (cache: CacheApi) extends controllers_all {


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

  def getArtefactById(searchId: String) = SecuredAction.async { implicit request =>


    val getartefacts = for {
      c <- artefacts.filter(x => x.id === searchId.toInt) //.sortBy { x => x.id.desc }
    } yield (c)

    db.run(getartefacts.result).map { res => {
      Ok(Json.toJson(res))
    }
    }
  }

  def artefactSearch(searchString: String) = SecuredAction.async { implicit request =>


    val getartefacts = for {
      c <- artefacts.filter(x =>
        x.content.like("%" + searchString + "%") || x.tags_ids_string.like("%" + searchString + "%")) //.sortBy { x => x.id.desc }

    } yield (c)


    db.run(getartefacts.result).map { res => {
      //Logger.info(res.length)
      Ok(Json.toJson(res))
    }
    }
  }

  def postArtefact = SecuredAction.async(parse.json) { implicit request =>

    val artefact = request.body.as[Artefact]
    //val artefactTag_Results = db.run(artefact_tags.result).value


    // val q = for (a <- artefact_tags) yield a.artefact_Tag -> a.artefact_Tag_Id
    // val a = q.result
    // val results = Await.result(db.run(a), Duration("2 seconds")).toMap
    // Logger.info(results.toString)
    //val artefactTag_Results: Map[String,Int] = Await.result(db.run(a), 1 second)


    //
    //
    //




    val b = Artefact(
      id = 0,
      content = artefact.content,
      creator = request.user.id,
      //categories_id = artefact.categories_id,
      //tags_ids_string = (for (a <- artefact.tags_ids_string.split(',')) yield {artefactTag_Results.get.filter(x => x.artefact_Tag_Id === a).artefact_Tag_Id}).mkString(","),
      // DOESN'T ALWAYS STORE TAGS tags_ids_string = artefact.tags_ids_string.split(",").map(x => results.get(x)).mkString(","),
      tags_ids_string = artefact.tags_ids_string,
      category_id = artefact.category_id,
      created = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))

    val user_id: Int =
      request.session.get("user").map { u =>
        val auth = cache.get[User](u)
        auth.get.id
      }.get



    IntelligenceHelper.addIntelligence(b, FROM_NEW_ARTEFACT, user_id)



    db.run((artefacts += b).asTry).map(res =>
      res match {
        case Success(res) => {

          val q = for (a <- artefact_tags) yield a.artefact_Tag
          val a = q.result
          val results = db.run(a)

          results.onComplete {
            case Success(x) => {
              val artefact_tags = TableQuery[ArtefactTags]

              for (t <- artefact.tags_ids_string.split(",")) {
                if (!x.contains(t)) {
                  db.run(artefact_tags += ArtefactTag(
                    artefact_Tag_Id = 0,
                    artefact_Tag = t,
                    creator_id = request.user.id,
                    created = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())))
                }
              }
              x
            }
            case Failure(x) => Logger.info(s"Writing artefact $x failed");


          }
          IntelligenceHelper.generateArtefactValidation(b)
          Ok(Json.toJson(b))

        }
        case Failure(e) => {
          Logger.error(s"Problem on insert, ${e.getMessage}")
          InternalServerError(s"Problem on insert, ${e.getMessage}")
        }
      }
    )
    //Future.successful(Ok("added"))
  }


  def updateArtefact = SecuredAction.async(parse.json) { implicit request =>

    val artefact = request.body.as[Artefact]
    Logger.info(artefact.content)

    //val existingArtefactContent = (for { c <- artefacts if c.id === artefact.id } yield (c.content, c.tags_ids_string).update((artefact.content, artefact.tags_ids_string))

    //val existingArtefactContent = for { c <- artefacts if c.id === artefact.id } yield (c.content)
    val existingArtefactContent = artefacts.filter(_.id === artefact.id).map(c => (c.content, c.tags_ids_string, c.category_id))
    Logger.info(existingArtefactContent.updateStatement.toString)

    db.run((existingArtefactContent.update((artefact.content, artefact.tags_ids_string, artefact.category_id)).asTry).map(res => res match {
      case Success(res) => {
        Logger.info(res.value.toString)

        val q = artefact_tags
        val a = q.result
        val results = db.run(a)

        results.onComplete {
          case Success(x) => {
            val artefact_tags = TableQuery[ArtefactTags]

            for (t <- artefact.tags_ids_string.split(",")) {
              if (!x.contains(t)) {
                db.run(artefact_tags += ArtefactTag(
                  artefact_Tag_Id = 0,
                  artefact_Tag = t,
                  creator_id = request.user.id,
                  created = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())))
              }
            }
            x
          }
          case Failure(x) => Logger.info(s"Writing artefact $x failed");


        }


        Ok("Updated")
      }
      case Failure(e) => {
        Logger.error(s"Problem on insert, ${e.getMessage}")
        InternalServerError(s"Problem on insert, ${e.getMessage}")
      }


    })
    )


    //  val updateStatement = artefacts.filter(_.id === artefact.id)
    //    .map (x => (x.content, x.tags_ids_string))
    //    .update(artefact.content, artefact.tags_ids_string)

    //Logger.info(updateStatement.toString)

    //Future.successful(Ok("Updated"))


    // Try(db.run(updateStatement).recover{ex: Throwable => Logger.error("Error occured when inserting user", ex)}) match {
    //   case Success(e) => {
    //     Logger.info(e.value.toString)
    //     Future.successful(Ok("Updated"))
    //   }
    //   case Failure(e) => {
    //     Logger.error(s"Problem on insert, ${e.getMessage}")
    //     Future.failed(e)
    //   }

    // }


    //Ok("success");


    //val existingArtefactTags = (for { c <- artefacts if c.id === artefact.id } yield c.tags_ids_string).update(artefact.tags_ids_string)


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

  def getArtefactTagsForFeed() = SecuredAction.async {

    implicit request =>


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

    // for debugging 500 error

    Logger.info(request.body.toString)
    //Thread.sleep(2000)
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

    // To try to fix issue of 500 error
    //Thread.sleep(200)


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


  def interaction = SecuredAction.async(parse.json) {
    implicit request =>


      val interactionBody = request.body.as[Interaction]
      val newInteraction = Interaction(
        interactionBody.artefact_id,
        request.user.id,
        interactionBody.interaction_type,
        new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
      )

      val query = interactions += newInteraction

      db.run((interactions += newInteraction).asTry).map(res =>
        res match {
          case Success(res) => Ok("Interaction recorded");
          case Failure(res) => BadRequest(s"Writing artefact $res failed");
        })

  }

  def message = SecuredAction.async(parse.json) {

    implicit request =>

      Logger.info(request.body.toString())

      val message = request.body.as[Message]

      val newMessage = Message(
        message_id = 0,
        message_type = message.message_type,
        from_user = request.user.id,
        to_user = message.to_user,
        artefact_id = message.artefact_id,
        message = message.message,
        timestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
      )

      val query = messages += newMessage

      db.run(
        query.asTry
          .map(res =>
            res match {
              case Success(res) => Ok("Message recorded");
              case Failure(res) => BadRequest(s"Sending message $res failed");
            }
          )
      )


  }


  def addGroup() = SecuredAction.async(parse.json) {
    implicit request =>




      val groupBody = request.body.as[Group]
      val newGroup = Group(
        groupBody.id,
        groupBody.group_name,
        request.user.id,
        // 1,
        // "group1",
        // 1,
        new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
      )

      // Writes Group as a normal value

      implicit val jsonReadWriteFormatTrait = Json.format[Group]
      val query = groups += newGroup

      db.run(query.asTry).map(res =>
        res match {
          case Success(res) => Ok("Group recorded");
          case Failure(res) => BadRequest(s"Writing group $res failed");
        })

  }


  def getGroups() = SecuredAction.async {

    implicit request =>


      val getGroups = for {
        c <- groups.sortBy { x => x.id.desc }
      } yield (c)


      //Json.arr() :+(getartefacttags)
      db.run(getGroups.result).map { res => {





        //def convertGroupsToJsonOrig(groups: Seq[Group]): JsValue = {
        //  Json.toJson(
        //    groups.map { t => Map("id" -> t.id, "name" -> t.group_name)}
        //  )
        //}

        // val str = res.map { t => "\"id\": \""+t.id+"\", \"name\": \""+t.group_name+"\""}
        //  val returnval = "{" + str.toString()
        //Ok(str.toString())

        Ok(Json.toJson(res))


        // val mappedGroups = (
        //   res.map(a => "id" -> a.id, "name" -> a.group_name).toMap());
        //
        //val mappedGroups = res.map (a => (("\"id\":" + a.id.toString), ("name\":" + "\"" + a.group_name)))
        //Ok(Json.arr() :+ (Json.toJson(res)))
        //Ok(scala.util.parsing.json.JSONObject(mappedGroups).toString())
      }
      }


  }


  def postRecommendation() = SecuredAction.async(parse.json) {
    implicit request =>

      val recBody = request.body.as[Recommendation]


      // (id: Int,
      //     recommended_by: Int,
      //     recommended_for: Int,
      //     recommendation_score: Double,
      //     recommended_timestamp: Timestamp)



      val newRec = Recommendation(
        recBody.id,
        recBody.artefact_id,
        request.user.id,
        recBody.recommended_for,
        recBody.recommendation_score,
        new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
      )

      Logger.info(newRec.recommended_for.toString);

      val query = recommendations += newRec

      db.run(query.asTry).map(res =>
        res match {
          case Success(res) => Ok("Recommendation recorded");
          case Failure(res) => BadRequest(s"Writing recommendation $res failed");
        })

  }

  def postComment() = SecuredAction.async(parse.json) {
    implicit request =>

      val commentBody = request.body.as[Comment]

  //     comment_id: Int,
  //   from_user: Int,
  //   artefact_id: Int,
  //   comment_content: String,
  //   comment_timestamp: Timestamp

      val newComment = Comment(
        commentBody.comment_id,
        request.user.id,
        commentBody.artefact_id,
        commentBody.comment_content,
        new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
      )

      Logger.info(newComment.comment_content.toString);

      val query = comments += newComment

      db.run(query.asTry).map(res =>
        res match {
          case Success(res) => Ok("Comment recorded");
          case Failure(res) => BadRequest(s"Writing comment $res failed");
        })

  }

  def updateCategories = SecuredAction.async(parse.json) { implicit request =>

    val future = scala.concurrent.Future {

      val children = request.body \\ "id"

      //Logger.info("children " + children.size.toString)


      //]}]}5:"di"{,}4:"di"{[:"nerdlihc",3:"di"{,}2:"di"{,}1:"di"{[

      val pattern = """nerdlihc",[0-9]+""".r

      // val pattern(a, b) = pattern

      val str = request.body.toString()
      Logger.info("finallmatch " + List(pattern.findAllMatchIn(str)) )
      Logger.info(pattern.findAllMatchIn(str).size.toString)



      for (c <- children if (c.as[Int] != 0)) {

        val id = c.as[Int]

        // Logger.info("STRING " + str)
        Logger.info("id " + (for {cat <- categories if cat.category_id === id} yield cat.parent).toString)
        // Logger.info("index " + ("\"id\":" + id))
        // Logger.info("STRING SLICE" + str.slice(0, str.indexOf("\"id\":" + id)))
        // Logger.info("STRING SLICE REVERSE" + str.slice(0, str.indexOf("\"id\":" + id)).reverse)//.slice(14, 14 + id.toString.length + 1))
        // Logger.info("MATCH" + pattern.findFirstIn(str.slice(0, str.indexOf("\"id\":" + id)).reverse))
        // Logger.info("Int " + pattern.findFirstIn(str.slice(0, str.indexOf("\"id\":" + id)).reverse).get.slice(10, 10 + id.toString.length).reverse.toInt )


        val parentId: Int = pattern.findFirstIn(str.slice(0, str.indexOf("\"id\":" + id)).reverse).get.slice(10, 10 + id.toString.length).reverse.toInt
        val q = for {cat <- categories if cat.category_id === id} yield cat.parent
        val updateAction = q.update(parentId)
        //Logger.info(db.run(updateAction).toString)

        db.run(updateAction.asTry).map(res =>
          res match {
            case Success(res) => Logger.info(s"Category: $res records updated");
            case Failure(res) => Logger.info(s"Writing categories: $res records failed");
          })
      }


      //NEED TO UPDATE ALL PARENTS TO -1

    //  pattern.findAllMatchIn(str).size match {
    //    case 0 => {
//
    //        val q = for {cat <- categories} yield cat.parent
    //        val updateAction = q.update(-1)
    //        db.run(updateAction.asTry).map(res =>
    //        res match {
    //          case Success(res) => Logger.info(s"Category $res updated");
    //          case Failure(res) => Logger.info(s"Writing category $res failed");
    //        })
    //      }
    //    case _ => {
//
    //
    //  }
//
      //categoryresults

   //  }

   }

    future.map(i => Ok("Written"))


// Category.innerObj.updateCategories(request.body)


//   (res =>
// res match {
//   case Success(res) => Ok("res");
//   case Failure(res) => BadRequest(s"Writing categories failed");


// })



  }

  def getCategories() = SecuredAction { implicit request =>

          Ok(Category.innerObj.getCategoryPathsForNewArtefact())
  }

  def addCategory() = SecuredAction.async(parse.json) { implicit request =>

    implicit val jsonReadWriteFormatTrait = Json.format[Category]
    val category = request.body.as[Category]
    val newCategory = Category(
      0,
      category.category_name,
      category.parent
    )

    //Logger.info(newComment.comment_content.toString);

    val query = categories += newCategory

    db.run(query.asTry).map(res =>
      res match {
        case Success(res) => Ok(s"Category $res recorded");
        case Failure(res) => BadRequest(s"Writing category $res failed");
      })

  }



  def getUsers() = SecuredAction { implicit request =>

    implicit object customUserJsonFormatter extends Format[User] {

      def writes(x: User): JsValue = {
        Json.obj(
          "id" -> JsNumber(x.id),
          "name" -> JsString(x.nickname)

        )
      }

      def reads(json: JsValue): JsResult[User] = {

        val str = json.as[String]
        //JsSuccess(Category(0,(json \ "name").get.toString(),0,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())))

        JsSuccess(User(0,"","",""))

      }
    }



    val query = users.sortBy(user => user.nickname).result
    val queryResult = Await.result(db.run(query.asTry).map { res =>

      res match {
        case Success(a) => Ok(Json.toJson(a))
        case Failure(a) => BadRequest(s"$a")
      }
    }, Duration.Inf)

    queryResult

  }
}