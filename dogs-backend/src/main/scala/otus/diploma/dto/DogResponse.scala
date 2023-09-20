package otus.diploma.dto

import zio.json._

import java.time.LocalDateTime
import java.util.UUID

case class DogResponseWrapper(list: List[DogResponse])

object DogResponseWrapper {
  implicit val decoder: JsonDecoder[DogResponseWrapper] = DeriveJsonDecoder.gen[DogResponseWrapper]
  implicit val encoder: JsonEncoder[DogResponseWrapper] = DeriveJsonEncoder.gen[DogResponseWrapper]
}
case class DogResponse(id: Long, name: String,
                       idSerial: UUID,
                       registrationDate: LocalDateTime,
                       breedId: Long,
                       breedName: String,
                       fosterHostId: Option[Long],
                       fosterHostRegistrationDate: Option[LocalDateTime],
                       volunteerId: Option[Long],
                       volunteerName: Option[String],
                       volunteerDocument: Option[String])
object DogResponse {
  implicit val decoder: JsonDecoder[DogResponse] = DeriveJsonDecoder.gen[DogResponse]
  implicit val encoder: JsonEncoder[DogResponse] = DeriveJsonEncoder.gen[DogResponse]
}