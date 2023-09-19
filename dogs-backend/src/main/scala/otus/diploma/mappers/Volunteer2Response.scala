package otus.diploma.mappers

import otus.diploma.db.model.Volunteer
import otus.diploma.dto.VolunteerResponse
import otus.diploma.mappers.common.CommonMapper

private [mappers] trait Volunteer2Response {

  implicit val volunteer2ResponseMapper = new CommonMapper[Volunteer, VolunteerResponse] {
    override def mapModel(model: Volunteer): VolunteerResponse =
      VolunteerResponse(
        model.id,
        model.name,
        model.document,
        model.birthdate.toLocalDate,
        model.registrationDate.toLocalDateTime
      )
  }
}
