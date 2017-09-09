package models.Intelligence

import java.sql.Timestamp

/**
  * Created by dannymadell on 28/07/2017.
  */
case class NewArtefactRequestFromUser (

  id: Int,
  user_asking: Int, // user asking the question
  question: String, // what is the question
  category: String, // what category does this fall under
  date_requested: Timestamp,
  status: String, // requested, answered, closed

  user_answering: Int, // could be null
  answering_artefact_id: Int,
  date_answered: Timestamp


  // on new request generate three people to ask
)