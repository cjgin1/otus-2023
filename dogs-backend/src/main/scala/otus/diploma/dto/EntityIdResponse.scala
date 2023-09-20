package otus.diploma.dto

import zio.json._

case class EntityIdResponse(id : Long)

object EntityIdResponse {
  implicit val decoder: JsonDecoder[EntityIdResponse] = DeriveJsonDecoder.gen[EntityIdResponse]
  implicit val encoder: JsonEncoder[EntityIdResponse] = DeriveJsonEncoder.gen[EntityIdResponse]
}
