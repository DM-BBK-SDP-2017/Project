package controllers

import java.util.Calendar

import models.Users.Group
import play.api.libs.json._

/**
  * Created by dannymadell on 12/07/2017.
  */
object Utils {



  def HTMLDecode(str: String) = {

    // return value.replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");

    str.replace("&amp;","&").replace("&gt;",">").replace("&lt;", "<").replace("&quot;", "\"")

  }








}
