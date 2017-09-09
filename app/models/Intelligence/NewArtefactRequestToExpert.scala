package models.Intelligence

import java.sql.Timestamp

/**
  * Created by dannymadell on 29/07/2017.
  */
case class NewArtefactRequestToExpert // answering a request for an artefact
(
  request_id: Int,
  to_user: Int,
  status: String, // pending, on hold, answered, ignored, rejected
  last_in_feed_timestamp: Timestamp,
  number_of_times_in_feed: Int // if ignore 3 times remove

)