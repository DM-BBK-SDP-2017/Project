package controllers

import models.Intelligence.IntelWeightings
import play.api.db.slick.HasDatabaseConfig
import play.api.mvc.Controller
import slick.driver.JdbcProfile
import tables._


/**
  * Created by dannymadell on 10/09/2017.
  */
trait controllers_all extends HasDatabaseConfig[JdbcProfile] with Controller with AllTables with IntelWeightings {

  import play.api.Play
  import play.api.db.slick.DatabaseConfigProvider

  import slick.driver.JdbcProfile

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)



}
