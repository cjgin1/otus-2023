package mappers

import db.model.Volunteer
import dto.VolunteerResponse
import mappers.common.CommonMapper

private [mappers] trait Volunteer2Response {

  implicit val volunteer2ResponseMapper = new CommonMapper[Volunteer, VolunteerResponse] {
    override def mapModel(model: Volunteer): VolunteerResponse =
      VolunteerResponse(
        model.id,
        model.name,
        model.document,
        model.birthdate,
        model.registrationDate
      )
  }
}
