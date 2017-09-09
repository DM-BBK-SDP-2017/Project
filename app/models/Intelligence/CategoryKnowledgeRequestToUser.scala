package models.Intelligence

/**
  * Created by dannymadell on 23/07/2017.
  */
case class CategoryKnowledgeRequestToUser // Do you know about this? Score from 1-10? If not me, someone else...?
(
  request_id: Int,
  category: Int,
  user: Int, // i.e. the user being asked
  status: String,// pending, on hold, answered

  answer: Double,//-1 - 1 OPTIONAL
  another_user: Int // i.e. if they answer they don't know but someone else does
)