package otus.diploma.dto

import zio.json._

import java.time.{LocalDate, LocalDateTime}

case class VolunteerResponseWrapper(list: List[VolunteerResponse])

object VolunteerResponseWrapper {
  implicit val decoder: JsonDecoder[VolunteerResponseWrapper] = DeriveJsonDecoder.gen[VolunteerResponseWrapper]
  implicit val encoder: JsonEncoder[VolunteerResponseWrapper] = DeriveJsonEncoder.gen[VolunteerResponseWrapper]
}

case class VolunteerResponse(id: Long, name: String, document: String, birthDate: LocalDate,
                             registrationDate: LocalDateTime)

object VolunteerResponse {
  implicit val decoder: JsonDecoder[VolunteerResponse] = DeriveJsonDecoder.gen[VolunteerResponse]
  implicit val encoder: JsonEncoder[VolunteerResponse] = DeriveJsonEncoder.gen[VolunteerResponse]
}
