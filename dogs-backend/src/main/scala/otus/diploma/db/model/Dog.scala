package otus.diploma.db.model

import otus.diploma.db.model.common.CommonModel

import java.sql.Timestamp
import java.util.UUID

case class Dog(override val id: Long = 0, name: String, idSerial: UUID, registrationDate: Timestamp, breedId: Long) extends CommonModel
