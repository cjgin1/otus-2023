package dto

import java.sql.{Date, Timestamp}

case class VolunteerResponse(id: Long, name: String, document: String, birthDate: Date, registrationDate: Timestamp)
