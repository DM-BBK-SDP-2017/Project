package models.Intelligence

/**
  * Created by dannymadell on 29/07/2017.
  */
trait IntelWeightings {

  val FROM_NEW_ARTEFACT: Double = 0.5
  val FROM_ARTEFACT_VALIDATION: Double = 0.5
  val FROM_CATEGORY_KNOWLEDGE_RESPONSE_SELF = 0.5
  val FROM_CATEGORY_KNOWLEDGE_RESPONSE_OTHER = 0.8

}
