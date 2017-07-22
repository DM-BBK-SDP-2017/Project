package controllers

import java.sql.Timestamp
import java.util.Calendar

import models.Interactions.Interaction
import models.Users.User
import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile
import tables._


/**
  * Created by dannymadell on 17/07/2017.
  */
object Forms extends InteractionTable  with HasDatabaseConfig[JdbcProfile]  {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._


  val loginForm = Form(

    mapping(
      "id" -> number,
      "user" -> text,
      "password" -> text,
      "nickname" -> text)(User.apply)(User.unpick _))

  val interactionForm = Form(

    mapping(
      "artefact_id" -> number,
        "user_id" -> number,
        "interaction_type" -> text)
    ((artefact_id, user_id, interaction_type) => Interaction(artefact_id,user_id,interaction_type,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) ))
    ((i: Interaction) => Some(i.artefact_id, i.user_id, i.interaction_type)))


}
/*
val stockForm: Form[Stock] = Form(
mapping(
"symbol" -> nonEmptyText.verifying("Doh - Stock already exists (1)!", Stock.findBySymbol(_) == 0),
"company" -> optional(text))
((symbol, company) => Stock(0, symbol, company))
((s: Stock) => Some((s.symbol, s.company))
) verifying("Doh - Stock already exists (2)!", fields => fields match {
case Stock(i, s, c) =>  Stock.findBySymbol(s) == 0
})
)*/