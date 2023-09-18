package db.model

import db.model.common.CommonModel

import java.sql.Timestamp
import java.util.UUID

case class DogView(override val id: Long = 0, name: String, idSerial: UUID, registrationDate: Timestamp, breedId: Long, breedName: String) extends CommonModel
