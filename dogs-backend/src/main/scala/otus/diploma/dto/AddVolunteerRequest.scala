package otus.diploma.dto

import zio.json._

import java.time.LocalDate

case class AddVolunteerRequest(name: String, document: String, birthDate: LocalDate)

object AddVolunteerRequest {
  implicit val decoder: JsonDecoder[AddVolunteerRequest] = DeriveJsonDecoder.gen[AddVolunteerRequest]
  implicit val encoder: JsonEncoder[AddVolunteerRequest] = DeriveJsonEncoder.gen[AddVolunteerRequest]
}