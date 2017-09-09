package controllers

import java.util.Calendar

import models.Users.Group
import play.api.Logger
import play.api.libs.json._
import play.libs.Json

/**
  * Created by dannymadell on 12/07/2017.
  */
object Utils {



  def HTMLDecode(str: String) = {

    // return value.replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");

    str.replace("&amp;","&").replace("&gt;",">").replace("&lt;", "<").replace("&quot;", "\"")

  }

  def categoryUpdater(json: Json) = {

    // updates categories here
    //[{"id":1},{"id":2,"children":[{"id":4},{"id":5,"children":[{"id":6},{"id":7},{"id":8}]},{"id":9},{"id":10}]},{"id":3},{"id":11},{"id":12}]
  }

  def logPrinter(str: String) = {
    Logger.info(str)
  }





}
