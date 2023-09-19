package otus.diploma.dto

import zio.json._

case class AddDogRequest(name: String, breedName: String)

object AddDogRequest {
  implicit val decoder: JsonDecoder[AddDogRequest] = DeriveJsonDecoder.gen[AddDogRequest]
  implicit val encoder: JsonEncoder[AddDogRequest] = DeriveJsonEncoder.gen[AddDogRequest]
}