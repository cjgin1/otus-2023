package otus.diploma.dto

import java.sql.Timestamp
import java.util.UUID

case class DogResponse(id: Long, name: String, idSerial: UUID, registrationDate: Timestamp, breedId: Long, breedName: String)
