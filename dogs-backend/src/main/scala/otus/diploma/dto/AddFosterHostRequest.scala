package otus.diploma.dto

import zio.json._

case class AddFosterHostRequest(dogId: Long, fosterHostId: Long)

object AddFosterHostRequest {
  implicit val decoder: JsonDecoder[AddFosterHostRequest] = DeriveJsonDecoder.gen[AddFosterHostRequest]
  implicit val encoder: JsonEncoder[AddFosterHostRequest] = DeriveJsonEncoder.gen[AddFosterHostRequest]
}