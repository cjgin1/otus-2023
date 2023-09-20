package otus.diploma.db.model

import otus.diploma.db.model.common.CommonModel

import java.sql.Timestamp
import java.util.UUID

case class DogView(override val id: Long = 0,
                   name: String,
                   idSerial: UUID,
                   registrationDate: Timestamp,
                   breedId: Long,
                   breedName: String,
                   fosterHostId: Option[Long],
                   fosterHostRegistrationDate: Option[Timestamp],
                   volunteerId: Option[Long],
                   volunteerName: Option[String],
                   volunteerDocument: Option[String]) extends CommonModel
