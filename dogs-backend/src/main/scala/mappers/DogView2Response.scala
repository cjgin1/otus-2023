package mappers

import db.model.DogView
import dto.DogResponse
import mappers.common.CommonMapper


private[mappers] trait DogView2Response {
  implicit val dogView2ResponseMapper = new CommonMapper[DogView, DogResponse] {
    override def mapModel(model: DogView): DogResponse =
      DogResponse(model.id, model.name, model.idSerial, model.registrationDate, model.breedId, model.breedName)
  }
}
