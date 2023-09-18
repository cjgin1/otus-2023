package otus.diploma.mappers

import otus.diploma.db.model.DogView
import otus.diploma.dto.DogResponse
import otus.diploma.mappers.common.CommonMapper


private[mappers] trait DogView2Response {
  implicit val dogView2ResponseMapper = new CommonMapper[DogView, DogResponse] {
    override def mapModel(model: DogView): DogResponse =
      DogResponse(model.id, model.name, model.idSerial, model.registrationDate, model.breedId, model.breedName)
  }
}
